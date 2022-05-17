package com.ab.labs.planner.data.dao

import androidx.room.*
import com.ab.labs.planner.data.entity.Note


@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM note WHERE user_id = :userId")
    suspend fun getNotes(userId: Long): List<Note>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun getNote(id: Long): List<Note>

}
