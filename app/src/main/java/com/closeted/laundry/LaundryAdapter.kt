package com.closeted.laundry
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.closet.Closet
import com.closeted.closet.ClosetViewHolder
import com.closeted.closet.ClothingAdapter
import com.closeted.closet.EditMode


public class LaundryAdapter (private val data: ArrayList<Closet>): RecyclerView.Adapter<ClosetViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    var editMode: EditMode = EditMode.NORMAL

    var childItemAdapter: ClothingAdapter? = null


    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ClosetViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.activity_closet_section,
                parent,
                false
            )

        return ClosetViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: ClosetViewHolder, position: Int) {
        val parentItem = data[position]
        holder.parentItemTitle.text = parentItem.section

        val layoutManager = GridLayoutManager(
            holder.childRecyclerView.context,
            3,  // Number of columns in the grid
            GridLayoutManager.VERTICAL,
            false
        )

        layoutManager.initialPrefetchItemCount = parentItem.clothing.size

        //val
        childItemAdapter = ClothingAdapter(parentItem.clothing, this.editMode)
        holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = childItemAdapter
        holder.childRecyclerView.setRecycledViewPool(viewPool)
    }

    fun toggleSelectMode() {
        if(this.editMode == EditMode.NORMAL){
            this.editMode = EditMode.SELECT
        }
        else{
            this.editMode = EditMode.NORMAL
        }
        notifyDataSetChanged()

        // Set the edit mode for the child adapter if needed
        childItemAdapter?.setEditMode(this.editMode)
    }

    fun toggleSelectAllMode() {
        if(this.editMode == EditMode.NORMAL){
            this.editMode = EditMode.SELECT_ALL
        }
        else{
            this.editMode = EditMode.NORMAL
        }
        notifyDataSetChanged()

        // Set the edit mode for the child adapter if needed
        childItemAdapter?.setEditMode(this.editMode)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

