package com.ab.labs.planner.core.date

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateFormat {

    val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    val dateFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
    val monthFormat: SimpleDateFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
    val yearFormat: SimpleDateFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
    val timeFormat: SimpleDateFormat = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    val prettyDateFormat: SimpleDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
    val dateTimeFormat: SimpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)

    fun isDate(repeat: Int, eventDate: Date, clickDate: Date) =
        isOnce(eventDate, clickDate)
                || isDaily(repeat, eventDate, clickDate)
                || isWeakly(repeat, eventDate, clickDate)
                || isAnnually(repeat, eventDate, clickDate)

    fun isOnce(eventDate: Date, clickDate: Date) =
        equalDatesByDay(eventDate, clickDate)

    fun isDaily(repeat: Int, eventDate: Date, clickDate: Date) =
        repeat == 1 && eventDate.before(clickDate)

    fun isWeakly(repeat: Int, eventDate: Date, clickDate: Date) =
        repeat == 2 && eventDate.before(clickDate) && equalDatesByWeakDay(
            eventDate,
            clickDate
        )

    fun isAnnually(repeat: Int, eventDate: Date, clickDate: Date) =
        repeat == 3 && eventDate.before(clickDate) && equalDatesByYear(
            eventDate,
            clickDate
        )

    fun equalDatesByDay(date1: Date, date2: Date): Boolean {
        val c1: Calendar = Calendar.getInstance()
        c1.time = date1
        val c2: Calendar = Calendar.getInstance()
        c2.time = date2
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
                c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
    }

    fun equalDatesByWeakDay(date1: Date, date2: Date): Boolean {
        val c1: Calendar = Calendar.getInstance()
        c1.time = date1
        val c2: Calendar = Calendar.getInstance()
        c2.time = date2
        return c1.get(Calendar.DAY_OF_WEEK) == c2.get(Calendar.DAY_OF_WEEK)
    }

    fun equalDatesByYear(date1: Date, date2: Date): Boolean {
        val c1: Calendar = Calendar.getInstance()
        c1.time = date1
        val c2: Calendar = Calendar.getInstance()
        c2.time = date2
        return c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
                c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)
    }

    fun rangeDate(start: Date, ending: Date) =
        dateTimeFormat.format(start) + " - " + timeFormat.format(ending)

    fun rangeTime(start: Date, ending: Date) =
        timeFormat.format(start) + " - " + timeFormat.format(ending)

    fun toDate(dateInString: String): Date? {
        var date: Date? = null
        try {
            date = dateFormat.parse(dateInString)
        } catch (e: ParseException) {
            //e.printStackTrace()
        }
        return date
    }

}

