package com.example.shoppy_onlineshop.ui.bag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreProduct

class BagAdapter(private var items: List<CartItem>) : RecyclerView.Adapter<BagAdapter.BagViewHolder>() {

    class BagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName_TextView: TextView = view.findViewById(R.id.productTitle)
        val productPrice_TextView: TextView = view.findViewById(R.id.productUnitPrice)
        val productQuantity_TextView: TextView = view.findViewById(R.id.quantityText)
        val productTotalPrice_TextView: TextView = view.findViewById(R.id.totalPriceText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bag, parent, false)
        return BagViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: BagViewHolder, position: Int) {
        val item = items[position]

        holder.productName_TextView.text = item.product.title
        holder.productPrice_TextView.text = "$%.2f".format(item.product.price)
        holder.productQuantity_TextView.text = item.quantity.toString()
        holder.productTotalPrice_TextView.text = "Total: $%.2f".format(item.product.price * item.quantity)
    }

    fun updateItems(newItems: List<CartItem>) {
        this.items = newItems
        notifyDataSetChanged()
    }
}