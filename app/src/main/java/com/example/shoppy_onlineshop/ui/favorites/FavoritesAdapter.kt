package com.example.shoppy_onlineshop.ui.favorites

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreProduct

class FavoritesAdapter(private var favorites: List<StoreProduct>) : RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder>() {

    class FavoritesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val favoritesTextView: TextView = view.findViewById(R.id.favoriteProductName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {

        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_favorites, parent, false)
        return FavoritesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.favoritesTextView.text = favorites[position].title
    }

    fun updateFavoriteProducts(newFavoriteProducts: List<StoreProduct>){
        this.favorites = newFavoriteProducts
        notifyDataSetChanged()
    }
}


