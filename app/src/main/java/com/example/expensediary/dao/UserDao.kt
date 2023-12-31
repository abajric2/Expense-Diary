package com.example.expensediary.dao

import androidx.room.*
import com.example.expensediary.data.User

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<User>
    @Insert
    suspend fun insertAll(vararg users: User)
    @Update
    suspend fun updateAll(vararg users: User)
    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getById(id: Long): List<User>
    @Delete
    suspend fun delete(user: User)
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun searchByUsername(username: String): List<User>
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun searchByUsernameAndPassword(username: String, password: String): List<User>
}