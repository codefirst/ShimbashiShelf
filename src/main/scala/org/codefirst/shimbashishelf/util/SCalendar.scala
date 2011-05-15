package org.codefirst.shimbashishelf.util

import java.util.{Date, Calendar, TimeZone}


class SCalendar (date : Date) {

  def startDayOfMonth() : Date = { 
    val cal = Calendar.getInstance()
    cal.setTime(date)
    cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
    cal.set(Calendar.HOUR, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    cal.set(Calendar.AM_PM, Calendar.AM)
    cal.getTime()
  }

  def endDayOfMonth() : Date = { 
    val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))
    cal.setTime(date)
    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
    cal.set(Calendar.HOUR, 11)
    cal.set(Calendar.AM_PM, Calendar.PM)
    cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE))
    cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND))
    cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND))
    cal.getTime()
  }


  def getCalendar() : Calendar = { 
    val cal = Calendar.getInstance()
    cal.setTime(date)
    cal
  }

  override def toString() : String = { 
    val cal = getCalendar()
    cal.get(Calendar.YEAR).toString() + "/" + (cal.get(Calendar.MONTH) + 1).toString() + "/" + cal.get(Calendar.DATE).toString() + " " + 
    cal.get(Calendar.HOUR).toString() + ":" + (cal.get(Calendar.MINUTE)).toString() + ":" + cal.get(Calendar.SECOND).toString() + " " + 
    cal.get(Calendar.MILLISECOND).toString() + " " + (if (cal.get(Calendar.AM_PM) == Calendar.AM) {"AM"} else {"PM"})
  }

  def iterator(end : Date) = new Iterator[Date] { 
    val c = Calendar.getInstance()
    c.setTime(date)
    c.add(Calendar.DATE, -1)
    var current = c.getTime()
    def hasNext : Boolean = { 
      val cal = Calendar.getInstance()
      cal.setTime(current)
      cal.add(Calendar.DATE, 1)
      cal.add(Calendar.MILLISECOND, 1)
      cal.getTime().before(end)
    }
    def next : Date = { 
      val cal = Calendar.getInstance()
      cal.setTime(current)
      cal.add(Calendar.DATE, 1)
      current = cal.getTime()
      current
    }
  }
}

object SCalendar { 
  implicit def SCalendar2Calendar(scal : SCalendar) : Calendar = { 
    scal.getCalendar()
  }
  implicit def Calendar2SCalendar(cal : Calendar) : SCalendar = { 
    new SCalendar(cal.getTime())
  }
}
