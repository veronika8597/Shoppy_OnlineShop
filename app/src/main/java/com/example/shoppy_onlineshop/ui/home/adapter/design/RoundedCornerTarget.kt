package com.example.shoppy_onlineshop.utils

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class RoundedCornerTarget(
    private val imageView: ImageView,
    private val cornerRadius: Float
) : CustomTarget<Bitmap>() {

    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
        val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
        circularBitmapDrawable.cornerRadius = cornerRadius
        imageView.setImageDrawable(circularBitmapDrawable)
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        imageView.setImageDrawable(placeholder)
    }
}