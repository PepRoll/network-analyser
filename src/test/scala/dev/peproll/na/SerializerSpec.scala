package dev.peproll.na

import java.net.InetAddress
import java.time.{LocalDateTime, Month}

import dev.peproll.na.Model.{SuspiciousActivity, SuspiciousEnter}
import org.scalatest.{FlatSpec, Matchers}

class SerializerSpec extends FlatSpec with Matchers {

  "SuspiciousEnter" should "serialize" in {
    val suspiciousEnter = SuspiciousEnter("name",
      LocalDateTime.of(2015, Month.NOVEMBER, 30, 23, 21, 55))
    suspiciousEnter.toString shouldBe "name:2015-11-30 23:21:55"
  }

  "SuspiciousActivity" should "serialize" in {
    val suspiciousActivity = SuspiciousActivity(
      InetAddress.getByName("127.0.0.1"),
      LocalDateTime.of(2019, Month.NOVEMBER, 30, 23, 21, 55),
      LocalDateTime.of(2019, Month.NOVEMBER, 30, 23, 22, 55),
      Seq(
        SuspiciousEnter("name1", LocalDateTime.of(2019, Month.NOVEMBER, 30, 23, 21, 55)),
        SuspiciousEnter("name2", LocalDateTime.of(2019, Month.NOVEMBER, 30, 23, 22, 55))
      )
    )

    suspiciousActivity.toString shouldBe "127.0.0.1,2019-11-30 23:21:55,2019-11-30 23:22:55," +
      "\"name1:2019-11-30 23:21:55,name2:2019-11-30 23:22:55\""

  }

}
