package dev.peproll.na.parser

import java.net.InetAddress
import java.time.{LocalDateTime, Month}

import dev.peproll.na.Model.LogEntry
import org.scalatest._

class LineParserSpec extends FlatSpec with Matchers with EitherValues {

  "LineReader" should "read lines" in {
    val line = List("Bartle", "98.213.234.82", "2015-11-30 23:27:49")
    val result = LineReader[LogEntry](line)
    result.right.value should be(
      LogEntry("Bartle", InetAddress.getByName("98.213.234.82"),
        LocalDateTime.of(2015, Month.NOVEMBER, 30, 23, 27, 49)
      )
    )
  }

  "LineReader" should "fail on missing elements" in {
    val line = List("Bartle")
    val result = LineReader[LogEntry](line)
    result.left.value should be(List("Excepted list element."))
  }

  "LineReader" should "fail on surplus elements" in {
    val line = List("Bartle", "98.213.234.82", "2015-11-30 23:27:49", "baz")
    val result = LineReader[LogEntry](line)
    result.left.value should be(List("""Expected end, got "baz"."""))
  }

}
