package com.example.expensediary.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    @ColumnInfo(name = "first_name") var firstName: String,
    @ColumnInfo(name = "last_name") var lastName: String,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "password") var password: String,
    @ColumnInfo(name = "daily_limit") var dailyLimit: Int,
    @ColumnInfo(name = "monthly_limit") var monthlyLimit: Int,
    @ColumnInfo(name = "currency") var currency: String
    )