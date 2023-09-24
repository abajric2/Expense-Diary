package com.example.expensediary

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
import java.sql.Date
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
    private lateinit var addExpense: ImageButton
    private lateinit var item: EditText
    private lateinit var price: EditText
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
        addExpense = findViewById(R.id.addExpense)
        datePicker = findViewById(R.id.datePicker)
        item = findViewById(R.id.item)
        price = findViewById(R.id.price)
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
        addExpense.setOnClickListener {
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            scope.launch {
                ExpenseRepository.insert(user!!.id, item.text.toString(), price.text.toString().toInt(), selectedDate.text.toString(), context)
                expenses = ExpenseRepository.getUsersExpensesByDate(user!!.id, selectedDate.text.toString(), context)
                expenseListAdapter.updateExpenses(expenses)
                setDailyLimitInfo()
                setMonthlyLimitInfo()
            }
        }
        setMonthlyLimitInfo()
        setDailyLimitInfo()
    }
    private fun setMonthlyLimitInfo() {
        val context: Context = this
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            var info: String = "MONTHLY LIMIT: " + user!!.monthlyLimit + " " + user!!.currency
            if(ExpenseRepository.expensesExist(user!!.id, context)) {
                var sum = ExpenseRepository.getUsersMonthlySum(
                    user!!.id,
                    selectedDate.text.toString(),
                    context
                )
                info += "\n\nMONTHLY EXPENSE: " + sum + " " + user!!.currency
                if (sum > user!!.monthlyLimit) {
                    info += "\n\nLIMIT EXCEEDED!\nAMOUNT OF OVERFLOW: " + (sum - user!!.monthlyLimit) + " " + user!!.currency
                } else {
                    info += "\n\nREMAINING BEFORE OVERDRAFT: " + (user!!.monthlyLimit - sum) + " " + user!!.currency
                }
            } else {
                info += "\n\nMONTHLY EXPENSE: " + 0 + " " + user!!.currency
                info += "\n\nREMAINING BEFORE OVERDRAFT: " + (user!!.monthlyLimit) + " " + user!!.currency
            }
            monthlyLimitInfo.text = info
        }
    }
    private fun setDailyLimitInfo() {
        val context: Context = this
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            var info: String = "DAILY LIMIT: " + user!!.dailyLimit + " " + user!!.currency
            if(ExpenseRepository.expensesExist(user!!.id, context)) {
                var sum = ExpenseRepository.getUsersDailySum(
                    user!!.id,
                    selectedDate.text.toString(),
                    context
                )
                info += "\n\nDAILY EXPENSE: " + sum + " " + user!!.currency
                if (sum > user!!.dailyLimit) {
                    info += "\n\nLIMIT EXCEEDED!\n" +
                            "AMOUNT OF OVERFLOW:  " + (sum - user!!.dailyLimit) + " " + user!!.currency
                } else {
                    info += "\n\nREMAINING BEFORE OVERDRAFT: " + (user!!.dailyLimit - sum) + " " + user!!.currency
                }
            } else {
                info += "\n\nDAILY EXPENSE: " + 0 + " " + user!!.currency
                info += "\n\nREMAINING BEFORE OVERDRAFT: " + (user!!.dailyLimit) + " " + user!!.currency
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