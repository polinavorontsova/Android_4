package com.ab.labs.planner.data.dao

import androidx.room.*
import com.ab.labs.planner.data.entity.Event


@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(event: Event): Long

    @Update
    suspend fun update(event: Event)

    @Delete
    suspend fun delete(event: Event)

    @Query("SELECT * FROM event WHERE user_id = :userId ORDER BY start")
    suspend fun getEvents(userId: Long): List<Event>
    
    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun getEvent(id: Long): List<Event>

}
