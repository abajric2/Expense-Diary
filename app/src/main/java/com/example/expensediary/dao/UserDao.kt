package com.example.expensediary.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.expensediary.data.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<User>
    @Insert
    suspend fun insertAll(vararg users: User)
    @Delete
    suspend fun delete(user: User)
}