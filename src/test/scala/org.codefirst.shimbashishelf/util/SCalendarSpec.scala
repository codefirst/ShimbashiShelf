package org.codefirst.shimbashishelf.util
import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers
import java.util.Calendar

class SCalendarSpec extends Spec with ShouldMatchers {
  describe("日付の情報取得") {
    val scalendar = SCalendar(2011,4,14)

    it("最初の日付の取得") {
      scalendar.startOfMonth.year should be  (2011)
      scalendar.startOfMonth.month should be (4)
      scalendar.startOfMonth.date should be  (1)
    }

    it("最後の日付の取得") {
      scalendar.endOfMonth.year  should be  (2011)
      scalendar.endOfMonth.month should be (4)
      scalendar.endOfMonth.date  should be  (30)
    }

    it("文字列化") {
      scalendar.toString should be("2011/4/14 0:0:0 0 AM")
    }

    it("今日") {
      SCalendar.today.hour   should be(0)
      SCalendar.today.minute should be(0)
      SCalendar.today.second should be(0)
    }
  }

  describe("イテレータ") {
    it("開始日・終了日指定") {
      val cal = SCalendar(2011,4, 1)
      val startDate = cal.startOfMonth
      val endDate   = cal.endOfMonth

      (startDate to endDate).length should be (30)
    }
  }
}

