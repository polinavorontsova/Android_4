package com.ab.labs.planner.data.dao

import androidx.room.*
import com.ab.labs.planner.data.entity.User


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User): Long

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM user WHERE name = :name OR email = :email")
    suspend fun existUser(name: String, email: String): List<User>

    @Query("SELECT * FROM user WHERE name = :name AND password = :password")
    suspend fun getUser(name: String, password: String): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    suspend fun getUser(id: Long): List<User>

}
