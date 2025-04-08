package com.example.shoppy_onlineshop.ui.userProfile.Orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

class OrdersAdapter(val orders: MutableList<Order>, private val onViewDetailsClick: (Order) -> Unit) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val orderID_textView: TextView = itemView.findViewById(R.id.orderId_textView)
        val orderDate_textView: TextView = itemView.findViewById(R.id.orderDate_textView)
        val orderStatus_textView: TextView = itemView.findViewById(R.id.orderStatus_textView)
        val totalPrice_textView: TextView = itemView.findViewById(R.id.totalPrice_textView)
        val viewDetails_button: Button = itemView.findViewById(R.id.viewDetails_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.purchases_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val order = orders[position]

        holder.orderID_textView.text = order.orderId
        holder.orderDate_textView.text = order.timestamp.toString()
        holder.orderStatus_textView.text = order.status
        val totalPrice = order.items.sumOf { it.product.price * it.quantity }
        holder.totalPrice_textView.text = String.format("%.2f", totalPrice)


        holder.viewDetails_button.setOnClickListener {
            onViewDetailsClick(order)
        }
    }


    override fun getItemCount(): Int {
        return orders.size
    }


    fun updateOrders(newOrders: List<Order>) {
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }
}