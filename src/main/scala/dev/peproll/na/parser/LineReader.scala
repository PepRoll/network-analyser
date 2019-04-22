package dev.peproll.na.parser

import shapeless.{::, Generic, HList, HNil}

trait LineReader[Out] {
  def apply(l: List[String]): Either[List[String], Out]
}

object LineReader {

  implicit val hnilParser: LineReader[HNil] = {
    case Nil    => Right(HNil)
    case h +: t => Left(List(s"""Expected end, got "$h"."""))
  }

  implicit def hconsParser[H: Parser, T <: HList: LineReader]: LineReader[H :: T] =
    (s: List[String]) =>
      s match {
        case Nil => Left(List("Excepted list element."))
        case h +: t =>
          val head = implicitly[Parser[H]].apply(h)
          val tail = implicitly[LineReader[T]].apply(t)
          (head, tail) match {
            case (Left(error), Left(errors)) => Left(error :: errors)
            case (Left(error), Right(_))     => Left(error :: Nil)
            case (Right(_), Left(errors))    => Left(errors)
            case (Right(h), Right(t))        => Right(h :: t)
          }
      }

  implicit def caseClassParser[Out, R <: HList](implicit gen: Generic[Out] { type Repr = R }, reprParser: LineReader[R])
      : LineReader[Out] =
    (s: List[String]) => reprParser.apply(s).right.map(gen.from)

  def apply[Out](s: List[String])(implicit parser: LineReader[Out]): Either[List[String], Out] =
    parser(s)
}
