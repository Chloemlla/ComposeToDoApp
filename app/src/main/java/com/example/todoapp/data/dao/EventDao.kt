package com.example.todoapp.data.dao

import androidx.room.*
import com.example.todoapp.data.model.Event
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Query("SELECT * FROM events_table ORDER BY dateMillis ASC")
    fun getAllEvents(): Flow<List<Event>>

    @Query("SELECT * FROM events_table WHERE dateMillis > :now ORDER BY dateMillis ASC LIMIT :limit")
    fun getUpcomingEvents(now: Long = System.currentTimeMillis(), limit: Int = 1): Flow<List<Event>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event): Long

    @Update
    suspend fun updateEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)
}