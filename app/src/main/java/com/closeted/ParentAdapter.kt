package com.closeted

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.databinding.HorizontalOutfitsBinding

class ParentAdapter(private val data: ArrayList<ParentModel>) : RecyclerView.Adapter<ParentAdapter.ViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.horizontal_outfits,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val collection = data[position]
        val childItemAdapter = ChildAdapter(collection.clothingItems)
        //holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = childItemAdapter
        holder.childRecyclerView.setRecycledViewPool(viewPool)
        /*holder.binding.apply {
            val collection = data[position]
            val outfitAdapter = ChildAdapter(collection.clothingItems)
            outfitChildRv.adapter=outfitAdapter
        }*/
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        //val binding = HorizontalOutfitsBinding.bind(itemView)
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.outfitChild_rv)
    }

}