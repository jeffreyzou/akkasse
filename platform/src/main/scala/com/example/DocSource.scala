package com.example

import java.time.LocalTime
import java.time.format.DateTimeFormatter

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.directives.LogEntry
import akka.stream.Materializer

import scala.concurrent.ExecutionContext
import akka.event.Logging._

import scala.concurrent.duration.Duration

object DocSource {

  def route(system: ActorSystem)(implicit ec: ExecutionContext, mat: Materializer) = {
    import Directives._

    get {
      logRequest(showRequest _) {
        pathEndOrSingleSlash {
            getFromFile("./src/main/webapp/doc/apidoc.html")
        } ~
        pathPrefix("livedoc"){
          getFromDirectory("./src/main/webapp/doc/livedoc")
        } ~
          pathPrefix("style") {
            getFromDirectory("./src/main/webapp/doc/livedoc/style")
          } ~
          pathPrefix("style/images") {
            getFromDirectory("./src/main/webapp/doc/livedoc/style/images")
          } ~
          pathPrefix("classpath") {
            getFromDirectory("./src/main/webapp/doc/livedoc/classpath")
          } ~
          pathPrefix("css") {
            getFromDirectory("./src/main/webapp/doc/livedoc/css")
          } ~
          pathPrefix("js") {
            getFromDirectory("./src/main/webapp/doc/livedoc/js/")
          } ~
          pathPrefix("img") {
            getFromDirectory("./src/main/webapp/doc/livedoc/img")
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
  }
}
