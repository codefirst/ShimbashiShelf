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
import org.codefirst.shimbashishelf.vcs._
import org.codefirst.shimbashishelf.search._
import org.codefirst.shimbashishelf.util._
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import scala.collection.Iterator
import scala.collection.immutable.HashSet

class Calendar {
  private val filesRoot : String = "files" // TODO configure root directory

  def render(xhtml : NodeSeq) : NodeSeq = {
    val cal = java.util.Calendar.getInstance()
    val year  = Integer.parseInt(S.param("year").openOr(cal.get(java.util.Calendar.YEAR).toString()))
    val month  = Integer.parseInt(S.param("month").openOr((cal.get(java.util.Calendar.MONTH) + 1).toString()))
    cal.set(java.util.Calendar.YEAR, year)
    cal.set(java.util.Calendar.MONTH, month - 1)
    val scal = new SCalendar(cal.getTime())
    val commits = new VersionControl(new File(filesRoot)).commitList(Some(scal.startDayOfMonth()), Some(scal.endDayOfMonth()))
    val calendar = <table class="calendar">
    {
      for (date <- new SCalendar(scal.startDayOfMonth()).iterator(scal.endDayOfMonth()))
      yield {
        cal.setTime(date)
        filesOfDay(cal.getTime(), commits)
      }
    }
    </table>

    val monthTitle = {
      if (month == 0)
        (year - 1) + "/" + 12
      else
        year + "/" + month
    }

    // make navigator
    var url = "/calendar?year=" + cal.get(java.util.Calendar.YEAR) + "&month=" + cal.get(java.util.Calendar.MONTH)
    val prevMonth = <div class="prev-month">
      <a href={url}><span>{S.?("< prev")}</span></a>
    </div>
    cal.add(java.util.Calendar.MONTH, 2)
    url = "/calendar?year=" + cal.get(java.util.Calendar.YEAR) + "&month=" + cal.get(java.util.Calendar.MONTH)
    val nextMonth = <div class="next-month">
      <a href={url}><span>{S.?("next >")}</span></a>
    </div>

    bind("result", xhtml, "calendar" -> calendar, "prevMonth" -> prevMonth, "nextMonth" -> nextMonth, "monthTitle" -> monthTitle)
  }

  private def filesOfDay(day:Date, commits : List[FileDiffCommit]) = {
    val cal = java.util.Calendar.getInstance()
    cal.setTime(day)
    var files = HashSet[String]()
    for (commit <- commits if dateEquals(day, commit.getDate())) commit.getFiles().foreach { file => files = files + file }

    <tr class={getDayName(cal, true) + " " + cycle}>
      <td class="day">
        { cal.get(java.util.Calendar.DATE) }
      </td>
      <td class="dayname">
        ({ getDayName(cal) })
      </td>
      <td class="files">
        {
          for (file <- files) yield
            Searcher.searchByPath(new File(filesRoot + "/" + file).getAbsolutePath()) match {
              case Some(document) => <div><a title={ file } href={"/show?id=" + document.id}>{ file }</a></div>
              case None           => <div>{ file }</div>
            }
        }
      </td>
    </tr>
  }

  private def getDayName(cal : java.util.Calendar, isLower : Boolean = false) : String = {
    val ret = Map(java.util.Calendar.SUNDAY -> "Sun",
        java.util.Calendar.MONDAY -> "Mon",
        java.util.Calendar.TUESDAY -> "Tue",
        java.util.Calendar.WEDNESDAY -> "Wed",
        java.util.Calendar.THURSDAY -> "Thu",
        java.util.Calendar.FRIDAY -> "Fri",
        java.util.Calendar.SATURDAY-> "Sat").get(cal.get(java.util.Calendar.DAY_OF_WEEK)) match {
      case Some(x) => x
      case None    => ""
    }
    if (isLower) ret.toLowerCase
    else ret
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

  private var cycleCounter : Int = 1
  private def cycle : String = {
    cycleCounter += 1
    if (cycleCounter % 2 == 1) "odd"
    else "even"
  }
}

