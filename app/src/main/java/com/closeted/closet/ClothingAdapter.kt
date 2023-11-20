package com.closeted.closet
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R

class ClothingAdapter(private val data: ArrayList<Clothing>, private val laundryView: Boolean, private var editMode: EditMode): RecyclerView.Adapter<ClothingViewHolder>() {
    private var checkBool = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.clothing_cell, parent, false)
        return ClothingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        Log.d("ClothingAdapter", "Binding Edit Mode: " + this.editMode.toString())
        holder.bindData(data[position], this.editMode!!)

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
        checkButton.visibility = if (this.editMode == EditMode.SELECT) View.VISIBLE else View.GONE
        checkButton.isClickable = this.editMode == EditMode.SELECT

        val clothing = holder.itemView.findViewById<ImageView>(R.id.imageView)
        clothing.setOnClickListener {
            if(editMode==EditMode.NORMAL){
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

    fun setEditMode(mode: EditMode){
        this.editMode = mode
        Log.d("ClothingAdapter", this.editMode.toString())
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return data.size
    }

}