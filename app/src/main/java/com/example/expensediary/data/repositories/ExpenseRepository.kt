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
        suspend fun getUsersExpensesByDate(user_id: Int, expense_date: Date, context: Context): List<Expense> = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            val expenses: List<Expense> = db!!.expenseDao().getUsersExpensesByDate(user_id, expense_date)
            expenses
        }
    }
}