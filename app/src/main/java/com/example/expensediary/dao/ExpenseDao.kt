package com.example.expensediary.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.expensediary.data.Expense
import com.example.expensediary.data.User
import java.sql.Date

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses")
    suspend fun getAll(): List<Expense>
    @Insert
    suspend fun insertAll(vararg expenses: Expense)
    @Delete
    suspend fun delete(expense: Expense)
    @Query("SELECT * FROM expenses WHERE user_id = :user_id AND expense_date = :expense_date")
    suspend fun getUsersExpensesByDate(user_id: Int, expense_date: Date): List<Expense>
}