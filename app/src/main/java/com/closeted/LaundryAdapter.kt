package com.closeted
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


public class LaundryAdapter (private val data: ArrayList<Closet>): RecyclerView.Adapter<ClosetViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    var selectAllMode: Boolean = false
    var selectMode: Boolean = false

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
        childItemAdapter = ClothingAdapter(parentItem.clothing, true)
        holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = childItemAdapter
        holder.childRecyclerView.setRecycledViewPool(viewPool)
    }

    /*
    fun toggleSelectAllMode() {
        selectAllMode = !selectAllMode
        if(selectAllMode){
            for(i in data){
                for(j in i.clothing){
                    j.selectAllMode = !j.selectAllMode
                    childItemAdapter?.notifyDataSetChanged()
                }
            }
        }
    }



    fun toggleSelectMode() {
        selectMode = !selectMode
        for(closet in data){
            for(clothing in closet.clothing){
                clothing.selectMode = selectMode
            }
        }
        notifyDataSetChanged()
    }

     */

    override fun getItemCount(): Int {
        return data.size
    }
}

