package org.codefirst.shimbashishelf.web

import java.util.Date

import unfiltered.request._
import unfiltered.response._
import unfiltered.scalate._

import org.codefirst.shimbashishelf.util.SCalendar
import org.codefirst.shimbashishelf.util.Base._
import org.codefirst.shimbashishelf.vcs.Commit
import org.codefirst.shimbashishelf.filesystem.{FileObject, FileSystem}


case class Day(day : Int, wday : String, files : Seq[FileObject], klass : String)

object Calendar {
  val wday = Map(
    java.util.Calendar.SUNDAY -> "Sun",
    java.util.Calendar.MONDAY -> "Mon",
    java.util.Calendar.TUESDAY -> "Tue",
    java.util.Calendar.WEDNESDAY -> "Wed",
    java.util.Calendar.THURSDAY -> "Thu",
    java.util.Calendar.FRIDAY -> "Fri",
    java.util.Calendar.SATURDAY-> "Sat")

  def today =
     SCalendar.today

  def navi(cal : SCalendar) =
    "year=%d&month=%d".format(cal.year, cal.month)

  def apply[A](req : HttpRequest[A]) = {
    val year  = getInt(req, "year" , today.year)
    val month = getInt(req, "month", today.month)

    val cal    = SCalendar(year, month, 1)
    val commits =
      FileSystem.commitList(Some(cal.startOfMonth.time),
                            Some(cal.endOfMonth.time))
    val days : Seq[Day] =
      (cal.startOfMonth to cal.endOfMonth).map(c => filesOfDay(c.time, commits)).toSeq

    val title =
      if (month == 0)
        (year - 1) + "/" + 12
      else
        year + "/" + month

    val prev = navi(cal.addMonth(-1))
    val next = navi(cal.addMonth(1))

    Ok ~> Scalate(req,
                  "calendar.scaml",
                  ("title", title),
                  ("days", days),
                  ("prev", prev),
                  ("next", next))
  }

  private def filesOfDay(day:Date, commits : Iterable[Commit[FileObject]]) = {
    val cal = java.util.Calendar.getInstance()
    cal.setTime(day)
    Day(cal.get(java.util.Calendar.DATE),
        dayName(cal).toLowerCase,
        commits.filter(c => dateEquals(day, c.date)).flatMap(_.files).toSet.toSeq,
        if(dateEquals(day, today.time)) "today" else "")
  }

  private def dayName(cal : java.util.Calendar) : String = {
    wday.getOrElse(cal.get(java.util.Calendar.DAY_OF_WEEK),"")
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
