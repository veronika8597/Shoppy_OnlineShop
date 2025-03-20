package com.example.shoppy_onlineshop.ui.home.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreProduct

class ProductAdapter(
    private var products: List<StoreProduct>,
    private val productClickListener: ProductClickListener
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.productTitleTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)

        fun bind(product: StoreProduct, productClickListener: ProductClickListener) {
            titleTextView.text = product.title
            priceTextView.text = "$${product.price}"

            Glide.with(productImageView.context)
                .load(product.thumbnail)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(productImageView)

            itemView.setOnClickListener {
                productClickListener.onProductClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position], productClickListener)
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<StoreProduct>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
