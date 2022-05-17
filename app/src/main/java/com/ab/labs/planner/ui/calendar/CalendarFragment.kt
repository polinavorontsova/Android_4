package com.ab.labs.planner.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ab.labs.planner.App
import com.ab.labs.R
import com.ab.labs.planner.core.BaseFragment
import com.ab.labs.planner.core.date.DateFormat
import com.ab.labs.planner.core.extension.getViewModelExt
import com.ab.labs.planner.core.extension.navigateExt
import com.ab.labs.planner.core.extension.showAlertDialogExt
import com.ab.labs.planner.data.entity.Event
import com.ab.labs.databinding.FragmentCalendarBinding
import java.util.*

class CalendarFragment : BaseFragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarViewModel: CalendarViewModel
    private var events: List<Event> = ArrayList()
    private val adapter = EventsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        calendarViewModel = getViewModelExt {
            CalendarViewModel(
                eventRepository = App.instance.eventRepository,
                userId = checkUserId()
            )
        }
        initListeners()
        initObservers()
        initEventRecycler()
        calendarViewModel.loadEvents()
        binding.dateView.text = DateFormat.prettyDateFormat.format(calendarViewModel.clickDate)
        binding.customCalendarView.clickDate = calendarViewModel.clickDate
        binding.customCalendarView.calendar = calendarViewModel.calendar
        return binding.root
    }

    override fun onStop() {
        super.onStop()
        calendarViewModel.calendar = binding.customCalendarView.calendar
    }

    override fun initFab() {
        fab.setImageResource(R.drawable.ic_add_24)
        fab.show()
        fab.setOnClickListener {
            navigateExt(CalendarFragmentDirections.actionNavCalendarToNavNewEvent())
        }
    }

    private fun initListeners() {
        binding.customCalendarView.onClickListener { date ->
            calendarViewModel.clickDate = date
            binding.dateView.text = DateFormat.prettyDateFormat.format(date)
            showDayEvents()
        }
    }

    private fun initObservers() {
        calendarViewModel.events.observe(viewLifecycleOwner) { events ->
            binding.customCalendarView.setEvents(events)
            this.events = events
            showDayEvents()
        }

        calendarViewModel.deletedEvent.observe(viewLifecycleOwner) {
            calendarViewModel.loadEvents()
        }
    }

    private fun showDayEvents() {
        val dayEvents = mutableListOf<Event>()
        for (event in events) {
            val eventDate = Date(event.start)
            if (DateFormat.isDate(event.repeat, eventDate, calendarViewModel.clickDate)) {
                dayEvents.add(event)
            }
        }
        if (dayEvents.isEmpty()) {
            binding.eventsView.visibility = View.GONE
            binding.emptyView.visibility = View.VISIBLE
        } else {
            binding.eventsView.visibility = View.VISIBLE
            binding.emptyView.visibility = View.GONE
        }
        adapter.submitList(dayEvents)
    }

    private fun initEventRecycler() {
        val recyclerView = binding.eventsView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = true
        recyclerView.adapter = adapter
        addDividers()

        adapter.shotClickListener = { event, _ ->
            navigateExt(CalendarFragmentDirections.actionNavCalendarToNavEvent(event.id))
        }
        adapter.editClickListener = { event ->
            navigateExt(CalendarFragmentDirections.actionNavCalendarToNavNewEvent(event.id))
        }
        adapter.deleteClickListener = { event ->
            showAlertDialogExt(R.string.dialog_delete) {
                calendarViewModel.delete(event)
            }
        }
    }

    private fun addDividers() {
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.eventsView.addItemDecoration(decoration)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}