package modelergy

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class Sseload extends Simulation {
val httpConf = http
  .baseURL("https://api-test.modeler.gy")

val scn = scenario("ServerSentEvent")
  .exec( sse("StreamingLogEvents")
    .open("/log/sse/1")
    .check( wsAwait.within(200 seconds).until(100) )
    )
  .exec(sse("CloseSSE").close())

  setUp(
      scn.inject(atOnceUsers(10))
    ).protocols(httpConf)

}
