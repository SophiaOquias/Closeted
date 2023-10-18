package com.closeted

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ClothingViewHolder(itemView:View): ViewHolder(itemView) {
    private val img: ImageView = itemView.findViewById(R.id.imageView)

    fun bindData(clothing: Clothing) {
        img.setImageResource(clothing.imageId)
        img.scaleType = ImageView.ScaleType.CENTER_CROP

    }

}