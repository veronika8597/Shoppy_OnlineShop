package com.example.shoppy_onlineshop.ui.home.products

import android.util.Log
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
import com.example.shoppy_onlineshop.helpers.isProductInFavorites
import com.example.shoppy_onlineshop.helpers.toggleFavoriteStatus

class ProductAdapter(
    private var products: List<StoreProduct>,
    private val productClickListener: ProductClickListener,
    private val userId: String
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
        private val titleTextView: TextView = itemView.findViewById(R.id.productTitleTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)
        private val emptyHeart: ImageView = itemView.findViewById(R.id.favorite_button_emptyHeart)
        private val filledHeart: ImageView = itemView.findViewById(R.id.favorite_button_filledHeart)

        private var isFavorite = false

        fun bind(product: StoreProduct, productClickListener: ProductClickListener, userId: String) {
            titleTextView.text = product.title
            priceTextView.text = "$${product.price}"

            Glide.with(productImageView.context)
                .load(product.thumbnail)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(productImageView)

            // Check if product is in favorites
            isProductInFavorites(userId, product) { result ->
                isFavorite = result
                updateFavoriteUI(isFavorite)
            }

            // Click listener for the heart icons
            val favoriteClickListener = View.OnClickListener {
                isFavorite = toggleFavoriteStatus(
                    userId,
                    emptyHeart,
                    filledHeart,
                    product,
                    isFavorite
                )
                updateFavoriteUI(isFavorite)
            }

            // Set favorite icon click listeners
            emptyHeart.setOnClickListener(favoriteClickListener)
            emptyHeart.isClickable = true
            emptyHeart.isFocusable = true

            filledHeart.setOnClickListener(favoriteClickListener)
            filledHeart.isClickable = true
            filledHeart.isFocusable = true

            // Entire item click (not triggered by heart clicks anymore)
            itemView.setOnClickListener {
                productClickListener.onProductClick(product)
            }
        }

        private fun updateFavoriteUI(isFavorite: Boolean) {
            filledHeart.visibility = if (isFavorite) View.VISIBLE else View.INVISIBLE
            emptyHeart.visibility = if (!isFavorite) View.VISIBLE else View.INVISIBLE
            Log.d("ProductAdapter", "Updated UI: isFavorite = $isFavorite")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position], productClickListener, userId)
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newProducts: List<StoreProduct>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
