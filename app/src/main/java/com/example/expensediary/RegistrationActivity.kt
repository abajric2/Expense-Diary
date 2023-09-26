package com.example.expensediary

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.expensediary.data.User
import com.example.expensediary.data.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegistrationActivity : AppCompatActivity() {
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var monthlyLimit: EditText
    private lateinit var dailyLimit: EditText
    private lateinit var currency: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        firstName = findViewById(R.id.firstName)
        lastName = findViewById(R.id.lastName)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        monthlyLimit = findViewById(R.id.monthlyLimit)
        dailyLimit = findViewById(R.id.dailyLimit)
        currency = findViewById(R.id.currency)
        findViewById<Button>(R.id.signUp).setOnClickListener {
            var usersFirstName: String = firstName.text.toString()
            var usersLastName: String = lastName.text.toString()
            var usersUsername: String = username.text.toString()
            var usersPassword: String = password.text.toString()
            var usersDailyLimit: Double? = dailyLimit.text.toString().toDoubleOrNull()
            if(usersDailyLimit == null) usersDailyLimit = 0.0
            var usersMonthlyLimit: Double? = monthlyLimit.text.toString().toDoubleOrNull()
            if(usersMonthlyLimit == null) usersMonthlyLimit = 0.0
            var usersCurreny: String = currency.text.toString()
            var user: User = User(firstName = usersFirstName, lastName = usersLastName, username = usersUsername,
                password = usersPassword, dailyLimit = usersDailyLimit, monthlyLimit = usersMonthlyLimit, currency = usersCurreny)
            val context: Context = this
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            scope.launch {
                if(UserRepository.usernameExists(usersUsername, context)) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Sign up error!")
                    builder.setMessage("Username that you entered already exists")
                    builder.setPositiveButton("OK") { dialog, which ->
                    }
                    builder.show()
                }
                else {
                    UserRepository.insert(user, context)
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Account successfully created!")
                    builder.setMessage("Please log in to continue")
                    builder.setCancelable(false)
                    builder.setPositiveButton("OK") { dialog, which ->
                        val intent = Intent(context, LoginActivity::class.java).apply {
                        }
                        startActivity(intent)
                    }
                    builder.show()
                }
            }

            return@setOnClickListener
        }
        findViewById<TextView>(R.id.logIn).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java).apply {
            }
            startActivity(intent)
            return@setOnClickListener
        }
    }
}