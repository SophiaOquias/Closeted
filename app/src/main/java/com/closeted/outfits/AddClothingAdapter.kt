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
import com.closeted.closet.ClothingAdapter
import com.closeted.closet.EditMode


class AddClothingAdapter (private val data: ArrayList<Closet>, private val selectedList: ArrayList<String>): RecyclerView.Adapter<ClosetViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    private var childItemAdapter: ClothingAdapter? = null

    init {
        // Initialize the child adapter here (if needed)
        // For example, you can set it as non-editable initially
        childItemAdapter = ClothingAdapter(ArrayList(), EditMode.NORMAL)
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
        //parent item refers to section of clothes (tops, skirts, dresses, trousers, etc.)
        val parentItem = data[position]
        holder.parentItemTitle.text = parentItem.section

        // Check if all clothes under the parent item are in the laundry
        val clothesNotInLaundry = parentItem.clothing.filter { !it.laundry }

        // If all clothes are in the laundry, skip binding data for this parent item
        if (clothesNotInLaundry.isEmpty()) {
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

        //layoutManager.initialPrefetchItemCount = parentItem.clothing.size
        layoutManager.initialPrefetchItemCount = clothesNotInLaundry.size
        val clothesNotInLaundryList = ArrayList(clothesNotInLaundry)
        val childItemAdapter = AddClothingChildAdapter(clothesNotInLaundryList, false, selectedList)

        holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = childItemAdapter
        holder.childRecyclerView.setRecycledViewPool(viewPool)

    }

    override fun getItemCount(): Int {
        return data.size
    }
}
