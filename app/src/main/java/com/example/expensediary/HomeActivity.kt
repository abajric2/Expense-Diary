package com.example.expensediary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.expensediary.data.User

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val user: User? = intent.getParcelableExtra("user")
        val pozdrav: String = "Zdravo" + (user?.firstName ?: "")
        findViewById<TextView>(R.id.hello).text = pozdrav
    }
}