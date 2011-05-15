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
import org.codefirst.shimbashishelf.util._
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.Iterator
import scala.collection.immutable.HashSet

class Calendar {

  def render(xhtml : NodeSeq) : NodeSeq = {
    val cal = java.util.Calendar.getInstance()
    val year  = Integer.parseInt(S.param("year").openOr(cal.get(java.util.Calendar.YEAR).toString()))
    val month  = Integer.parseInt(S.param("month").openOr((cal.get(java.util.Calendar.MONTH) + 1).toString()))
    cal.set(java.util.Calendar.YEAR, year)
    cal.set(java.util.Calendar.MONTH, month - 1)
    val scal = new SCalendar(cal.getTime())
    val commits = new VersionControl(new File("files")).commitList(Some(scal.startDayOfMonth()), Some(scal.endDayOfMonth()))
    val calendar = <div class="calendar">
    { 
      for (date <- new SCalendar(scal.startDayOfMonth()).iterator(scal.endDayOfMonth()))
      yield <div class="date"> { 
        cal.setTime(date) 
        filesOfDay(cal.getTime(), commits)
      } </div> 
    }
    </div>

    val monthTitle = <div class="month-title">{ 
      if (month == 0) {
        (year - 1) + "/" + 12
      } else { 
        year + "/" + month
      }
    }</div>

    val prevMonth = <div class="prev-month">
      <a href={"/calendar?year=" + cal.get(java.util.Calendar.YEAR) + "&month=" + cal.get(java.util.Calendar.MONTH)}>&lt;&lt; prev</a>
    </div>
    cal.add(java.util.Calendar.MONTH, 2)
    val nextMonth = <div class="next-month">
      <a href={"/calendar?year=" + cal.get(java.util.Calendar.YEAR) + "&month=" + cal.get(java.util.Calendar.MONTH)}>next &gt;&gt;</a>
    </div>

    bind("result", xhtml, "calendar" -> calendar, "prevMonth" -> prevMonth, "nextMonth" -> nextMonth, "monthTitle" -> monthTitle)
  }

  private def filesOfDay(day:Date, commits : List[FileDiffCommit]) = { 
    val week_day_map = Map(java.util.Calendar.SUNDAY -> "Sun", 
                           java.util.Calendar.MONDAY -> "Mon",
                           java.util.Calendar.TUESDAY -> "Tue",
                           java.util.Calendar.WEDNESDAY -> "Wed",
                           java.util.Calendar.THURSDAY -> "Thu",
                           java.util.Calendar.FRIDAY -> "Fri",
                           java.util.Calendar.SATURDAY-> "Sat")
    val cal = java.util.Calendar.getInstance()
    cal.setTime(day)
    var files = HashSet[String]()
    for (commit <- commits if dateEquals(day, commit.getDate())) commit.getFiles().foreach { file => files = files + file } 
    <div class={week_day_map.get(cal.get(java.util.Calendar.DAY_OF_WEEK)) match {  
      case Some(x) => x 
      case None    => ""}}>
      <div class="day">
        { cal.get(java.util.Calendar.DATE) }
        ({ week_day_map.get(cal.get(java.util.Calendar.DAY_OF_WEEK)) match { 
          case Some(x) => x
          case None => ""} })
      </div>
      <div class="files">
        {
          for (file <- files) yield 
            Searcher.searchByPath(new File("files/" + file).getAbsolutePath()) match { // TODO configure root directory
              case Some(document) => <div><a href={"/show?id=" + document.id}>{ file }</a></div>
              case None           => <div>{ file }</div>

            }
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

