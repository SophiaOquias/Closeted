package com.closeted

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView.Adapter

class ViewOutfitAdapter(private val data: ArrayList<Int>): Adapter<ImageViewHolder>() {
    var isEditModeEnabled = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_item_layout, parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val xBtn = holder.itemView.findViewById<ImageButton>(R.id.viewRemoveBtn)
        xBtn.visibility = if (isEditModeEnabled) View.VISIBLE else View.INVISIBLE
        xBtn.isClickable = isEditModeEnabled

        xBtn.setOnClickListener {
            if (isEditModeEnabled) {
                val itemPosition = holder.adapterPosition
                data.removeAt(itemPosition)
                notifyItemRemoved(itemPosition)
            }
        }


        holder.bindData(data[position])
    }


}