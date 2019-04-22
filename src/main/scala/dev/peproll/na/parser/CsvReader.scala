package dev.peproll.na.parser

import akka.stream.scaladsl.Source
import com.github.tototoshi.csv.{CSVParser, DefaultCSVFormat}
import com.typesafe.scalalogging.Logger

class CsvReader[T: LineReader] {

  private val logger = Logger("CsvReader")

  private lazy val csvParser = new CSVParser(new DefaultCSVFormat() {})

  private def parseLine(line: String): Either[List[String], T] = {
    csvParser.parseLine(line) match {
      case Some(fields) => LineReader[T](fields)
      case None         => Left(List(s"Invalid line: $line"))
    }
  }

  def read[Mat](source: Source[String, Mat]): Source[T, Mat] =
    source
      .map(parseLine)
      .map(_.fold(errors => {
        errors.foreach(e => logger.warn(e))
        None
      }, Some(_)))
      .collect { case Some(t) => t }

}
