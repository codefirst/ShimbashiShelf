package org.codefirst.shimbashishelf.web.snippet

import _root_.scala.xml.{NodeSeq, Text}
import _root_.net.liftweb.http._
import _root_.net.liftweb.http.RequestVar
import _root_.net.liftweb.http.SHtml._
import _root_.net.liftweb.util._
import _root_.net.liftweb.common._
import net.liftweb.http._
import Helpers._

import org.codefirst.shimbashishelf._
import org.codefirst.shimbashishelf.search._
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.Iterator

class Calendar {

  def render(xhtml : NodeSeq) : NodeSeq = {
    val cal = java.util.Calendar.getInstance()
    val year  = Integer.parseInt(S.param("year").openOr(cal.get(java.util.Calendar.YEAR).toString()))
    val month  = Integer.parseInt(S.param("month").openOr((cal.get(java.util.Calendar.MONTH) + 1).toString()))
    cal.set(java.util.Calendar.YEAR, year)
    cal.set(java.util.Calendar.MONTH, month - 1)
    val startDate = java.util.Calendar.getInstance()
    startDate.setTime(startDayOfMonth(cal.getTime()))
    val endDate = java.util.Calendar.getInstance()
    endDate.setTime(endDayOfMonth(cal.getTime()))
    val calendar = <div class="calendar">
        { 
          for (i <- 1 to endDate.get(java.util.Calendar.DATE)) 
          yield <div class="date"> { 
            cal.set(java.util.Calendar.DATE, i); 
            filesOfDay(cal.getTime(), new VersionControl(new File("files")).commitList(Some(startDate.getTime()), Some(endDate.getTime())))
          } </div> 
        }
      </div>

    bind("result", xhtml, "calendar" -> calendar)
  }

  private def startDayOfMonth(date : Date) : Date = { 
    val cal = java.util.Calendar.getInstance()
    cal.setTime(date)
    cal.set(java.util.Calendar.DATE, 1)
    cal.set(java.util.Calendar.HOUR, 0)
    cal.set(java.util.Calendar.MINUTE, 0)
    cal.set(java.util.Calendar.SECOND, 0)
    cal.set(java.util.Calendar.MILLISECOND, 0)
    cal.getTime()
  }

  private def endDayOfMonth(date : Date) : Date = { 
    val cal = java.util.Calendar.getInstance()
    cal.setTime(date)
    cal.set(java.util.Calendar.MONTH, cal.get(java.util.Calendar.MONTH) + 1)
    cal.set(java.util.Calendar.DATE, 1)
    cal.set(java.util.Calendar.HOUR, 0)
    cal.set(java.util.Calendar.MINUTE, 0)
    cal.set(java.util.Calendar.SECOND, 0)
    cal.set(java.util.Calendar.MILLISECOND, 0)
    cal.add(java.util.Calendar.MINUTE, -1)
    cal.getTime()
  }

  private def filesOfDay(day:Date, commits : List[FileDiffCommit]) = { 
    val week_day_map = Map(1 -> "Sun", 2 -> "Mon", 3 -> "Tue", 4 -> "Wed", 5 -> "Thu", 6 -> "Fri", 7 -> "Sat")
    val cal = java.util.Calendar.getInstance()
    cal.setTime(day)
    <div class="date">
      <div>{ cal.get(java.util.Calendar.DATE) } ({ week_day_map.get(cal.get(java.util.Calendar.DAY_OF_WEEK)) match { case Some(x) => x} })</div>
      <div>
        {
          for (commit <- commits if dateEquals(day, commit.getDate())) 
          yield <div> { commit.getFiles().map { file => <div> { file } </div>} } </div>
        }
      </div>
    </div>
  }

  private def dateEquals(day1 : Date, day2 : Date) : Boolean = { 
    val cal1 = java.util.Calendar.getInstance()
    val cal2 = java.util.Calendar.getInstance()
    cal1.setTime(day1)
    cal2.setTime(day2)
    cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
    cal1.get(java.util.Calendar.MONTH) == cal2.get(java.util.Calendar.MONTH) &&
    cal1.get(java.util.Calendar.DATE) == cal2.get(java.util.Calendar.DATE) 
  }
}

