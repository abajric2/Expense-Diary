package com.example.expensediary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.expensediary.data.User

class ProfileActivity : AppCompatActivity() {
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var dailyLimit: EditText
    private lateinit var monthlyLimit: EditText
    private lateinit var currency: EditText
    private lateinit var home: TextView
    private lateinit var logOut: TextView
    private lateinit var edit: Button
    private lateinit var update: Button
    private lateinit var delete: Button
    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        user = intent.getParcelableExtra("user")
        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        dailyLimit = findViewById(R.id.dailyLimit)
        monthlyLimit = findViewById(R.id.monthlyLimit)
        currency = findViewById(R.id.currency)
        home = findViewById(R.id.home)
        logOut = findViewById(R.id.logOut)
        edit = findViewById(R.id.edit)
        update = findViewById(R.id.update)
        delete = findViewById(R.id.delete)
        firstName.setText(user!!.firstName)
        lastName.setText(user!!.lastName)
        username.setText(user!!.username)
        password.setText(user!!.password)
        dailyLimit.setText((user!!.dailyLimit).toString())
        monthlyLimit.setText((user!!.monthlyLimit).toString())
        currency.setText(user!!.currency)
    }
}