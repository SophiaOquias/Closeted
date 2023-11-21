package com.closeted.closet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import kotlinx.coroutines.CoroutineScope


public class ClosetAdapter (private val data: ArrayList<Closet>, private val coroutineScope: CoroutineScope): RecyclerView.Adapter<ClosetViewHolder>(), ClothingAdapter.ClothingSelectionListener {
    private val viewPool = RecyclerView.RecycledViewPool()
    var editMode: EditMode = EditMode.NORMAL
    private var childItemAdapter: ClothingAdapter
    private var selectedClothing: ArrayList<Clothing> = ArrayList()

    init {
        // Initialize the child adapter here (if needed)
        // For example, you can set it as non-editable initially
        this.childItemAdapter = ClothingAdapter(ArrayList(), this.editMode, this, coroutineScope)
    }

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
        holder.parentItemTitle.text = data[position].section

        // If there are no longer clothes in the section, skip binding data for this parent item
        if (data[position].clothing.isEmpty()) {
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
            return
        } else {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val layoutManager = GridLayoutManager(
            holder.childRecyclerView.context,
            3,  // Number of columns in the grid
            GridLayoutManager.VERTICAL,
            false
        )

        layoutManager.initialPrefetchItemCount = data[position].clothing.size
        this.childItemAdapter = ClothingAdapter(data[position].clothing, this.editMode, this, coroutineScope)

        holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = childItemAdapter
        holder.childRecyclerView.setRecycledViewPool(viewPool)

    }

    fun toggleEditMode() {
        if(this.editMode != EditMode.DELETE){
            this.editMode = EditMode.DELETE
        }
        else{
            this.editMode = EditMode.NORMAL
        }

        this.childItemAdapter.setEditMode(this.editMode)
        notifyDataSetChanged()

    }

    fun toggleSelectMode() {
        if(this.editMode != EditMode.SELECT){
            this.editMode = EditMode.SELECT
        }
        else{
            this.editMode = EditMode.NORMAL
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

