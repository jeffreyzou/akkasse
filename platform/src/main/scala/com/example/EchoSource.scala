package com.example

import akka.actor.ActorSystem
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives
import akka.stream.Materializer
import akka.stream.scaladsl.Flow
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext

object EchoSource {
  def logger = LoggerFactory.getLogger(this.getClass)

  def route(system: ActorSystem)(implicit ec: ExecutionContext, mat: Materializer) = {
    import Directives._

    get {
      akka.http.scaladsl.server.Directives.handleWebSocketMessages(echoService)
    }
  }

  val echoService: Flow[Message, Message, _] = Flow[Message].map {
    case TextMessage.Strict(txt) => {
      logger.info(s"Echo $txt to client")
      TextMessage("ECHO: " + txt)
    }
    case _ => TextMessage("Message type unsupported")
  }
}
