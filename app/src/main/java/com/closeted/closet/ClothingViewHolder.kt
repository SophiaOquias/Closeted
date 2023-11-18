package com.closeted.closet

import android.graphics.Color
import android.view.View
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.closeted.R
import com.squareup.picasso.Picasso

class ClothingViewHolder(itemView:View): ViewHolder(itemView) {
    private val img: ImageView = itemView.findViewById(R.id.imageView)
    private val deleteButton: ImageButton = itemView.findViewById(R.id.trashButton)
    private val selectButton: CheckBox = itemView.findViewById(R.id.selectOption)

    fun bindData(clothing: Clothing, laundryView: Boolean) {
//        img.setImageResource(clothing.imageId)
        Picasso.get().load(clothing.imageUrl).into(img)
        img.scaleType = ImageView.ScaleType.CENTER_CROP

        if(laundryView.equals(false) && clothing.laundry){
            img.setColorFilter(Color.parseColor("#C8434141"))
        }

        // Set the visibility of the delete button based on the isEditMode flag
        if (clothing.isEditMode) {
            deleteButton.visibility = View.VISIBLE
            deleteButton.isClickable = true
            deleteButton.isEnabled = true
        } else {
            deleteButton.visibility = View.GONE
            deleteButton.isClickable = false
            deleteButton.isEnabled = false
        }

        // Set the visibility and checked state of the select button based on selectAllMode and selectMode.
        selectButton.visibility = if (clothing.selectAllMode || clothing.selectMode) View.VISIBLE else View.GONE
        selectButton.isClickable = clothing.selectMode

        // Check the select button if in selectAllMode.
        selectButton.isChecked = clothing.selectAllMode

    }
}

