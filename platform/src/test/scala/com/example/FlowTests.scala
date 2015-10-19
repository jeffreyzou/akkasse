package com.example

import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, Matchers, FlatSpec}
import scala.concurrent.{Await, Future}
import akka.actor._
import akka.stream._
import akka.stream.scaladsl._
import scala.concurrent.duration._

class FlowTests extends BaseActorTest {
  implicit val ec = system.dispatcher

  "an actor" should "be useable as a flow source" in {
    val b = Future {
      Thread.sleep(1000); "complete"
    }

    def run(actor: ActorRef): Unit = {
      Future {
        Thread.sleep(100); actor ! 1
      }
      Future {
        Thread.sleep(200); actor ! 2
      }
      Future {
        Thread.sleep(300); actor ! 3
      }
    }

    val source = Source
                    .actorRef[Int](0, OverflowStrategy.fail)
                    .mapMaterializedValue(ref ⇒ run(ref))

    implicit val m = ActorMaterializer()


    source runForeach { int ⇒
      info(s"received: $int")
    }

    Await.ready(b, 5 seconds)

  }
}
