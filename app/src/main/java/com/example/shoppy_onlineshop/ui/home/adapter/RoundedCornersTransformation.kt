package com.example.shoppy_onlineshop.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import java.security.MessageDigest

class RoundedCornersTransformation(private val radius: Float) : BitmapTransformation() {

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val source = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight)
        val bitmap = pool.get(source.width, source.height, Bitmap.Config.ARGB_8888)
            ?: Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.shader = android.graphics.BitmapShader(source, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP)
        val path = Path()
        path.addRoundRect(RectF(0f, 0f, source.width.toFloat(), source.height.toFloat()), radius, radius, Path.Direction.CW)
        canvas.drawPath(path, paint)
        return bitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(("rounded_corners_$radius").toByteArray())
    }
}