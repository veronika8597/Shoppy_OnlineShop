package com.example.shoppy_onlineshop.ui.home.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppy_onlineshop.api.Review
import com.example.shoppy_onlineshop.databinding.ItemReviewBinding

class ReviewAdapter(private val reviews: List<Review>) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int = reviews.size

    inner class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: Review) {
            // Binding the data from Review object to the respective views
            binding.reviewAuthor.text = review.reviewerName ?: "Anonymous"
            binding.reviewRating.text = "Rating: ${review.rating ?: "N/A"}"
            binding.reviewText.text = review.comment ?: "No comment available"
            binding.reviewEmail.text = review.reviewerEmail ?: "No email provided"
            binding.reviewDate.text = review.date ?: "No date available"
        }
    }
}

