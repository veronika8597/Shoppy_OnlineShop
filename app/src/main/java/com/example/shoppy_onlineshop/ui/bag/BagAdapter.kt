package com.example.shoppy_onlineshop.ui.bag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreProduct

class BagAdapter(
    private var bagItems: List<BagItem>,
    private val onIncreaseQuantity: (Int) -> Unit,
    private val onDecreaseQuantity: (Int) -> Unit,
    private val onRequestDeleteConfirmation: (Int) -> Unit
) : RecyclerView.Adapter<BagAdapter.BagViewHolder>() {

    class BagViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productName_TextView: TextView = view.findViewById(R.id.productTitle)
        val productPrice_TextView: TextView = view.findViewById(R.id.productUnitPrice)
        val productPicture_ImageView: ImageView = view.findViewById(R.id.productImage)
        val productQuantity_TextView: TextView = view.findViewById(R.id.quantityText)
        val productTotalPrice_TextView: TextView = view.findViewById(R.id.totalPriceText)
        val increaseButton: TextView = view.findViewById(R.id.btnIncrease)
        val decreaseButton: TextView = view.findViewById(R.id.btnDecrease)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bag, parent, false)
        return BagViewHolder(view)
    }

    override fun getItemCount(): Int = bagItems.size

    fun getItem(position: Int): BagItem = bagItems[position]

    override fun onBindViewHolder(holder: BagViewHolder, position: Int) {
        val item = bagItems[position]

        holder.productName_TextView.text = item.product.title
        holder.productPrice_TextView.text = "$%.2f".format(item.product.price)
        holder.productQuantity_TextView.text = item.quantity.toString()
        holder.productTotalPrice_TextView.text = "Total: $%.2f".format(item.product.price * item.quantity)

        Glide.with(holder.itemView.context)
            .load(item.product.thumbnail)
            .transform(CenterCrop(), RoundedCorners(16))
            .into(holder.productPicture_ImageView)

        holder.increaseButton.setOnClickListener {
            onIncreaseQuantity(item.product.id)
        }

        holder.decreaseButton.setOnClickListener {
            if (item.quantity > 1) {
                onDecreaseQuantity(item.product.id)
            } else {
                onRequestDeleteConfirmation(item.product.id)
            }
        }
    }

    fun updateItems(newItems: List<BagItem>) {
        this.bagItems = newItems
        notifyDataSetChanged()
    }
}