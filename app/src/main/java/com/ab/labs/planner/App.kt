package com.ab.labs.planner

import android.app.Application
import com.ab.labs.planner.data.PlannerRoomDatabase
import com.ab.labs.planner.data.repository.EventRepository
import com.ab.labs.planner.data.repository.NoteRepository
import com.ab.labs.planner.data.repository.UserRepository

class App : Application() {

    companion object {
        lateinit var instance: App
    }

    val database by lazy { PlannerRoomDatabase.getDatabase(this) }
    val userRepository by lazy { UserRepository(database.userDao()) }
    val noteRepository by lazy { NoteRepository(database.noteDao()) }
    val eventRepository by lazy { EventRepository(database.eventDao()) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}