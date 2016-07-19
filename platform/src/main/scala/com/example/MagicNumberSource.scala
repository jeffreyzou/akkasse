package com.example

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives
import akka.stream.scaladsl.Source
import akka.stream.{Materializer, OverflowStrategy}
import de.heikoseeberger.akkasse.{EventStreamMarshalling, ServerSentEvent}
import org.joda.time.DateTime
import org.json4s._
import org.json4s.native.JsonMethods._
import org.json4s.native.Serialization.write

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.Random

case class MagicNumberSource( evPerSeconds : Int ) {

  implicit val formats = org.json4s.DefaultFormats ++ org.json4s.ext.JodaTimeSerializers.all
  val frequency = 1/evPerSeconds.toDouble seconds

  def magicRoute(system: ActorSystem)(implicit ec: ExecutionContext, mat: Materializer) = {
    import Directives._
    import EventStreamMarshalling._

    val r = new Random()

    get {
      complete {
        Source.tick(1.seconds, frequency, Unit)
          .map(_ => r.nextLong)
          .map(magicServerSentEvent).buffer(10,OverflowStrategy.dropTail)
          .keepAlive(frequency*10, () => ServerSentEvent.Heartbeat)
      }
    }
  }

  def magicServerSentEvent(magic: Long): ServerSentEvent = ServerSentEvent(
    compact(render(parse(write(MagicEvent(magic,DateTime.now())))))
  )
}

case class MagicEvent( magic : Long, timestamp : DateTime )
