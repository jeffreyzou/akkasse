package com.example

import java.time.LocalTime
import java.time.format.DateTimeFormatter

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Directives
import akka.stream.Materializer
import akka.stream.scaladsl.Source
import de.heikoseeberger.akkasse.{EventStreamMarshalling, ServerSentEvent}
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

object TimeSource {
  def logger = LoggerFactory.getLogger(this.getClass)

  def timeRoute(system: ActorSystem)(implicit ec: ExecutionContext, mat: Materializer) = {
    import Directives._
    import EventStreamMarshalling._
    get {
      complete {
        Source.tick(1.seconds, 1.seconds, Unit)
          .map(_ => (this.hashCode(), LocalTime.now()))
          .map{ case (id,ts) => dateTimeToServerSentEvent(id,ts)}
          .keepAlive(1.second, () => ServerSentEvent.Heartbeat)
      }
    }
  }

  def dateTimeToServerSentEvent(id : Int, time: LocalTime): ServerSentEvent = {
    logger.info(s"Source id: ${id} Generating timestamp $time for SSE")
    ServerSentEvent(
      DateTimeFormatter.ISO_LOCAL_TIME.format(time)
    )
  }
}
