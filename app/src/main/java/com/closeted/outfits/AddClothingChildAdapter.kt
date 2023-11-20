package com.closeted.outfits
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.closet.Clothing
import com.closeted.closet.ClothingViewHolder
import com.closeted.closet.EditMode
import com.closeted.closet.OpenClothingItem

class AddClothingChildAdapter(private val data: ArrayList<Clothing>, private val laundryView: Boolean): RecyclerView.Adapter<ClothingViewHolder>() {
    private var checkBool = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.clothing_cell, parent, false)
        return ClothingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        if (data[position].laundry && !laundryView) {
            // Do not bind laundry items for closet view
            return
        }

        holder.bindData(data[position], EditMode.SELECT)

        val xBtn = holder.itemView.findViewById<ImageButton>(R.id.trashButton)
        if(xBtn.isEnabled){
            xBtn.setOnClickListener {
                val itemPosition = holder.adapterPosition
                data.removeAt(itemPosition)
                notifyItemRemoved(itemPosition)
                notifyDataSetChanged()
            }
        }

        val checkButton = holder.itemView.findViewById<CheckBox>(R.id.selectOption)

        val clothing = holder.itemView.findViewById<ImageView>(R.id.imageView)

        clothing.setOnClickListener {

            // TODO: select clothes to add in outfit and place in a list

        }


    }

    override fun getItemCount(): Int {
        return data.size
    }
}