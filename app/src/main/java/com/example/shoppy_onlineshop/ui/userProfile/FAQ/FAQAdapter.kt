package com.example.shoppy_onlineshop.ui.userProfile.FAQ

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.R

class FAQAdapter(private val faqList: List<FAQItem>) : RecyclerView.Adapter<FAQAdapter.FAQViewHolder>() {

    class FAQViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        val tvAnswer: TextView = itemView.findViewById(R.id.tvAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.faq_item, parent, false)
        return FAQViewHolder(view)
    }

    override fun onBindViewHolder(holder: FAQViewHolder, position: Int) {
        val faq = faqList[position]
        holder.tvQuestion.text = faq.question
        holder.tvAnswer.text = faq.answer

        holder.itemView.setOnClickListener {
            holder.tvAnswer.visibility = if (holder.tvAnswer.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }
    }

    override fun getItemCount(): Int = faqList.size
}
