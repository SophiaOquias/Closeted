package com.closeted.closet
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.closet.OpenClothingItem

class ClothingAdapter(private val data: ArrayList<Clothing>, private val laundryView: Boolean): RecyclerView.Adapter<ClothingViewHolder>() {
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

        holder.bindData(data[position], laundryView)

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
            if(data[position].selectMode == false){
                val context = holder.itemView.context
                val clickedClothing = data[position]
                val intent = Intent(context, OpenClothingItem::class.java)

                intent.putExtra("image_url", clickedClothing.id)
                intent.putExtra("clothing_type", clickedClothing.type)

                context.startActivity(intent)
            }
            else{
                checkBool = !checkBool
                checkButton.isChecked = checkBool

            }

        }


    }

    override fun getItemCount(): Int {
        return data.size
    }
}