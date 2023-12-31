package com.example.expensediary

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import org.w3c.dom.Text
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HomeActivity : AppCompatActivity(), ExpenseListAdapter.ButtonClickListener {
    private lateinit var selectedDate: TextView
    private lateinit var datePicker: ImageButton
    private lateinit var expenseList: RecyclerView
    private lateinit var expenseListAdapter: ExpenseListAdapter
    private lateinit var expenses: List<Expense>
    private lateinit var monthlyLimitInfo: TextView
    private lateinit var dailyLimitInfo: TextView
    private lateinit var monthlyLimitExceeded: TextView
    private lateinit var dailyLimitExceeded: TextView
    private lateinit var addExpense: ImageButton
    private lateinit var item: EditText
    private lateinit var price: EditText
    private lateinit var showMonthlyList: Button
    private lateinit var showDailyList: Button
    private lateinit var listInfo: TextView
    private lateinit var logOut: TextView
    private lateinit var profile: TextView
    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        user = intent.getParcelableExtra("user")
        val welcomeMessage: String = "Welcome " + (user?.firstName ?: "")
        findViewById<TextView>(R.id.welcome).text = welcomeMessage
        monthlyLimitInfo = findViewById(R.id.monthlyLimitInfo)
        dailyLimitInfo = findViewById(R.id.dailyLimitInfo)
        monthlyLimitExceeded = findViewById(R.id.monthlyLimitExceeded)
        dailyLimitExceeded = findViewById(R.id.dailyLimitExceeded)
        selectedDate = findViewById(R.id.selectedDate)
        addExpense = findViewById(R.id.addExpense)
        datePicker = findViewById(R.id.datePicker)
        logOut = findViewById(R.id.logOut)
        listInfo = findViewById(R.id.listInfo)
        profile = findViewById(R.id.profile)
        showDailyList = findViewById(R.id.showDailyList)
        showMonthlyList = findViewById(R.id.showMonthlyList)
        showDailyList.visibility = View.INVISIBLE
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
        expenseListAdapter = ExpenseListAdapter(listOf(), user, this, this)
        expenseList.adapter = expenseListAdapter
        val context: Context = this
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            expenses = ExpenseRepository.getUsersExpensesByDate(user!!.id, selectedDate.text.toString(), context)
            expenseListAdapter.updateExpenses(expenses)
        }
        val calendar = Calendar.getInstance()
        val picker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            scope.launch {
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateSelectedDate(calendar)
                if(showDailyList.visibility == View.VISIBLE) {
                    expenses = ExpenseRepository.getUsersExpensesByMonth(
                        user!!.id,
                        selectedDate.text.toString(),
                        context
                    )
                } else {
                    expenses = ExpenseRepository.getUsersExpensesByDate(
                        user!!.id,
                        selectedDate.text.toString(),
                        context
                    )
                }
                expenseListAdapter.updateExpenses(expenses)
                setDailyLimitInfo()
                setMonthlyLimitInfo()
            }
        }
        datePicker.setOnClickListener {
            DatePickerDialog(
                this,
                R.style.CustomDatePickerDialogTheme,
                picker,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        addExpense.setOnClickListener {
            if(item.text.toString().trim().isEmpty() ||
                price.text.toString().trim().isEmpty()) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Sign up error!")
                builder.setMessage("You must fill in all fields provided!")
                builder.setPositiveButton("OK") { dialog, which ->
                    dialog.dismiss()
                }
                builder.show()
            } else {
                val scope = CoroutineScope(Job() + Dispatchers.Main)
                scope.launch {
                    ExpenseRepository.insert(
                        user!!.id,
                        item.text.toString(),
                        price.text.toString().toDouble(),
                        selectedDate.text.toString(),
                        context
                    )
                    if(showDailyList.visibility == View.VISIBLE) {
                        expenses = ExpenseRepository.getUsersExpensesByMonth(
                            user!!.id,
                            selectedDate.text.toString(),
                            context
                        )
                    } else {
                        expenses = ExpenseRepository.getUsersExpensesByDate(
                            user!!.id,
                            selectedDate.text.toString(),
                            context
                        )
                    }
                    expenseListAdapter.updateExpenses(expenses)
                    setDailyLimitInfo()
                    setMonthlyLimitInfo()
                    item.setText("")
                    price.setText("")
                }
            }
        }
        showMonthlyList.setOnClickListener {
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            scope.launch {
                expenses = ExpenseRepository.getUsersExpensesByMonth(user!!.id, selectedDate.text.toString(), context)
                expenseListAdapter.updateExpenses(expenses)
                showMonthlyList.visibility = View.INVISIBLE
                showDailyList.visibility = View.VISIBLE
                val listInfoText = "Details of your monthly expenses"
                listInfo.text = listInfoText
            }
        }
        showDailyList.setOnClickListener {
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            scope.launch {
                expenses = ExpenseRepository.getUsersExpensesByDate(user!!.id, selectedDate.text.toString(), context)
                expenseListAdapter.updateExpenses(expenses)
                showDailyList.visibility = View.INVISIBLE
                showMonthlyList.visibility = View.VISIBLE
                val listInfoText = "Details of your daily expenses"
                listInfo.text = listInfoText
            }
        }
        logOut.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java).apply {
            }
            startActivity(intent)
        }
        profile.setOnClickListener {
            val intent = Intent(context, ProfileActivity::class.java).apply {
                putExtra("user", user)
            }
            startActivity(intent)
        }
        val listInfoText = "Details of your daily expenses"
        listInfo.text = listInfoText
        dailyLimitExceeded.visibility = View.GONE
        monthlyLimitExceeded.visibility = View.GONE
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
                val roundedSum = (sum * 100.0).roundToInt() / 100.0
                info += "\n\nMONTHLY EXPENSE: " + roundedSum + " " + user!!.currency
                if (sum > user!!.monthlyLimit) {
                    monthlyLimitExceeded.visibility = View.VISIBLE
                    val rounded = ((sum - user!!.monthlyLimit) * 100.0).roundToInt() / 100.0
                    info += "\n\nAMOUNT OF OVERFLOW: " + rounded + " " + user!!.currency
                } else {
                    monthlyLimitExceeded.visibility = View.GONE
                    val rounded = ((user!!.monthlyLimit - sum) * 100.0).roundToInt() / 100.0
                    info += "\n\nREMAINING BEFORE OVERDRAFT: " + rounded + " " + user!!.currency
                }
            } else {
                monthlyLimitExceeded.visibility = View.GONE
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
                val roundedSum = (sum * 100.0).roundToInt() / 100.0
                info += "\n\nDAILY EXPENSE: " + roundedSum + " " + user!!.currency
                if (sum > user!!.dailyLimit) {
                    dailyLimitExceeded.visibility = View.VISIBLE
                    val rounded = ((sum - user!!.dailyLimit) * 100.0).roundToInt() / 100.0
                    info += "\n\nAMOUNT OF OVERFLOW:  " + rounded + " " + user!!.currency
                } else {
                    dailyLimitExceeded.visibility = View.GONE
                    val rounded = ((user!!.dailyLimit - sum) * 100.0).roundToInt() / 100.0
                    info += "\n\nREMAINING BEFORE OVERDRAFT: " + rounded + " " + user!!.currency
                }
            } else {
                dailyLimitExceeded.visibility = View.GONE
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
    override fun onButtonClick(expense_id: Long) {
        val context: Context = this
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            ExpenseRepository.delete(expense_id, context)
            if(showDailyList.visibility == View.VISIBLE) {
                expenses = ExpenseRepository.getUsersExpensesByMonth(
                    user!!.id,
                    selectedDate.text.toString(),
                    context
                )
            } else {
                expenses = ExpenseRepository.getUsersExpensesByDate(
                    user!!.id,
                    selectedDate.text.toString(),
                    context
                )
            }
            expenseListAdapter.updateExpenses(expenses)
            setDailyLimitInfo()
            setMonthlyLimitInfo()
        }
    }
}