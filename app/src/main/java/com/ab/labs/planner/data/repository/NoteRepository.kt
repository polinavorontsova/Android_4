package com.ab.labs.planner.data.repository

import androidx.annotation.WorkerThread
import com.ab.labs.planner.data.dao.NoteDao
import com.ab.labs.planner.data.entity.Note

class NoteRepository(private val noteDao: NoteDao) {

    @WorkerThread
    suspend fun insert(note: Note) = noteDao.insert(note)

    @WorkerThread
    suspend fun update(note: Note) {
        noteDao.update(note)
    }

    @WorkerThread
    suspend fun delete(note: Note) {
        noteDao.delete(note)
    }

    @WorkerThread
    suspend fun getNotes(userId: Long) = noteDao.getNotes(userId)

    @WorkerThread
    suspend fun getNote(id: Long) = noteDao.getNote(id)
}
