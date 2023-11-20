package com.closeted.closet

import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.closeted.R
import com.squareup.picasso.Picasso

class ClothingViewHolder(itemView:View): ViewHolder(itemView) {
    private val img: ImageView = itemView.findViewById(R.id.imageView)
    private val deleteButton: ImageButton = itemView.findViewById(R.id.trashButton)
    private val selectButton: CheckBox = itemView.findViewById(R.id.selectOption)

    fun bindData(clothing: Clothing,  editMode: EditMode) {
//        img.setImageResource(clothing.imageId)
        Picasso.get().load(clothing.imageUrl).into(img)
        img.scaleType = ImageView.ScaleType.CENTER_CROP

        deleteButton.visibility = if (editMode == EditMode.DELETE) View.VISIBLE else View.GONE
        deleteButton.isClickable = editMode == EditMode.DELETE
        deleteButton.isEnabled = editMode == EditMode.DELETE

        selectButton.visibility = if (editMode == EditMode.SELECT || editMode == EditMode.SELECT_ALL) View.VISIBLE else View.GONE
        selectButton.isClickable =  (editMode == EditMode.SELECT || editMode == EditMode.SELECT_ALL)
        selectButton.isChecked =  editMode == EditMode.SELECT_ALL

    }
}

