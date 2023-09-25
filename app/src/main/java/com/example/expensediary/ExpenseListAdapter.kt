package com.example.expensediary

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensediary.data.Expense
import com.example.expensediary.data.User
import com.example.expensediary.data.repositories.ExpenseRepository
import com.example.expensediary.data.repositories.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExpenseListAdapter(
private var expenses: List<Expense>,
private var user: User?,
private var context: Context,
private val clickListener: ButtonClickListener
) : RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder>() {
    interface ButtonClickListener {
        fun onButtonClick(expense_id: Long)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder
    {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }
    override fun getItemCount(): Int = expenses.size
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.item.text = expenses[position].item
        val price: String = expenses[position].price.toString() + " " + (user?.currency ?: "")
        holder.price.text = price
        holder.delete.setOnClickListener {
            clickListener.onButtonClick(expenses[position].id)
        }
    }
    fun updateExpenses(expenses: List<Expense>) {
        this.expenses = expenses
        notifyDataSetChanged()
    }
    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: TextView = itemView.findViewById(R.id.item)
        val price: TextView = itemView.findViewById(R.id.price)
        val delete: ImageButton = itemView.findViewById(R.id.delete)
    }
}