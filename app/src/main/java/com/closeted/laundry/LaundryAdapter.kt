package com.closeted.laundry
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.closet.Closet
import com.closeted.closet.ClosetViewHolder
import com.closeted.closet.Clothing
import com.closeted.closet.ClothingAdapter
import com.closeted.closet.EditMode
import kotlinx.coroutines.CoroutineScope


class LaundryAdapter (private val data: ArrayList<Closet>, private val coroutineScope: CoroutineScope): RecyclerView.Adapter<ClosetViewHolder>(), ClothingAdapter.ClothingSelectionListener {
    private val viewPool = RecyclerView.RecycledViewPool()
    var editMode: EditMode = EditMode.NORMAL

    var childItemAdapter: ClothingAdapter
    private var selectedClothing: ArrayList<Clothing> = ArrayList()
    init {
        // Initialize the child adapter here (if needed)
        // For example, you can set it as non-editable initially
        this.childItemAdapter = ClothingAdapter(ArrayList(), editMode, this, coroutineScope)
    }

    fun setCheckboxVisibility(editMode: EditMode) {
        this.editMode = editMode
        childItemAdapter.setEditMode(editMode)
    }

    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ClosetViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.activity_closet_section, parent, false
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
        childItemAdapter = ClothingAdapter(parentItem.clothing, this.editMode, this, coroutineScope)
        holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = childItemAdapter
        holder.childRecyclerView.setRecycledViewPool(viewPool)
    }

    fun toggleSelectMode() {
        if(this.editMode != EditMode.SELECT){
            this.editMode = EditMode.SELECT
        }
        else{
            this.editMode = EditMode.NORMAL
            clearSelection()
        }

        this.childItemAdapter.setEditMode(this.editMode)
        notifyDataSetChanged()
    }

    override fun onItemSelectionChanged(item: Clothing, isSelected: Boolean) {
        if (isSelected) {
            selectedClothing.add(item)
        } else {
            selectedClothing.remove(item)
        }
    }

    fun getSelectedClothing(): ArrayList<Clothing> {
        Log.d("selectedClothing", selectedClothing.size.toString())
        return selectedClothing
    }

    fun clearSelection() {
        selectedClothing.clear()
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

