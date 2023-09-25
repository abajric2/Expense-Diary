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
    @Query("SELECT * FROM expenses WHERE user_id = :user_id")
    suspend fun getByUsersId(user_id: Long): List<Expense>
    @Insert
    suspend fun insertAll(vararg expenses: Expense)
    @Query("INSERT INTO expenses (user_id, item, price, expense_date) VALUES (:user_id, :item, :price, :expense_date)")
    suspend fun insert(user_id: Long, item: String, price: Int, expense_date: String)
    @Delete
    suspend fun delete(expense: Expense)
    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun delete(id: Long)
    @Query("""
        SELECT * FROM expenses 
        WHERE user_id = :user_id 
        AND expense_date = :expense_date
        """)
    suspend fun getUsersExpensesByDate(user_id: Long, expense_date: String): List<Expense>
    @Query("""
        SELECT IFNULL(SUM(price) ,0)
        FROM expenses 
        WHERE expense_date = :expense_date 
        AND user_id = :user_id
        """)
    suspend fun getUsersDailySum(user_id: Long, expense_date: String) : Int
    @Query("""
        SELECT IFNULL(SUM(price) ,0)
        FROM expenses 
        WHERE strftime('%Y-%m', expense_date) = strftime('%Y-%m', :expense_date) 
        AND user_id = :user_id
        """)
    suspend fun getUsersMonthlySum(user_id: Long, expense_date: String) : Int
    @Query("SELECT strftime('%Y-%m-%d', expense_date) FROM expenses WHERE id = :id")
    suspend fun getDate(id: Long): String
}