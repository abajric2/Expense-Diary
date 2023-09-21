package com.example.expensediary

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.expensediary.data.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
       // testing(this)
    }
    private fun testing(context: Context) {
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            UserRepository.getAll(context)
        }
    }
}