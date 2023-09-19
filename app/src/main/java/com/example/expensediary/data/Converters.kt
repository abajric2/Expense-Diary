package com.example.expensediary.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromDate(value: java.sql.Date?): Long? {
        return value?.time
    }

    @TypeConverter
    fun toDate(value: Long?): java.sql.Date? {
        return value?.let { java.sql.Date(it) }
    }
}

