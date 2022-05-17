package com.ab.labs.planner.ui.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.ab.labs.R
import com.ab.labs.planner.core.date.DateFormat
import com.ab.labs.planner.data.entity.Event
import java.util.*


class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val cellView: LinearLayout = view.findViewById(R.id.cell_view)
    private val dayView: TextView = view.findViewById(R.id.calendar_day)
    private val eventsView: TextView = view.findViewById(R.id.event_id)
    private val markView: ImageView = view.findViewById(R.id.mark_view)
    private lateinit var date: Date
    private lateinit var monthCalendar: Calendar

    fun bind(date: Date, monthCalendar: Calendar, events: List<Event>, clickDate: Date?) {
        this.date = date
        this.monthCalendar = monthCalendar

        showDay()
        showClickDay(clickDate)
        showEvents(events)
    }

    private fun showDay() {
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)

        val currentYear = monthCalendar.get(Calendar.YEAR)
        val currentMonth = monthCalendar.get(Calendar.MONTH) + 1

        var textColor = R.color.calendarMonthDayTextColor

        val backgroundColor = when {
            DateFormat.equalDatesByDay(date, Calendar.getInstance(Locale.ENGLISH).time) -> {
                R.color.calendarCurrentDayColor
            }
            month == currentMonth && year == currentYear -> {
                R.color.calendarMonthDayColor
            }
            else -> {
                textColor = R.color.calendarAnotherDayTextColor
                R.color.calendarAnotherDayColor
            }
        }
        cellView.setBackgroundColor(itemView.context.getColor(backgroundColor))

        dayView.text = day.toString()
        dayView.setTextColor(itemView.context.getColor(textColor))
    }

    private fun showClickDay(clickDate: Date?) {
        markView.isVisible = false
        clickDate?.let { d ->
            if (DateFormat.equalDatesByDay(date, d)) {
                markView.isVisible = true
            }
        }

    }

    private fun showEvents(events: List<Event>) {
        val dayEvents = mutableListOf<Event>()
        for (event in events) {
            val eventDate = Date(event.start)
            if (DateFormat.isDate(event.repeat, eventDate, date)) {
                dayEvents.add(event)
                eventsView.text = dayEvents.size.toString()
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup): CalendarViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.calendar_item, parent, false)
            return CalendarViewHolder(view)
        }
    }

}