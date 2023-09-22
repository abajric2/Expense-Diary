package com.example.expensediary

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.expensediary.data.User
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {
    private lateinit var selectedDate: TextView
    private lateinit var datePicker: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val user: User? = intent.getParcelableExtra("user")
        val welcomeMessage: String = "Welcome " + (user?.firstName ?: "")
        findViewById<TextView>(R.id.welcome).text = welcomeMessage
        selectedDate = findViewById(R.id.selectedDate)
        datePicker = findViewById(R.id.datePicker)
        updateSelectedDate(Calendar.getInstance())
        val calendar = Calendar.getInstance()
        val picker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateSelectedDate(calendar)
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