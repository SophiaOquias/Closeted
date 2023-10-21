package com.closeted.outfits

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.closeted.R

class OutfitImageViewHolder(itemView: View): ViewHolder(itemView) {
    private val iv: ImageView = itemView.findViewById(R.id.clothingIv)
    fun bindData(imageId: Int) {
        iv.setImageResource(imageId)
    }
}