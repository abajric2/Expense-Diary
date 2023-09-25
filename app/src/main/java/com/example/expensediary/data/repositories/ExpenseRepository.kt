package com.example.expensediary.data.repositories

import android.content.Context
import com.example.expensediary.data.Expense
import com.example.expensediary.data.User
import com.example.expensediary.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Date

class ExpenseRepository {
    companion object {
        suspend fun getUsersExpensesByDate(user_id: Long, expense_date: String, context: Context): List<Expense> = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            val expenses: List<Expense> = db!!.expenseDao().getUsersExpensesByDate(user_id, expense_date)
            expenses
        }
        suspend fun getUsersExpensesByMonth(user_id: Long, expense_date: String, context: Context): List<Expense> = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            val expenses: List<Expense> = db!!.expenseDao().getUsersExpensesByMonth(user_id, expense_date)
            expenses
        }
        suspend fun getUsersMonthlySum(user_id: Long, date: String, context: Context): Int = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            val sum: Int = db!!.expenseDao().getUsersMonthlySum(user_id, date)
            sum
        }
        suspend fun getUsersDailySum(user_id: Long, date: String, context: Context): Int = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            val sum: Int = db!!.expenseDao().getUsersDailySum(user_id, date)
            sum
        }
        suspend fun expensesExist(user_id: Long, context: Context): Boolean = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            val expenses: List<Expense> = db!!.expenseDao().getByUsersId(user_id)
            expenses.isNotEmpty()
        }
        suspend fun insert(user_id: Long, item: String, price: Int, expense_date: String, context: Context) = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            db!!.expenseDao().insert(user_id, item, price, expense_date)
        }
        suspend fun delete(id: Long, context: Context) = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            db!!.expenseDao().delete(id)
        }
        suspend fun getDate(id: Long, context: Context): String = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            db!!.expenseDao().getDate(id)
        }
    }
}