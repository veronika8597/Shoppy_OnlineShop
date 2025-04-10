package com.example.shoppy_onlineshop.ui.home.categories

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestListener
import com.example.shoppy_onlineshop.R
import com.example.shoppy_onlineshop.api.StoreCategory
import com.example.shoppy_onlineshop.ui.home.adapter.design.RoundedCornersTransformation

class CategoryAdapter(private var categories: List<StoreCategory>,
                      private val categoryClickListener: CategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryImageView: ImageView = itemView.findViewById(R.id.categoryImageView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)

        fun bind(category: StoreCategory, categoryClickListener: CategoryClickListener) {

            categoryTextView.text = category.name
            // Map category names to placeholder images
            val imageResId = when (category.slug) {
                "beauty" -> R.drawable.beauty_placeholder
                "fragrances" -> R.drawable.fragrances_placeholder
                "skin-care" -> R.drawable.skincare_placeholder
                "groceries" -> R.drawable.groceries_placeholder
                "home-decoration" -> R.drawable.home_decoration_placeholder
                "furniture" -> R.drawable.furniture_placeholder
                "tops" -> R.drawable.tops_placeholder
                "womens-dresses" -> R.drawable.womens_dresses_placeholder
                "womens-shoes" -> R.drawable.womens_shoes_placeholder
                "mens-shirts" -> R.drawable.mens_shirts_placeholder
                "mens-shoes" -> R.drawable.mens_shoes_placeholder
                "mens-watches" -> R.drawable.mens_watches_placeholder
                "womens-watches" -> R.drawable.womens_watches_placeholder
                "womens-bags" -> R.drawable.womens_bags_placeholder
                "womens-jewellery" -> R.drawable.womens_jewellery_placeholder
                "sunglasses" -> R.drawable.sunglasses_placeholder
                "vehicle" -> R.drawable.automotive_placeholder
                "motorcycle" -> R.drawable.motorcycle_placeholder
                "lighting" -> R.drawable.lighting_placeholder
                "kitchen-accessories" -> R.drawable.kitchen_accessories_placeholder
                "laptops" -> R.drawable.laptops_placeholder
                "mobile-accessories" -> R.drawable.mobile_accessories_placeholder
                "smartphones" -> R.drawable.smartphones_placeholder
                "sports-accessories" -> R.drawable.sports_accessories_placeholder
                "tablets" -> R.drawable.tablets_placeholder

                else -> R.drawable.placeholder_lavander // Default placeholder
            }

            Glide.with(itemView.context)
                .load(imageResId)
                .placeholder(R.drawable.placeholder_lavander)
                .transform(CenterCrop(), RoundedCornersTransformation(40f))
                .into(categoryImageView)
            itemView.setOnClickListener {
                categoryClickListener.onCategoryClick(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category, categoryClickListener)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun updateData(newCategories: List<StoreCategory>) {
        categories = newCategories
        notifyDataSetChanged()
    }

}