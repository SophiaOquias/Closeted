package com.closeted.closet

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.closeted.R

class ClosetViewHolder(itemView:View): ViewHolder(itemView) {
    val parentItemTitle: TextView = itemView.findViewById(R.id.clothing_type)
    val childRecyclerView: RecyclerView = itemView.findViewById(R.id.clothingRecycler)

}
