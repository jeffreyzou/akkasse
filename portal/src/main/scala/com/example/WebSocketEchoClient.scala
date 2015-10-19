package com.example

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object WebSocketEchoClient {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()
    import system.dispatcher


    val c: WSClient = WSClient("http://localhost:9000/echo", "HAL1000")

    if (c.connectBlocking())
      c.spam("hello message")
  }
}
