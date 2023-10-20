package com.closeted

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ImageViewHolder(itemView: View): ViewHolder(itemView) {
    private val iv: ImageView = itemView.findViewById(R.id.clothingIv)
    fun bindData(imageId: Int) {
        iv.setImageResource(imageId)
    }
}