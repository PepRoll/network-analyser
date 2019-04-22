package dev.peproll.na

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {
  val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  implicit val order: Ordering[LocalDateTime] = Ordering.fromLessThan[LocalDateTime](_ isBefore _)
}
