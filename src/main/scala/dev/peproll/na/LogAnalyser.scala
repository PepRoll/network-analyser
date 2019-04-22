package dev.peproll.na

import java.nio.file.Path
import java.time.ZoneOffset

import akka.actor.ActorSystem
import akka.stream.scaladsl.{FileIO, Flow, Framing, Keep}
import akka.stream.{ActorMaterializer, IOResult}
import akka.util.ByteString
import dev.peproll.na.Model.{LogEntry, SuspiciousActivity, SuspiciousEnter}
import dev.peproll.na.parser.CsvReader

import scala.concurrent.Future

object LogAnalyser {

  def apply(input: Path, output: Path, interval: Long)(
      implicit system: ActorSystem,
      materializer: ActorMaterializer): Future[IOResult] = {
    import DateTimeUtils._

    val sink = Flow[SuspiciousActivity]
      .map(s => ByteString(s.toString + "\n"))
      .toMat(FileIO.toPath(output))(Keep.right)

    csv
      .read(
        FileIO
          .fromPath(input)
          .via(Framing
            .delimiter(
              ByteString("\n"),
              maximumFrameLength = 256,
              allowTruncation = true
            )
            .map(_.utf8String)))
      .groupBy(Int.MaxValue, _.ip)
      .groupBy(
        Int.MaxValue,
        _.enteredAt.toEpochSecond(ZoneOffset.UTC) / interval
      )
      .fold(Seq.empty[LogEntry])(_ :+ _)
      .filter(_.size > 1)
      .map(_.sortBy(_.enteredAt))
      .map(toResult)
      .mergeSubstreams
      .mergeSubstreams
      .runWith(sink)

  }

  private lazy val csv = new CsvReader[LogEntry]

  private def toResult(logs: Seq[LogEntry]): SuspiciousActivity = {
    val ip = logs.head.ip
    val firstEnter = logs.head.enteredAt
    val lastEnter = logs.last.enteredAt
    val enters = logs.map(l => SuspiciousEnter(l.userName, l.enteredAt))

    SuspiciousActivity(ip, firstEnter, lastEnter, enters)
  }

}
