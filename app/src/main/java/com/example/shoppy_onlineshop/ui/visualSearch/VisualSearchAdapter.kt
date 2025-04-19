package com.example.shoppy_onlineshop.ui.visualSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoppy_onlineshop.api.StoreProduct
import com.example.shoppy_onlineshop.databinding.ItemVisualSearchProductBinding

class VisualSearchAdapter(
    private val onItemClick: (StoreProduct) -> Unit
) : RecyclerView.Adapter<VisualSearchAdapter.VisualSearchViewHolder>() {

    private val products = mutableListOf<StoreProduct>()

    inner class VisualSearchViewHolder(val binding: ItemVisualSearchProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: StoreProduct) {
            binding.productTitle.text = product.title
            binding.productPrice.text = "$${product.price}"

            Glide.with(binding.productImage.context)
                .load(product.thumbnail)
                .into(binding.productImage)

            binding.root.setOnClickListener {
                onItemClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisualSearchViewHolder {
        val binding = ItemVisualSearchProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VisualSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VisualSearchViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    fun updateData(newList: List<StoreProduct>) {
        products.clear()
        products.addAll(newList)
        notifyDataSetChanged()
    }
}