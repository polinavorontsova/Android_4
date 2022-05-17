package com.ab.labs.planner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ab.labs.planner.data.converter.DateLongConverter
import com.ab.labs.planner.data.dao.EventDao
import com.ab.labs.planner.data.dao.NoteDao
import com.ab.labs.planner.data.dao.UserDao
import com.ab.labs.planner.data.entity.Event
import com.ab.labs.planner.data.entity.Note
import com.ab.labs.planner.data.entity.User

@Database(entities = [User::class, Note::class, Event::class], version = 1, exportSchema = false)
@TypeConverters(DateLongConverter::class)
abstract class PlannerRoomDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun noteDao(): NoteDao
    abstract fun eventDao(): EventDao

    companion object {

        @Volatile
        private var INSTANCE: PlannerRoomDatabase? = null

        fun getDatabase(context: Context): PlannerRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        PlannerRoomDatabase::class.java,
                        "planner_database"
                    )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
