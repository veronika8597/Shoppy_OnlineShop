package com.example.shoppy_onlineshop.ui.bag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreProduct

class BagAdapter(private var products: List<StoreProduct>) : RecyclerView.Adapter<BagAdapter.BagViewHolder>() {

    class BagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productNameTextView: TextView = view.findViewById(R.id.productName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bag, parent, false)
        return BagViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: BagViewHolder, position: Int) {
        holder.productNameTextView.text = products[position].title
    }

    fun updateProducts(newProducts: List<StoreProduct>){
        this.products = newProducts
        notifyDataSetChanged()
    }
}