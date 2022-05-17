package com.ab.labs.planner.ui.newevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ab.labs.planner.core.viewmodel.SingleLiveEvent
import com.ab.labs.planner.data.entity.Event
import com.ab.labs.planner.data.repository.EventRepository
import kotlinx.coroutines.launch

class NewEventViewModel(
    private val eventRepository: EventRepository,
    private var id: Long
) : ViewModel() {

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    private val _savedEvent = SingleLiveEvent<Boolean>()
    val savedEvent: LiveData<Boolean> = _savedEvent

    init {
        if (id > 0) {
            viewModelScope.launch {
                val events = eventRepository.getEvent(id)
                if (events.isNotEmpty()) {
                    _event.value = events[0]
                }
            }
        }
    }

    fun save(event: Event) {
        viewModelScope.launch {
            if (id > 0) {
                event.id = id
                eventRepository.update(event)
            } else {
                id = eventRepository.insert(event)
                event.id = id
            }
            _event.value = event
            _savedEvent.value = true
        }
    }

}