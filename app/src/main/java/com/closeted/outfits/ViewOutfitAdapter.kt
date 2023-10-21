package com.closeted.outfits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.closeted.R

class ViewOutfitAdapter(private val data: ArrayList<Int>): Adapter<OutfitImageViewHolder>() {
    var isEditModeEnabled = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutfitImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.view_item_layout, parent, false)
        return OutfitImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: OutfitImageViewHolder, position: Int) {

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