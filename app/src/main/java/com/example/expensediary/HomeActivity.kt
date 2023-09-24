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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val user: User? = intent.getParcelableExtra("user")
        val welcomeMessage: String = "Welcome " + (user?.firstName ?: "")
        findViewById<TextView>(R.id.welcome).text = welcomeMessage
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
            }
        }
        datePicker.setOnClickListener {
            DatePickerDialog(this, picker, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }
    private fun updateSelectedDate(calendar: Calendar) {
        val format = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(format, Locale.UK)
        selectedDate.text = sdf.format(calendar.time)
    }
}