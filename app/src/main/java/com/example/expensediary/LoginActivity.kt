package com.example.expensediary

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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