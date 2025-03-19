package com.example.shoppy_onlineshop.ui.home.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.ui.home.adapter.design.RoundedCornersTransformation

class ProductAdapter(private var products: List<StoreProduct>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
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
        // Use Glide to load the product image using the 'imageUrl' field
        Glide.with(holder.productImageView.context)
            .load(product.thumbnail)  // This is the product's image URL
            .transform(CenterCrop(), RoundedCornersTransformation(16f)) // Apply transformations like rounded corners
            .into(holder.productImageView)  // This will load the image into the ImageView
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateData(newProducts: List<StoreProduct>) {
        products = newProducts
        notifyDataSetChanged()
    }
}