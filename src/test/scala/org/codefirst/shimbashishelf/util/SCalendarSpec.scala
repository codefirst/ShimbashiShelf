package org.codefirst.shimbashishelf.util
import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers
import java.util.Calendar

class SCalendarSpec extends Spec with ShouldMatchers {
  describe("日付の情報取得") {
    val scalendar = SCalendar(2011,4,14)

    it("最初の日付の取得") {
      new SCalendar(scalendar.startDayOfMonth()).get(Calendar.DATE) should be (1)
    }

    it("最後の日付の取得") {
      new SCalendar(scalendar.endDayOfMonth()).get(Calendar.DATE) should be (30)
    }

    it("文字列化") {
      scalendar.toString should be("2011/4/14 0:0:0 0 AM")
    }
  }

  describe("イテレータ") {
    it("開始日・終了日指定") {
      val cal = SCalendar(2011,4, 1)
      val startDate = new SCalendar(cal.getTime()).startDayOfMonth()
      val endDate = new SCalendar(cal.getTime()).endDayOfMonth()

      new SCalendar(startDate).iterator(endDate).length should be (30)
    }
  }
}

