package com

import akka.event.Logging._
import akka.http.scaladsl.model.HttpRequest
import akka.http.scaladsl.server.directives.LogEntry

import scala.collection.immutable.Queue

package object example {


  def showRequest(request: HttpRequest) = LogEntry( s" ${request.uri} : request: ${request.isRequest()}", InfoLevel)
  def showRest(request: HttpRequest) = LogEntry(request.uri.toString() + " unmatched", WarningLevel)

  class FiniteQueue[A](q: Queue[A]) {
    def enqueueFinite[B >: A](elem: B, maxSize: Int): Queue[B] = {
      var ret = q.enqueue(elem)
      while (ret.size > maxSize) { ret = ret.dequeue._2 }
      ret
    }
  }
  implicit def queue2finitequeue[A](q: Queue[A]): FiniteQueue[A] = new FiniteQueue[A](q)

  def aan(o1: String): String = {
    o1.charAt(0) match {
      case 'a' | 'e' | 'o' | 'i' | 'u' => "an"
      case _ => "a"
    }
  }
}
