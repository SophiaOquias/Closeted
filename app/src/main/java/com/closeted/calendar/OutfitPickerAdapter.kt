package com.closeted.calendar

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.outfits.Outfit
import com.closeted.outfits.OutfitChildAdapter

class OutfitPickerAdapter(private var data: ArrayList<Outfit>, private val activity: Activity) : RecyclerView.Adapter<OutfitPickerAdapter.ViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()

    companion object {
        private const val CALENDAR_DATE_REQUEST_CODE = 1
    }
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

        val childItemAdapter = OutfitPickerChildAdapter(collection.clothingItems, activity, collection.id)
        holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = childItemAdapter
        holder.childRecyclerView.setRecycledViewPool(viewPool)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CalendarOutfitSetDate::class.java)
            intent.putExtra("outfit_id", collection.id)
            startActivityForResult(activity, intent, CALENDAR_DATE_REQUEST_CODE, null)
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    fun setData(newData: ArrayList<Outfit>) {
        data.clear()
        data = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        //val binding = HorizontalOutfitsBinding.bind(itemView)
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.outfit_img)
    }

}