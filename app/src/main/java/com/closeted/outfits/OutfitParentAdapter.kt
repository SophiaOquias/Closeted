package com.closeted.outfits

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R

class OutfitParentAdapter(private val data: ArrayList<Outfit>) : RecyclerView.Adapter<OutfitParentAdapter.ViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.outfits_horizontal_section,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        val collection = data[position]

        val layoutManager = LinearLayoutManager(
            holder.childRecyclerView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        layoutManager.initialPrefetchItemCount = collection.clothingItems.size

        val childItemAdapter = OutfitChildAdapter(collection.clothingItems)
        holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = childItemAdapter
        holder.childRecyclerView.setRecycledViewPool(viewPool)

    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        //val binding = HorizontalOutfitsBinding.bind(itemView)
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.outfit_img)
    }

}