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

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<TextView>(R.id.signUp).setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java).apply {
            }
            startActivity(intent)
            return@setOnClickListener
        }
        findViewById<Button>(R.id.logIn).setOnClickListener {
            val context: Context = this
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            scope.launch {
                var username: String = findViewById<EditText>(R.id.username).text.toString()
                var password: String = findViewById<EditText>(R.id.password).text.toString()
                var user: User? = UserRepository.logIn(username, password, context)
                if(user == null) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Log in error!")
                    builder.setMessage("User with entered data not found")
                    builder.setPositiveButton("OK") { dialog, which ->
                    }
                    builder.show()
                } else {
                    val intent = Intent(context, HomeActivity::class.java).apply {
                        putExtra("user", user)
                    }
                    startActivity(intent)
                }
            }
            return@setOnClickListener
        }
        //testing(this)
    }
    private fun testing(context: Context) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            var boole: Boolean = false
            if(UserRepository.usernameExists("asd", context)) boole = true
            if(boole) println("postoji")
            else println("ne postoji")
        }

    }
}