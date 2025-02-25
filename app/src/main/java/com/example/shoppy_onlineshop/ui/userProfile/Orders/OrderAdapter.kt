package com.example.shoppy_onlineshop.ui.userProfile.Orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

class OrderAdapter(private val orders: List<order>) : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val orderID_textView = itemView.findViewById<TextView>(R.id.orderId_textView)
        val orderDate_textView = itemView.findViewById<TextView>(R.id.orderDate_textView)
        val orderStatus_textView = itemView.findViewById<TextView>(R.id.orderStatus_textView)
        val totalPrice_textView = itemView.findViewById<TextView>(R.id.totalPrice_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.purchases_row, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val order = orders[position]
        holder.orderID_textView.text = order.orderId
        holder.orderDate_textView.text = order.orderDate
        holder.orderStatus_textView.text = order.orderStatus
        holder.totalPrice_textView.text = order.totalPrice

    }
}