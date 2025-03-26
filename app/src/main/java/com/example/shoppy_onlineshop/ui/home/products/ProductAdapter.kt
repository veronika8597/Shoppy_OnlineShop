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
import com.example.shoppy_onlineshop.helpers.isProductInFavorites
import com.example.shoppy_onlineshop.helpers.toggleFavoriteStatus
import com.example.shoppy_onlineshop.ui.favorites.FavoritesViewModel

class ProductAdapter(
    private var products: List<StoreProduct>,
    private val productClickListener: ProductClickListener,
    private val userId: String
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImageView: ImageView = itemView.findViewById(R.id.productImageView)
        val titleTextView: TextView = itemView.findViewById(R.id.productTitleTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.productPriceTextView)

        val emptyHeart: ImageView = itemView.findViewById(R.id.favorite_button_emptyHeart)
        val filledHeart: ImageView = itemView.findViewById(R.id.favorite_button_filledHeart)

        fun bind(product: StoreProduct, productClickListener: ProductClickListener, userId: String) {
            titleTextView.text = product.title
            priceTextView.text = "$${product.price}"


            Glide.with(productImageView.context)
                .load(product.thumbnail)
                .transform(CenterCrop(), RoundedCorners(16))
                .into(productImageView)

            // Check favorite status
            isProductInFavorites(userId, product) { isFavorite ->
                updateFavoriteUI(isFavorite)

                val clickListener = View.OnClickListener {
                    val newStatus = toggleFavoriteStatus(userId, emptyHeart, filledHeart, product, isFavorite)
                    updateFavoriteUI(newStatus)
                }

                emptyHeart.setOnClickListener(clickListener)
                filledHeart.setOnClickListener(clickListener)
            }

            itemView.setOnClickListener {

                productClickListener.onProductClick(product)

            }
        }
        private fun updateFavoriteUI(isFavorite: Boolean) {
            filledHeart.visibility = if (isFavorite) View.VISIBLE else View.GONE
            emptyHeart.visibility = if (!isFavorite) View.VISIBLE else View.GONE
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
