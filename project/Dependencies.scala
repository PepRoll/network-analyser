import sbt._

object Dependencies {
  
  val akkaVersion = "2.5.22"
  val scoptVersion = "3.7.1"
  val shapelessVersion = "2.3.3"
  val scalaCsvVersion = "1.3.5"
  val logbackVersion = "1.2.3"
  val scalaLoggingVersion = "3.9.2"
  val scalaTestVersion = "3.0.5"
  
  
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
  val scopt = "com.github.scopt" %% "scopt" % scoptVersion
  val shapeless = "com.chuusai" %% "shapeless" % shapelessVersion
  val scalaCsv = "com.github.tototoshi" %% "scala-csv" % scalaCsvVersion
  val logback = "ch.qos.logback" % "logback-classic" % logbackVersion
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % Test
  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test
  
  val dependencies = Seq(
    akkaStream,
    scopt,
    shapeless,
    scalaCsv,
    logback,
    scalaLogging,
    scalaTest,
    akkaTestKit
  )
}
