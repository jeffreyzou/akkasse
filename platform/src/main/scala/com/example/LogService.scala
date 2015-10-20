package com.example

import java.io.InputStream

import akka.actor.{Props, Actor, ActorRef, ActorSystem}
import akka.cluster.pubsub.{DistributedPubSubMediator, DistributedPubSub}
import akka.cluster.pubsub.DistributedPubSubMediator.{Subscribe, Publish}
import akka.event.Logging
import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.model.headers.Accept
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers._
import akka.stream.{ActorMaterializer, OverflowStrategy, Materializer}
import akka.stream.scaladsl.{Source, Flow}
import com.example.ParagraphGenerator._
import de.heikoseeberger.akkasse.{EventStreamMarshalling, ServerSentEvent, WithHeartbeats}
import org.joda.time.DateTime
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization._
import org.slf4j.Logger
import scala.collection.immutable.Queue
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import com.example.LogMessages.{GetLogs, LogEntry, LogPulse}
import akka.pattern.ask
import scala.io.{Source => FSource}
import scala.util.{Success, Failure, Random}
import akka.pattern.{ ask, pipe }
import akka.stream.scaladsl.{ ImplicitMaterializer, Source }
import scala.concurrent.ExecutionContext



object LogService  {

  implicit val formats = org.json4s.DefaultFormats ++ org.json4s.ext.JodaTimeSerializers.all

  def logRoute(system: ActorSystem)(implicit ec: ExecutionContext, mat: Materializer) = {
    import Directives._
    logRequest("LOG") {
      pathEnd {
        put {
          val idx = LogSourceRegistry.createAndRegisterLogSource(500 millis, system)
          val responsecode = 201
          complete(
            HttpResponse(responsecode,
              headers = List(RawHeader("Content-TypeXX", "application/json"), Location(s"https://ws-test.modeler.gy/log/$idx")),
              entity = s"Created a log emitting entity: ${idx}")
          )
        }
      }
    }
  }

  def logGetRoute(system: ActorSystem)(implicit ec: ExecutionContext, mat: Materializer) = {
    path(IntNumber) { logId =>
      get {
        parameters('count.as[Int] ? 10) { pageSize =>
          LogSourceRegistry(logId) match {
            case Some(logEmitter) => {
              onComplete(logEmitter.ask(GetLogs(pageSize))(3 seconds).mapTo[List[LogEntry]]) {
                case Success(l) => {
                  complete(HttpResponse(status = 200,
                    headers = List(Location(s"https://ws-test.modeler.gy/log/$logId")),
                    entity = pretty(render(parse(write(l)))))
                  )
                }
                case Failure(ex) => {
                  complete(HttpResponse(404, List(), s"No log emitter found for ${logId}"))
                }
              }
            }
            case None => complete(HttpResponse(404, List(), s"No log emitter found for ${logId}"))
          }
        }
      }
    }
  }

  //LogEntry(DateTime.now().getMillis, "log", MessageGenerator.oneMessage()

  def sseRoute(system: ActorSystem)(implicit ec: ExecutionContext, mat: Materializer) = {
    import Directives._
    import EventStreamMarshalling._

    logRequest("SSE-IN") {
      path(IntNumber) { logId => //  "sse" / IntNumber
        get {
          LogSourceRegistry(logId) match {
            case Some(emitter) => {
              println(s"Creating and attaching a SSE stream for $logId")
              complete {
                Source.actorRef[LogMessages.LogEntry](10, OverflowStrategy.dropTail)
                  .map(LogMessages.logEventToServerSentEvent)
                  .mapMaterializedValue(source =>  DistributedPubSub(system).mediator ! Subscribe(logId.toString(),source) )

                //.mapMaterializedValue( source => LogSourceRegistry.getOrCreateRouter(logId,system) ! LogMessages.SubscribeToEmitter(source) )
              }
            }
            case None => complete(HttpResponse(404))
          }
        }
      }
    } ~
    path(Rest) {
      leftover => {
        logRequest(showRest _) {
          complete(leftover + "   not matched")
        }
      }
    }
  }
}

class LogRouter(id : Int) extends Actor {
  val log = Logging(context.system, this)
  val pubSubMediator = DistributedPubSub(context.system).mediator
  pubSubMediator ! Subscribe( id.toString, self )

  def receive = {
    case LogMessages.SubscribeToEmitter(a: ActorRef) => {
      context.become( active(a) )
    }
  }

  def active(subscriber : ActorRef) : Receive = {
    case l : LogMessages.LogEntry => {
      subscriber ! l
    }
  }
}

class LogEmitter( id :Int, f : FiniteDuration ) extends Actor {
  import context.dispatcher
  import LogMessages._
  val log = Logging(context.system, this)
  val beat = context.system.scheduler.schedule(1 second, f, self, LogPulse)
  val pubSubMediator = DistributedPubSub(context.system).mediator
  implicit val m = ActorMaterializer()
  var buffer = Queue[LogEntry]()
  val buffSize = 50

  def receive = {
    case LogPulse => {
      val e = LogEntry(DateTime.now().getMillis, CatGenerator.one(),
        MessageGenerator.oneMessage(), ParagraphGenerator.one())
      //log.debug(s"received a logging pulse - generated : \n\t${e}")
      buffer = buffer.enqueueFinite(e,buffSize)
      pubSubMediator ! Publish(id.toString,e)
    }
    case GetLogs( size ) => {
      log.debug(s"get logs called by ${sender.path} for $size entries : \n\t${buffer.reverse.take(size).mkString("  ")}")
      sender ! buffer.reverse.take(size).toList
    }
  }

  override def postStop = {
    beat.cancel()
  }


}

object LogMessages {
  case class LogEntry( timestamp : Long, category : String, message : String, description : String )
  case class LogPulse()
  case class GetLogs( size : Int )
  case class SubscribeToEmitter( a : ActorRef)

  implicit val formats = org.json4s.DefaultFormats ++ org.json4s.ext.JodaTimeSerializers.all
  def logEventToServerSentEvent(event: LogEntry): ServerSentEvent = {
      val data = compact(render(parse(write(event))))
      ServerSentEvent(data, "added")
  }
}


object LogSourceRegistry {
  private var registry : Map[Int,ActorRef] = Map()
  private var routers : Map[Int,ActorRef] = Map()

  def createAndRegisterLogSource( f : FiniteDuration, system : ActorSystem ) : Int = {
    val idx = maxidx + 1
    registry = registry + (idx -> system.actorOf(Props(classOf[LogEmitter],idx,f)))
    idx
  }

  def getOrCreateRouter( logId : Int, system: ActorSystem) : ActorRef = {
    routers.get(logId) match {
      case Some(r) => r
      case None => {
        val r = system.actorOf(Props(classOf[LogRouter],logId))
        routers = routers + (logId -> r)
        r
      }
    }
  }

  def apply(id : Int) : Option[ActorRef] = {
    registry.get(id)
  }

  def maxidx : Int = {
    registry.keySet match {
      case l if l.isEmpty => 0
      case l => l.max
    }
  }
}

object MessageGenerator {
  val nouns = FSource.fromInputStream(getClass.getResourceAsStream("/nouns")).getLines().toList
  val verbs = FSource.fromInputStream(getClass.getResourceAsStream("/verbs")).getLines().toList
  val adjectives = FSource.fromInputStream(getClass.getResourceAsStream("/adjectives")).getLines().toList
  val objects = FSource.fromInputStream(getClass.getResourceAsStream("/objects")).getLines().toList
  val adverbs = FSource.fromInputStream(getClass.getResourceAsStream("/adverbs")).getLines().toList
  var r = Random

  def oneMessage() : String = {
    val n = rE(nouns)
    val a = n.charAt(0) match {
      case 'a'|'e'|'o'|'i'|'u' => "an"
      case _ => if(r.nextBoolean()) "a" else "the"
    }
    val v = rE(verbs)
    val adj = rE(adjectives)
    val o = rE(objects)
    val ad = rE(adverbs)
    s"$a $n $v $adj $o $ad"
  }

  def rE(list: Seq[String]): String = list(r.nextInt(list.length))
}

object CatGenerator {
  val t1 = FSource.fromInputStream(getClass.getResourceAsStream("/title01.txt")).getLines().toList
  val t2 = FSource.fromInputStream(getClass.getResourceAsStream("/title02.txt")).getLines().toList
  var r = Random

  def one() : String = {
    s"${rE(t1)} ${rE(t2)}"
  }

  def rE(list: Seq[String]): String = list(r.nextInt(list.length))
}

object ParagraphGenerator {
  val l1 = FSource.fromInputStream(getClass.getResourceAsStream("/list01.txt")).getLines().toList
  val l2 = FSource.fromInputStream(getClass.getResourceAsStream("/list02.txt")).getLines().toList
  val l3 = FSource.fromInputStream(getClass.getResourceAsStream("/list03.txt")).getLines().toList
  val l4 = FSource.fromInputStream(getClass.getResourceAsStream("/list04.txt")).getLines().toList
  val l5 = FSource.fromInputStream(getClass.getResourceAsStream("/list05.txt")).getLines().toList
  val l6 = FSource.fromInputStream(getClass.getResourceAsStream("/list06.txt")).getLines().toList
  val l7 = FSource.fromInputStream(getClass.getResourceAsStream("/list07.txt")).getLines().toList
  val l8 = FSource.fromInputStream(getClass.getResourceAsStream("/list08.txt")).getLines().toList

  var r = Random

  def one() : String = {
    val o1 = rE(l1)
    val a1 = aan(o1)

    val o4 = rE(l4)
    val a4 = aan(o4)

    s"In $a1 $o1 ${rE(l2)} a young ${rE(l3)} stumbles across $a4 $o4 which spurs him into conflict with ${rE(l5)} with " +
      s"the help of a ${rE(l6)} and her ${rE(l7)} culminating in ${rE(l8)}"
  }
  def rE(list: Seq[String]): String = list(r.nextInt(list.length))
}
