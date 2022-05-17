package com.ab.labs.planner.ui.newevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.ab.labs.planner.App
import com.ab.labs.R
import com.ab.labs.planner.core.BaseFragment
import com.ab.labs.planner.core.date.DateFormat
import com.ab.labs.planner.core.extension.getViewModelExt
import com.ab.labs.planner.core.extension.hideSoftKeyboardExt
import com.ab.labs.planner.data.entity.Event
import com.ab.labs.databinding.FragmentNewEventBinding
import com.ab.labs.planner.ui.addnote.AddNoteFragmentArgs
import java.util.*

class NewEventFragment : BaseFragment() {

    private var _binding: FragmentNewEventBinding? = null
    private val binding get() = _binding!!

    private lateinit var newEventViewModel: NewEventViewModel
    private var userId: Long = -1
    private var start = false
    private var startDate: Date = Calendar.getInstance().time
    private var endDate: Date = Calendar.getInstance().time
    private var repeat = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewEventBinding.inflate(inflater, container, false)
        arguments?.let {
            val args = AddNoteFragmentArgs.fromBundle(it)
            val id = args.id
            userId = checkUserId()
            newEventViewModel = getViewModelExt {
                NewEventViewModel(
                    eventRepository = App.instance.eventRepository, id = id
                )
            }
            initObservers()
            initListeners()
            binding.onceRadio.isChecked = true
        }
        binding.timePicker.setIs24HourView(true)
        return binding.root
    }

    override fun initFab() {
        fab.setImageResource(R.drawable.ic_done_24)
        fab.show()
        fab.setOnClickListener {
            saveEvent()
        }
    }

    private fun initListeners() {
        binding.repeatBtn.setOnClickListener {
            hideSoftKeyboardExt()
            binding.repeatRadioGroup.isVisible = !binding.repeatRadioGroup.isVisible
        }

        binding.repeatRadioGroup.setOnCheckedChangeListener { _, view ->
            var label = R.string.repeat_once
            repeat = when (view) {
                R.id.onceRadio -> {
                    label = R.string.repeat_once
                    0
                }
                R.id.dailyRadio -> {
                    label = R.string.repeat_daily
                    1
                }
                R.id.weekdaysRadio -> {
                    label = R.string.repeat_on_weekdays
                    2
                }
                R.id.annualyRadio -> {
                    label = R.string.repeat_annualy
                    3
                }
                else -> 0
            }
            binding.repeatBtn.text = getString(R.string.repeat_btn) + ": " + getString(label)
        }

        binding.startBtn.setOnClickListener {
            hideSoftKeyboardExt()
            binding.repeatRadioGroup.isVisible = false
            binding.datePickerView.isVisible = true
            fab.hide()
            start = true

            val calendar = Calendar.getInstance()
            calendar.time = startDate
            binding.datePicker.updateDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        }

        binding.datePickerCancel.setOnClickListener {
            binding.datePickerView.isVisible = false
            fab.show()
            start = false
        }
        binding.datePickerOK.setOnClickListener {
            binding.datePickerView.isVisible = false
            binding.timePickerView.isVisible = true
            binding.timeTextView.text = getString(R.string.input_start_time)
            start = true
            val calendar = Calendar.getInstance()
            calendar.time = startDate
            binding.timePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
            binding.timePicker.minute = calendar.get(Calendar.MINUTE)
        }
        binding.timePickerCancel.setOnClickListener {
            binding.timePickerView.isVisible = false
            fab.show()
            start = false
        }
        binding.timePickerOK.setOnClickListener {
            if (start) {
                start = false
                binding.timeTextView.text = getString(R.string.input_end_time)
                val cal = Calendar.getInstance()
                cal.set(
                    binding.datePicker.year,
                    binding.datePicker.month,
                    binding.datePicker.dayOfMonth,
                    binding.timePicker.hour,
                    binding.timePicker.minute
                )
                startDate = cal.time

                val calendar = Calendar.getInstance()
                calendar.time = endDate
                binding.timePicker.hour = calendar.get(Calendar.HOUR_OF_DAY)
                binding.timePicker.minute = calendar.get(Calendar.MINUTE)
            } else {
                binding.timePickerView.isVisible = false
                fab.show()

                val cal = Calendar.getInstance()
                cal.set(
                    binding.datePicker.year,
                    binding.datePicker.month,
                    binding.datePicker.dayOfMonth,
                    binding.timePicker.hour,
                    binding.timePicker.minute
                )
                endDate = cal.time
                binding.startBtn.text = DateFormat.rangeDate(startDate, endDate)
            }
        }
    }

    private fun saveEvent() {
        val tittle = binding.editTittle.text.toString().trim()
        val description = binding.editDescription.text.toString().trim()
        when {
            tittle.isEmpty() -> {
                binding.editTittle.requestFocus()
                binding.editTittle.error = getString(R.string.tittle_field_error)
            }
            else -> {

                val event = Event(
                    userId = userId,
                    tittle = tittle,
                    start = startDate.time,
                    ending = endDate.time,
                    repeat = repeat,
                    description = description
                )
                newEventViewModel.save(event)
            }
        }
    }

    private fun initObservers() {
        newEventViewModel.savedEvent.observe(viewLifecycleOwner) {
            showInfoSnackbar(R.string.event_saved)
        }
        newEventViewModel.event.observe(viewLifecycleOwner) { e ->
            binding.editTittle.setText(e.tittle)
            binding.editDescription.setText(e.description)
            startDate = Date(e.start)
            endDate = Date(e.ending)
            initRepeat(e.repeat)
            binding.startBtn.text = DateFormat.rangeDate(startDate, endDate)
        }
    }

    private fun initRepeat(repeat: Int) {
        this.repeat = repeat
        when (repeat) {
            0 -> binding.onceRadio.isChecked = true
            1 -> binding.dailyRadio.isChecked = true
            2 -> binding.weekdaysRadio.isChecked = true
            3 -> binding.annualyRadio.isChecked = true
            else -> binding.onceRadio.isChecked = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}