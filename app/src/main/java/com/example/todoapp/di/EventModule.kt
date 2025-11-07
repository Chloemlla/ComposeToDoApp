package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.data.dao.EventDao
import com.example.todoapp.data.db.EventsDatabase
import com.example.todoapp.repository.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EventModule {

    @Provides
    @Singleton
    fun provideEventsDatabase(@ApplicationContext context: Context): EventsDatabase {
        return EventsDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideEventDao(database: EventsDatabase): EventDao {
        return database.eventDao()
    }

    @Provides
    @Singleton
    fun provideEventRepository(eventDao: EventDao): EventRepository {
        return EventRepository(eventDao)
    }
}