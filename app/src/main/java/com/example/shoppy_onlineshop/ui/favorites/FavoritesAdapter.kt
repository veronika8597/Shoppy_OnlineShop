package com.example.shoppy_onlineshop.ui.favorites

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreProduct

class FavoritesAdapter(private var favorites: List<StoreProduct>, private val onAddToBagClick: (StoreProduct) -> Unit) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    class FavoritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val favProductImage: ImageView = view.findViewById(R.id.productImage)
        val favProductTitle: TextView = view.findViewById(R.id.productTitle)
        val favProductPrice: TextView = view.findViewById(R.id.productPrice)
        val isOutOfStock: TextView = view.findViewById(R.id.outOfStockLabel)
        val addToBagButton: TextView = view.findViewById(R.id.addToBag_Button)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {

        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_favorites, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    fun getItem(position: Int): StoreProduct {
        return favorites[position]
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.favProductTitle.text = favorites[position].title
        holder.favProductPrice.text = favorites[position].price.toString()
        holder.isOutOfStock.visibility = if (isProductInStock(favorites[position])) View.VISIBLE else View.GONE
        Glide.with(holder.itemView.context)
            .load(favorites[position].thumbnail)
            .transform(CenterCrop(), RoundedCorners(16))
            .into(holder.favProductImage)

        holder.addToBagButton.setOnClickListener {
            onAddToBagClick(favorites[position])
        }
    }


    fun updateFavoriteProducts(newFavoriteProducts: List<StoreProduct>){
        this.favorites = newFavoriteProducts
        notifyDataSetChanged()
    }
}

fun isProductInStock(product: StoreProduct): Boolean{
    return product.stock == 0

}


