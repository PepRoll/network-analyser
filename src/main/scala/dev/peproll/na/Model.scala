package dev.peproll.na

import java.net.InetAddress
import java.time.LocalDateTime

object Model {

  final case class LogEntry(userName: String, ip: InetAddress, enteredAt: LocalDateTime)

  final case class SuspiciousEnter(userName: String, enteredAt: LocalDateTime) {
    override def toString: String = s"$userName:${enteredAt.format(DateTimeUtils.timeFormat)}"
  }

  final case class SuspiciousActivity(
      ip: InetAddress,
      firstEnter: LocalDateTime,
      lastEnter: LocalDateTime,
      enters: Seq[SuspiciousEnter]) {
    override def toString: String =
      s"""${ip.getHostAddress},${firstEnter.format(DateTimeUtils.timeFormat)},${lastEnter.format(
        DateTimeUtils.timeFormat)},"${enters.mkString(",")}""""
  }

}
