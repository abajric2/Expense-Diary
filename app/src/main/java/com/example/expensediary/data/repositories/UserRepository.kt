package com.example.expensediary.data.repositories

import android.content.Context
import com.example.expensediary.data.User
import com.example.expensediary.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {
    companion object {
        suspend fun getAll(context: Context):List<User> = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            var users = db!!.userDao().getAll()
            users
        }
    }
}