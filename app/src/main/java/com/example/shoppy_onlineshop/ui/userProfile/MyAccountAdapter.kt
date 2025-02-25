package com.example.shoppy_onlineshop.ui.userProfile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

class MyAccountAdapter(
    private val sections: List<AccountSection>,
    private val onItemClick: (AccountSection) -> Unit // Click listener
) : RecyclerView.Adapter<MyAccountAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.rowLogoImage)
        val title: TextView = view.findViewById(R.id.rowNameText)
        val cardView: CardView = view.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_account_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = sections[position]
        holder.icon.setImageResource(section.icon)
        holder.title.text = section.title

        // Handle item clicks
        holder.cardView.setOnClickListener { onItemClick(section) }
    }

    override fun getItemCount(): Int = sections.size
}
