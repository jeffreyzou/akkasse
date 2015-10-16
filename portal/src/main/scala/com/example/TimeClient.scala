package com.example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import de.heikoseeberger.akkasse.{ EventStreamUnmarshalling, ServerSentEvent }
import java.time.LocalTime

object TimeClient {
  import EventStreamUnmarshalling._

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()
    import system.dispatcher

    Source.single(HttpRequest(uri = "/time"))
      .via(Http().outgoingConnection("127.0.0.1", 9000))
      .mapAsync(1)(Unmarshal(_).to[Source[ServerSentEvent, Any]])
      .runForeach(_.runForeach(event => println(s"${LocalTime.now()} $event")))
  }
}