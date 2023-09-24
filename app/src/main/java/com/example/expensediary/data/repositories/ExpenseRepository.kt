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
    }
}