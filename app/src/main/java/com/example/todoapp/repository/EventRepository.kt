package com.example.todoapp.repository

import com.example.todoapp.data.dao.EventDao
import com.example.todoapp.data.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepository @Inject constructor(
    private val eventDao: EventDao
) {
    fun allEvents(): Flow<List<Event>> = eventDao.getAllEvents()
    fun upcomingEvent(limit: Int = 1) = eventDao.getUpcomingEvents(limit = limit)
    suspend fun addEvent(event: Event): Long = eventDao.insertEvent(event)
    suspend fun updateEvent(event: Event) = eventDao.updateEvent(event)
    suspend fun deleteEvent(event: Event) = eventDao.deleteEvent(event)
}