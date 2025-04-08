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

class OrderDetailsAdapter(private val items: List<BagItem>): RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val orderProductName: TextView = itemView.findViewById(R.id.productTitle_textView)
        val orderProductQuantity: TextView = itemView.findViewById(R.id.quantity_textView)
        val orderProductPrice: TextView = itemView.findViewById(R.id.productPrice_textView)
        val orderProductImage: ImageView = itemView.findViewById(R.id.productImageView_imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_product, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.orderProductName.text = item.product.title
        holder.orderProductQuantity.text = "Qty: ${item.quantity}"
        holder.orderProductPrice.text = "$%.2f".format(item.product.price * item.quantity)

        Glide.with(holder.itemView.context)
            .load(item.product.thumbnail)
            .centerCrop()
            .into(holder.orderProductImage)
    }

    override fun getItemCount(): Int = items.size
}