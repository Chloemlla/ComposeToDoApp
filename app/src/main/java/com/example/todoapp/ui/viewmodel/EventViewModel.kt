package com.example.todoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.Event
import com.example.todoapp.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val eventRepository: EventRepository
) : ViewModel() {

    val events: StateFlow<List<Event>> =
        eventRepository.allEvents()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    // convenience flow: next upcoming
    val nextEvent = eventRepository.upcomingEvent(limit = 1)
        .map { it.firstOrNull() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun addEvent(title: String, dateMillis: Long) {
        viewModelScope.launch {
            eventRepository.addEvent(Event(title = title, dateMillis = dateMillis))
        }
    }

    fun updateEvent(event: Event) {
        viewModelScope.launch { eventRepository.updateEvent(event) }
    }

    fun deleteEvent(event: Event) {
        viewModelScope.launch { eventRepository.deleteEvent(event) }
    }

    companion object {
        fun daysUntil(targetMillis: Long): Long {
            val now = System.currentTimeMillis()
            val diff = targetMillis - now
            return if (diff <= 0L) 0L else TimeUnit.MILLISECONDS.toDays(diff) + 1 // inclusive so event tomorrow => 1
        }
    }
}