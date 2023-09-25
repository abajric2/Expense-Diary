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
        suspend fun insert(user: User, context: Context) = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            db!!.userDao().insertAll(user)
        }
        suspend fun usernameExists(username: String, context: Context): Boolean = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            val users: List<User> = db!!.userDao().searchByUsername(username)
            var usernameExists: Boolean = false
            if(users.isNotEmpty()) usernameExists = true
            usernameExists
        }
        suspend fun logIn(username: String, password: String, context: Context): User? = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            var users: List<User> = db!!.userDao().searchByUsernameAndPassword(username, password)
            var user: User? = null
            if(users.isNotEmpty() && users.size == 1) user = users.get(0)
            user
        }
        suspend fun getById(id: Long, context: Context): User? = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            val users: List<User> = db!!.userDao().getById(id)
            if(users.isNotEmpty() && users.size == 1) users.get(0)
            else null
        }
        suspend fun update(user: User, context: Context) = withContext(Dispatchers.IO) {
            var db = AppDatabase.getInstance(context)
            db!!.userDao().updateAll(user)
        }
    }
}