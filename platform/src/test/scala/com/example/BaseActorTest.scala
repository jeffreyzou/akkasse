package com.example
import scala.util.Random

import org.scalatest.{FlatSpecLike, BeforeAndAfterAll, WordSpecLike, Matchers}

import com.typesafe.config.ConfigFactory

import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.{ TestActors, DefaultTimeout, ImplicitSender, TestKit }
import scala.concurrent.duration._
import scala.collection.immutable

abstract class BaseActorTest extends TestKit(ActorSystem("TestSystem",ConfigFactory.parseString(SimpleAkkaConfig.config)))
with DefaultTimeout with ImplicitSender
with FlatSpecLike with Matchers with BeforeAndAfterAll {

  override def afterAll {
    shutdown()
  }
//  implicit protected val system = ActorSystem("streaming-system",ConfigFactory.parseString(SimpleAkkaConfig.config))
//
//  override protected def afterAll() = {
//    Await.ready(system.terminate(), Duration.Inf)
//    super.afterAll()
//  }
}

object SimpleAkkaConfig {
  val config =
    """
  | akka {
  | akka.loggers = ["akka.testkit.TestEventListener","akka.event.slf4j.Slf4jLogger"]
  |   log-dead-letters = ON
  |   loglevel = "ERROR"
  |     actor {
  |       debug {
  |         receive = on
  |         autoreceive = on
  |         lifecycle = on
  |         fsm = on
  |     }
  |   }
  | }
""".stripMargin
}