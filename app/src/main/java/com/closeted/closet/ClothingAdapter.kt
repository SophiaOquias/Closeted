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
import com.closeted.database.FirebaseReferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ClothingAdapter(private val data: ArrayList<Clothing>, private var editMode: EditMode,private val selectionListener: ClothingSelectionListener,  private val coroutineScope: CoroutineScope): RecyclerView.Adapter<ClothingViewHolder>() {
    private var checkBool = false
    private var firebase: FirebaseReferences = FirebaseReferences()

    interface ClothingSelectionListener {
        fun onItemSelectionChanged(item: Clothing, isSelected: Boolean)
    }

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
                coroutineScope.launch {
                    val itemPosition = holder.adapterPosition
                    firebase.deleteClothing(data[itemPosition])
                    data.removeAt(itemPosition)
                    notifyItemRemoved(itemPosition)
                }
            }
        }

        val checkButton = holder.itemView.findViewById<CheckBox>(R.id.selectOption)
        checkButton.visibility = if (this.editMode == EditMode.SELECT) View.VISIBLE else View.GONE
        checkButton.isClickable = this.editMode == EditMode.SELECT

        checkButton.setOnCheckedChangeListener { _, isChecked ->
            selectionListener.onItemSelectionChanged(data[position], isChecked)
        }

        val clothing = holder.itemView.findViewById<ImageView>(R.id.imageView)
        clothing.setOnClickListener {
            if(editMode == EditMode.NORMAL){
                val context = holder.itemView.context
                val clickedClothing = data[position]
                val intent = Intent(context, OpenClothingItem::class.java)

                intent.putExtra("id", clickedClothing.id)
                intent.putExtra("image_url", clickedClothing.imageUrl)
                intent.putExtra("clothing_type", clickedClothing.type)
                intent.putExtra("notes", clickedClothing.notes)
                intent.putExtra("laundry", clickedClothing.laundry)

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