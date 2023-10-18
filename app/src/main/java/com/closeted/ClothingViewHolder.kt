package com.closeted

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ClothingViewHolder(itemView:View): ViewHolder(itemView) {
    private val img: ImageView = itemView.findViewById(R.id.imageView)

    fun bindData(clothing: Clothing) {
        img.setImageResource(clothing.imageId)
        img.scaleType = ImageView.ScaleType.CENTER_CROP
    }

}