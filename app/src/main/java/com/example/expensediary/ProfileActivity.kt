package com.example.expensediary

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
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
        setEditTextNotEditable(firstName)
        setEditTextNotEditable(lastName)
        setEditTextNotEditable(username)
        setEditTextNotEditable(password)
        setEditTextNotEditable(dailyLimit)
        setEditTextNotEditable(monthlyLimit)
        setEditTextNotEditable(currency)
        update.visibility = View.INVISIBLE
        edit.setOnClickListener {
            setEditTextEditable(firstName)
            setEditTextEditable(lastName)
            setEditTextEditable(username)
            setEditTextEditable(password)
            setEditTextEditable(dailyLimit)
            setEditTextEditable(monthlyLimit)
            setEditTextEditable(currency)
            edit.visibility = View.INVISIBLE
            update.visibility = View.VISIBLE
        }
        var context: Context = this
        update.setOnClickListener {
            val scope = CoroutineScope(Job() + Dispatchers.Main)
            scope.launch {
                if (UserRepository.usernameExists(username.text.toString(), context, user!!.id)) {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Update error!")
                    builder.setMessage("Username that you entered already exists")
                    builder.setPositiveButton("OK") { dialog, which ->
                    }
                    builder.show()
                } else {
                    var updatedUser = User(
                        id = user!!.id,
                        firstName = firstName.text.toString(),
                        lastName = lastName.text.toString(),
                        username = username.text.toString(),
                        password = password.text.toString(),
                        dailyLimit = dailyLimit.text.toString().toDouble(),
                        monthlyLimit = monthlyLimit.text.toString().toDouble(),
                        currency = currency.text.toString()
                    )
                    UserRepository.update(updatedUser, context)
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Update success!")
                    builder.setMessage("Data successfully updated!")
                    builder.setPositiveButton("OK") { dialog, which ->
                    }
                    builder.show()
                    user = updatedUser
                    setEditTextNotEditable(firstName)
                    setEditTextNotEditable(lastName)
                    setEditTextNotEditable(username)
                    setEditTextNotEditable(password)
                    setEditTextNotEditable(dailyLimit)
                    setEditTextNotEditable(monthlyLimit)
                    setEditTextNotEditable(currency)
                    update.visibility = View.INVISIBLE
                    edit.visibility = View.VISIBLE
                }
            }
        }
        home.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java).apply {
                putExtra("user", user)
            }
            startActivity(intent)
        }
        logOut.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java).apply {
            }
            startActivity(intent)
        }
        delete.setOnClickListener {
            val confirmationDialog = AlertDialog.Builder(this)
                .setTitle("Deleting confirmation")
                .setMessage("Are you sure you want to delete your account?")
                .setPositiveButton("YES") { _, _ ->
                    val scope = CoroutineScope(Job() + Dispatchers.Main)
                    scope.launch {
                        UserRepository.delete(user!!, context)
                        val builder = AlertDialog.Builder(context)
                        val positiveResponseDialog = AlertDialog.Builder(context)
                            .setCancelable(false)
                            .setTitle("Successfully deleted!")
                            .setMessage("Your account is deleted and you will be logged out!")
                            .setPositiveButton("OK") { dialog, _ ->
                                val intent = Intent(context, LoginActivity::class.java).apply {
                                }
                                startActivity(intent)
                            }
                            .create()

                        positiveResponseDialog.show()
                    }
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            confirmationDialog.show()
            /* builder.setTitle("Successfully deleted!")
                builder.setMessage("Your account is deleted and you will be logged out!")
                builder.setCancelable(false)
                builder.setPositiveButton("OK") { dialog, which ->
                    val intent = Intent(context, LoginActivity::class.java).apply {
                    }
                    startActivity(intent)
                }
                builder.show()*/
        }
    }

    private fun setEditTextNotEditable(editText: EditText) {
        editText.isFocusable = false
        editText.isFocusableInTouchMode = false
        editText.isCursorVisible = false
        editText.keyListener = null
    }

    private fun setEditTextEditable(editText: EditText) {
        editText.isFocusable = true
        editText.isFocusableInTouchMode = true
        editText.isCursorVisible = true
        editText.keyListener = EditText(this).keyListener
    }
}