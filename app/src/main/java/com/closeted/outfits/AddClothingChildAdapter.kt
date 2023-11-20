package com.closeted.outfits
import android.util.Log
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

class AddClothingChildAdapter(private val data: ArrayList<Clothing>, private val laundryView: Boolean, private val selectedClothingIds: ArrayList<String>): RecyclerView.Adapter<ClothingViewHolder>() {
    private var checkBool = false
    private var editMode = EditMode.SELECT

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
        checkButton.isClickable = this.editMode == EditMode.SELECT
        val clothing = holder.itemView.findViewById<ImageView>(R.id.imageView)
        clothing.setOnClickListener {
            // TODO: select clothes to add in outfit and place in a list
            if (editMode == EditMode.SELECT) {
                checkBool = !checkBool
                checkButton.isChecked = checkBool
                val clothingId = data[position].id

                if (checkBool) {
                    selectedClothingIds.add(clothingId)
                    Log.d("SelectedID", "ClothingID: $clothingId")
                }
                else {
                    selectedClothingIds.remove(clothingId)
                }
            }
            Log.d("SelectedIDList", "ClothingIDList: $selectedClothingIds")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}