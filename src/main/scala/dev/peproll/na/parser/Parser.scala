package dev.peproll.na.parser

import java.net.InetAddress
import java.time.LocalDateTime

import scala.util.{Success, Try}

trait Parser[T] {
  def apply(s: String): Either[String, T]
}

object Parser {
  import dev.peproll.na.DateTimeUtils._

  implicit val stringParser: Parser[String] = (s: String) => Right(s)
  implicit val instantParser: TryParser[LocalDateTime] = (s: String) =>
    LocalDateTime.parse(s, timeFormat)
  implicit val inetAddressParser: TryParser[InetAddress] = (s: String) => InetAddress.getByName(s)

  trait TryParser[T] extends Parser[T] {

    protected def parse(s: String): T

    def apply(s: String): Either[String, T] =
      Try(parse(s)).transform(s => Success(Right(s)), f => Success(Left(f.getMessage))).get
  }
}
