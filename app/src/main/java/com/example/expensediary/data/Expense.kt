package com.example.expensediary.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date


@Entity(tableName = "expenses",
        foreignKeys = [
            ForeignKey(
                entity = User::class,
                parentColumns = ["id"],
                childColumns = ["user_id"],
                onDelete = ForeignKey.CASCADE
            )
        ])
data class Expense(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L,
    @ColumnInfo(name = "user_id") var user_id: Long,
    @ColumnInfo(name = "item") var item: String,
    @ColumnInfo(name = "price") var price: Double,
    @ColumnInfo(name = "expense_date") var expenseDate: Date
)
