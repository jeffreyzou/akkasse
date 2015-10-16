package com.example

import java.time.LocalTime
import java.time.format.DateTimeFormatter

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives
import akka.stream.Materializer
import akka.stream.scaladsl.{Flow, Source}
import org.slf4j.LoggerFactory
import akka.http.scaladsl.model.ws.{ Message, TextMessage }
import akka.stream.scaladsl.Flow

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

object EchoSource {
  def logger = LoggerFactory.getLogger(this.getClass)

  def route(system: ActorSystem)(implicit ec: ExecutionContext, mat: Materializer) = {
    import Directives._

    get {
      handleWebsocketMessages(echoService)
    }
  }

  val echoService: Flow[Message, Message, _] = Flow[Message].map {
    case TextMessage.Strict(txt) => TextMessage("ECHO: " + txt)
    case _ => TextMessage("Message type unsupported")
  }
}
