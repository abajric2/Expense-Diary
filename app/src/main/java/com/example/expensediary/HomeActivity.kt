package com.example.expensediary

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.expensediary.data.Expense
import com.example.expensediary.data.User
import com.example.expensediary.data.repositories.ExpenseRepository
import com.example.expensediary.data.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    private lateinit var selectedDate: TextView
    private lateinit var datePicker: ImageButton
    private lateinit var expenseList: RecyclerView
    private lateinit var expenseListAdapter: ExpenseListAdapter
    private lateinit var expenses: List<Expense>
    private lateinit var monthlyLimitInfo: TextView
    private lateinit var dailyLimitInfo: TextView
    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        user = intent.getParcelableExtra("user")
        val welcomeMessage: String = "Welcome " + (user?.firstName ?: "")
        findViewById<TextView>(R.id.welcome).text = welcomeMessage
        monthlyLimitInfo = findViewById(R.id.monthlyLimitInfo)
        dailyLimitInfo = findViewById(R.id.dailyLimitInfo)
        selectedDate = findViewById(R.id.selectedDate)
        datePicker = findViewById(R.id.datePicker)
        expenses = listOf()
        updateSelectedDate(Calendar.getInstance())
        expenseList = findViewById(R.id.expensesList)
        expenseList.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        expenseListAdapter = ExpenseListAdapter(listOf())
        expenseList.adapter = expenseListAdapter
        val context: Context = this
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            expenses = ExpenseRepository.getUsersExpensesByDate(user!!.id, selectedDate.text.toString(), context)
            expenseListAdapter.updateExpenses(expenses)
        }
        val calendar = Calendar.getInstance()
        val picker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateSelectedDate(calendar)
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            scope.launch {
                expenses = ExpenseRepository.getUsersExpensesByDate(user!!.id, selectedDate.text.toString(), context)
                expenseListAdapter.updateExpenses(expenses)
                setDailyLimitInfo()
                setMonthlyLimitInfo()
            }
        }
        datePicker.setOnClickListener {
            DatePickerDialog(this, picker, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        setMonthlyLimitInfo()
        setDailyLimitInfo()
    }
    private fun setMonthlyLimitInfo() {
        val context: Context = this
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            var info: String = "Your monthly limit is " + user!!.monthlyLimit + " " + user!!.currency + "."
            if(ExpenseRepository.expensesExist(user!!.id, context)) {
                var sum = ExpenseRepository.getUsersMonthlySum(
                    user!!.id,
                    selectedDate.text.toString(),
                    context
                )
                info += "\n\nUp to this moment, the sum of all expenses during the selected month is " + sum + " " + user!!.currency + "."
                if (sum > user!!.monthlyLimit) {
                    info += "\n\nBe careful with your expenses, you have exceeded your monthly limit! You have spent " + (sum - user!!.monthlyLimit) + " " + user!!.currency + " more than you should have."
                } else {
                    info += "\n\nYou have " + (user!!.monthlyLimit - sum) + " " + user!!.currency + " left to spend during the selected month if you do not want to exceed the set monthly limit."
                }
            } else {
                info += "\n\nYou haven't spent money so far this month."
            }
            monthlyLimitInfo.text = info
        }
    }
    private fun setDailyLimitInfo() {
        val context: Context = this
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            var info: String = "Your daily limit is " + user!!.dailyLimit + " " + user!!.currency + "."
            if(ExpenseRepository.expensesExist(user!!.id, context)) {
                var sum = ExpenseRepository.getUsersDailySum(
                    user!!.id,
                    selectedDate.text.toString(),
                    context
                )
                info += "\n\nUp to this moment, the sum of all expenses during the selected day is " + sum + " " + user!!.currency + "."
                if (sum > user!!.dailyLimit) {
                    info += "\n\nBe careful with your expenses, you have exceeded your daily limit! You have spent " + (sum - user!!.dailyLimit) + " " + user!!.currency + " more than you should have."
                } else {
                    info += "\n\nYou have " + (user!!.dailyLimit - sum) + " " + user!!.currency + " left to spend during the selected day if you do not want to exceed the set daily limit."
                }
            } else {
                info += "\n\nYou haven't spent money so far this day."
            }
            dailyLimitInfo.text = info
        }
    }
    private fun updateSelectedDate(calendar: Calendar) {
        val format = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(format, Locale.UK)
        selectedDate.text = sdf.format(calendar.time)
    }
}