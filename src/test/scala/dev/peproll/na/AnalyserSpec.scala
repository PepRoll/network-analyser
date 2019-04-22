package dev.peproll.na

import java.io.File
import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.testkit.TestKit
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import org.scalatest.{FlatSpecLike, Matchers}

import scala.io.Source

class AnalyserSpec extends TestKit(ActorSystem("TestActorSystem"))
  with FlatSpecLike
  with Matchers
  with ScalaFutures {

  implicit val materializer = ActorMaterializer()
  implicit val defaultPatience =
    PatienceConfig(timeout = Span(1, Seconds), interval = Span(500, Millis))

  "Log Analyser" should "analyse test.csv" in {
    val output = File.createTempFile("result", ".csv")

    val input = Paths.get(getClass.getResource("/test.csv").toURI)
    val result = LogAnalyser(input, output.toPath, 3600)

    whenReady(result) { res =>
      res.wasSuccessful shouldBe true
      val result = Source.fromFile(output).getLines()
      val expected = Source.fromURL(getClass.getResource("/expected.csv")).getLines
      (result zip expected).forall {
        case (r, e) => e == r
      } shouldBe true
      output.delete()
    }
  }

}
