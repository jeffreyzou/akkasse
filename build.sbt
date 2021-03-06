import spray.revolver.RevolverPlugin._

lazy val root = (project in file(".")).
  aggregate(platform, portal)
  .enablePlugins(GitVersioning)

lazy val commonSettings = Seq(
  organization := "com.example",
  version := "0.1",
  scalaVersion := "2.11.8"
)

lazy val coreDeps = List(
  "com.typesafe.akka" %% "akka-actor" % "2.4.8",
  "com.typesafe.akka" %% "akka-testkit" % "2.4.8",
  "com.typesafe.akka" %% "akka-remote"                   % "2.4.8",
  "com.typesafe.akka" %% "akka-cluster"                  % "2.4.8",
  "com.typesafe.akka" %% "akka-cluster-tools"            % "2.4.8",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.8",
  "com.typesafe.akka" %% "akka-stream-experimental" % "2.0-M1",
  "com.typesafe.akka" %% "akka-http-core-experimental" % "2.0-M1",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.0-M1",
  "joda-time"         %  "joda-time" % "2.8.2",
  "org.json4s"        %% "json4s-native"                 % "3.2.11",
  "org.json4s"        %% "json4s-ext"                 % "3.2.11",
  "org.scalatest"     %% "scalatest" % "2.2.5" % "test",
  "ch.qos.logback" 	  %  "logback-classic" % "1.1.2",
  "de.heikoseeberger" %% "akka-sse" % "1.8.1"
)

lazy val platform = (project in file("platform")).
  settings(commonSettings: _*).
  settings(
    name := "platform-akka-sse-stream",
    libraryDependencies ++= coreDeps,
    Revolver.settings
  )

lazy val portal = (project in file("portal")).
  settings(commonSettings: _*).
  settings(
    name := "portal-play-sse-client",
    libraryDependencies ++= coreDeps ++ List(
      "org.java-websocket" % "Java-WebSocket" % "1.3.0"
    )
  )
