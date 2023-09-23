package com.example.expensediary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expensediary.data.Expense

class ExpenseListAdapter(
private var expenses: List<Expense>
) : RecyclerView.Adapter<ExpenseListAdapter.ExpenseViewHolder>() {
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
        holder.price.text = expenses[position].price.toString()
    }
    fun updateExpenses(expenses: List<Expense>) {
        this.expenses = expenses
        notifyDataSetChanged()
    }
    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item: TextView = itemView.findViewById(R.id.item)
        val price: TextView = itemView.findViewById(R.id.price)
    }
}