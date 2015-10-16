package com.example

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{StatusCodes, HttpRequest}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.ActorMaterializer
import org.scalatest.{FlatSpec, Matchers, BeforeAndAfterAll}
import org.scalatest.concurrent.ScalaFutures

class AppSpec extends FlatSpec with Matchers
with ScalaFutures with BeforeAndAfterAll {

//  implicit val testSystem =
//    akka.actor.ActorSystem("test-system")
//  import testSystem.dispatcher
//  implicit val fm = ActorMaterializer()
//  val server =  Servers()
//
//  override def afterAll = testSystem.shutdown()
//
//  def sendRequest(req: HttpRequest) =
//    Source.single(req).via(
//      Http().outgoingConnection(host = "localhost",
//        port = 8080)
//    ).runWith(Sink.head)
//
//  "The app" should "return redirect to livedoc on a GET to /" in {
//    val request = sendRequest(HttpRequest())
//    whenReady(request) { response =>
//      val stringFuture = Unmarshal(response.entity).to[String]
//      whenReady(stringFuture) { str =>
//        str should include("Hello World!")
//      }
//    }
//  }
//  "The app" should "return 404 on a GET to /foo" in {
//    val request = sendRequest(HttpRequest(uri = "/foo"))
//    whenReady(request) { response =>
//      response.status shouldBe StatusCodes.NotFound
//    }
//  }
}