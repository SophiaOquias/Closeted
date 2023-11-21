package com.closeted.outfits
import android.view.LayoutInflater
import android.view.View
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


class AddClothingAdapter (private val data: ArrayList<Closet>, private val selectedList: ArrayList<String>, private val coroutineScope: CoroutineScope): RecyclerView.Adapter<ClosetViewHolder>(), ClothingAdapter.ClothingSelectionListener {
    private val viewPool = RecyclerView.RecycledViewPool()
    private var childItemAdapter: ClothingAdapter? = null

    init {
        // Initialize the child adapter here (if needed)
        // For example, you can set it as non-editable initially
        childItemAdapter = ClothingAdapter(ArrayList(), EditMode.NORMAL, this, coroutineScope)
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

    override fun onBindViewHolder(holder: ClosetViewHolder, position: Int) {
        //parent item refers to section of clothes (tops, skirts, dresses, trousers, etc.)
        val parentItem = data[position]
        holder.parentItemTitle.text = parentItem.section

        val layoutManager = GridLayoutManager(
            holder.childRecyclerView.context,
            3,  // Number of columns in the grid
            GridLayoutManager.VERTICAL,
            false
        )

        layoutManager.initialPrefetchItemCount = parentItem.clothing.size
        val childItemAdapter = AddClothingChildAdapter(parentItem.clothing, false, selectedList)

        holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = childItemAdapter
        holder.childRecyclerView.setRecycledViewPool(viewPool)

    }

    override fun onItemSelectionChanged(item: Clothing, isSelected: Boolean) {
        val selectedClothing = ArrayList<Clothing>()
        if (isSelected) {
            selectedClothing.add(item)
        } else {
            selectedClothing.remove(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

