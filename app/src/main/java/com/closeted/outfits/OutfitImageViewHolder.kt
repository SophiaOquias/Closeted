package com.closeted.outfits

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.closeted.R
import com.closeted.closet.Clothing
import com.squareup.picasso.Picasso

class OutfitImageViewHolder(itemView: View): ViewHolder(itemView) {
    private val iv: ImageView = itemView.findViewById(R.id.clothingIv)
    fun bindData(clothing: Clothing) {
        Picasso.get().load(clothing.imageUrl).into(iv)
    }
}