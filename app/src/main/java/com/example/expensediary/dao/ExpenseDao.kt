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
    @Query("""
        SELECT * FROM expenses 
        WHERE user_id = :user_id 
        AND expense_date = :expense_date
        """)
    suspend fun getUsersExpensesByDate(user_id: Long, expense_date: String): List<Expense>
    @Query("""
        SELECT SUM(price) 
        FROM expenses 
        WHERE expense_date = :expense_date 
        AND user_id = :user_id
        """)
    suspend fun getUsersDailySum(user_id: Long, expense_date: Date) : Int
    @Query("""
        SELECT SUM(price) 
        FROM expenses 
        WHERE strftime('%Y-%m', expense_date) = strftime('%Y-%m', :expense_date) 
        AND user_id = :user_id
        """)
    suspend fun getUsersMonthlySum(user_id: Long, expense_date: Date) : Int
}