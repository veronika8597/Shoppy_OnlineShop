package com.example.shoppy_onlineshop.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreProduct

class ProductAdapter(private var products: List<StoreProduct>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.productTitleTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.titleTextView.text = product.title
        holder.priceTextView.text = "$${product.price}"
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateData(newProducts: List<StoreProduct>) {
        products = newProducts
        notifyDataSetChanged()
    }
}