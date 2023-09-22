package com.example.expensediary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.expensediary.data.User

class HomeActivity : AppCompatActivity() {
    private lateinit var selectedDate: TextView
    private lateinit var datePicker: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val user: User? = intent.getParcelableExtra("user")

    }
}