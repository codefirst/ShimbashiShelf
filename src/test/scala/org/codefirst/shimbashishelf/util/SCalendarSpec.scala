package org.codefirst.shimbashishelf.util
import org.scalatest.Spec
import org.scalatest.matchers.MustMatchers
import org.scalatest.matchers.ShouldMatchers
import java.util.Calendar
import org.codefirst.shimbashishelf.util.SCalendar

class SCalendarSpec extends Spec with ShouldMatchers {
  describe("2011/4/14") {
    it("get start date of month") {
      val cal = Calendar.getInstance()
      cal.set(Calendar.YEAR, 2011)
      cal.set(Calendar.MONTH, Calendar.APRIL)
      cal.set(Calendar.DATE, 14 )
      val scalendar = new SCalendar(cal.getTime())
      new SCalendar(scalendar.startDayOfMonth()).get(Calendar.DATE) should be (1)
    }
    it("get end date of month") {
      val cal = Calendar.getInstance()
      cal.set(Calendar.YEAR, 2011)
      cal.set(Calendar.MONTH, Calendar.APRIL)
      cal.set(Calendar.DATE, 14 )
      val scalendar = new SCalendar(cal.getTime())
      new SCalendar(scalendar.endDayOfMonth()).get(Calendar.DATE) should be (30)
    }
  }

  describe("2011/4/1 - 2011/4/30") { 
    it("iterate through a month") { 
      val cal = Calendar.getInstance()
      cal.set(Calendar.YEAR, 2011)
      cal.set(Calendar.MONTH, Calendar.APRIL)
      val startDate = new SCalendar(cal.getTime()).startDayOfMonth()
      val endDate = new SCalendar(cal.getTime()).endDayOfMonth()
      
      new SCalendar(startDate).iterator(endDate).foreach { date =>        
        println(new SCalendar(date))
      }
    }
  }
}

