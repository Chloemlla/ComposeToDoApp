package com.example.todoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events_table")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val note: String? = null,
    // the target date/time in epoch millis (UTC)
    val dateMillis: Long,
    val createdAt: Long = System.currentTimeMillis()
)