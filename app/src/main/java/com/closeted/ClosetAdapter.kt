package com.closeted
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageButton
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


public class ClosetAdapter (private val data: ArrayList<Closet>): RecyclerView.Adapter<ClosetViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    var isEditMode: Boolean = false

    private var childItemAdapter: ClothingAdapter? = null

    init {
        // Initialize the child adapter here (if needed)
        // For example, you can set it as non-editable initially
        childItemAdapter = ClothingAdapter(ArrayList(), false)
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
        val parentItem = data[position]
        holder.parentItemTitle.text = parentItem.section

        val layoutManager = GridLayoutManager(
            holder.childRecyclerView.context,
            3,  // Number of columns in the grid
            GridLayoutManager.VERTICAL,
            false
        )

        layoutManager.initialPrefetchItemCount = parentItem.clothing.size

        val childItemAdapter = ClothingAdapter(parentItem.clothing, false)
        holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = childItemAdapter
        holder.childRecyclerView.setRecycledViewPool(viewPool)

    }

    fun toggleEditMode() {
        isEditMode = !isEditMode

        if(isEditMode){
            for(i in data){
                for(j in i.clothing){
                    j.isEditMode = !j.isEditMode
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }
}

