package com.example

import akka.actor.{ ActorRef, ActorSystem }
import akka.http.scaladsl.Http
import akka.stream.{ ActorMaterializer }
import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.unmarshalling.FromRequestUnmarshaller
import akka.http.scaladsl.model.StatusCodes.{Found,MovedPermanently}
import akka.http.scaladsl.coding.Deflate
import akka.http.scaladsl.server.Directives._


object Server {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem()
    implicit val mat = ActorMaterializer()
    import system.dispatcher

    val route = {
      path( Slash.? ) {
       redirect("livedoc",Found)
      } ~
      pathPrefix("livedoc"){
        DocSource.route(system)
      } ~
      path("time") {
        TimeSource.timeRoute(system)
      } ~
      path("magic"){
        MagicNumberSource(4).magicRoute(system)
      }
    }

    Http().bindAndHandle(route, "127.0.0.1", 9000)
  }

}