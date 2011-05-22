package org.codefirst.shimbashishelf.util
import java.util.{Date, Calendar, TimeZone}

class SCalendar (cal : Calendar) {
  private def dup : Calendar =
    cal.clone().asInstanceOf[Calendar]

  def startOfMonth : SCalendar = {
    val cal = dup
    cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DAY_OF_MONTH))
    cal.set(Calendar.HOUR, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    cal.set(Calendar.AM_PM, Calendar.AM)
    new SCalendar(cal)
  }

  def endOfMonth : SCalendar = {
    val cal = dup
    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
    cal.set(Calendar.HOUR, 11)
    cal.set(Calendar.AM_PM, Calendar.PM)
    cal.set(Calendar.MINUTE, cal.getActualMaximum(Calendar.MINUTE))
    cal.set(Calendar.SECOND, cal.getActualMaximum(Calendar.SECOND))
    cal.set(Calendar.MILLISECOND, cal.getActualMaximum(Calendar.MILLISECOND))
    new SCalendar(cal)
  }

  def addMonth(m : Int) = {
    val cal = dup
    cal.add(java.util.Calendar.MONTH, m)
    new SCalendar(cal)
  }

  def time = cal.getTime()
  def year  = cal.get(Calendar.YEAR)
  def month = cal.get(Calendar.MONTH) + 1
  def date  = cal.get(Calendar.DATE)
  def hour  = cal.get(Calendar.HOUR)
  def minute = cal.get(Calendar.MINUTE)
  def second =  cal.get(Calendar.SECOND)
  def millsecond = cal.get(Calendar.MILLISECOND)
  def am_pm =
    if (cal.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM"

  override def toString() : String =
    "%d/%d/%d %d:%d:%d %d %s".format(year,month,date,hour,minute,second,millsecond, am_pm)

  def to(end : SCalendar) = {
    val cal : Calendar = dup
    cal.add(Calendar.DATE, -1)

    new Iterator[SCalendar] {
      def current : SCalendar =
        new SCalendar(cal.clone().asInstanceOf[Calendar])

      def hasNext : Boolean = {
        val c = cal.clone().asInstanceOf[Calendar]
        c.add(Calendar.DATE, 1)
        c.add(Calendar.MILLISECOND, 1)
        c.getTime().before(end.time)
      }
      def next : SCalendar = {
        cal.add(Calendar.DATE, 1)
        current
      }
    }
  }
}

object SCalendar {
  implicit def Calendar2SCalendar(cal : Calendar) : SCalendar = {
    new SCalendar(cal)
  }

  private def now =
    Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"))

  def apply(year : Int, month : Int,  date : Int) : SCalendar = {
    val cal = now
    cal.set(Calendar.YEAR, year)
    cal.set(Calendar.MONTH, month -1)
    cal.set(Calendar.DATE, date )
    cal.set(Calendar.HOUR, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    cal.set(Calendar.AM_PM, Calendar.AM)
    new SCalendar(cal)
  }

  def today = {
    val cal = now
    cal.set(Calendar.HOUR, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    cal.set(Calendar.AM_PM, Calendar.AM)
    new SCalendar(cal)
  }
}
