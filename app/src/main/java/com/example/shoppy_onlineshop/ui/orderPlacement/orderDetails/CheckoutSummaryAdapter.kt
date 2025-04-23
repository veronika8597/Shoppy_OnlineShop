package com.example.shoppy_onlineshop.ui.orderPlacement.orderDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.ui.bag.BagItem

class CheckoutSummaryAdapter(private val items: List<BagItem>) :
    RecyclerView.Adapter<CheckoutSummaryAdapter.SummaryViewHolder>() {

    class SummaryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.summaryProductName)
        val quantity: TextView = view.findViewById(R.id.summaryProductQuantity)
        val price: TextView = view.findViewById(R.id.summaryProductPrice)
        val image: ImageView = view.findViewById(R.id.summaryProductImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkout_summary_row, parent, false)
        return SummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.product.title
        holder.quantity.text = "x${item.quantity}"
        holder.price.text = "₪%.2f".format(item.product.price * item.quantity)

        Glide.with(holder.itemView)
            .load(item.product.thumbnail)
            .placeholder(R.drawable.placeholder_lavander)
            .into(holder.image)

    }

    override fun getItemCount() = items.size
}