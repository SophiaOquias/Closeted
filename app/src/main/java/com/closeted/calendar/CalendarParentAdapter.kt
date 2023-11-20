package com.closeted.calendar

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.outfits.ViewOutfitActivity
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarParentAdapter(private var data: ArrayList<Calendar>): RecyclerView.Adapter<CalendarParentAdapter.ViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val dateTv: TextView = itemView.findViewById(R.id.calendarDateTv)
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.calendarChildRecycler)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.calendar_outfit_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(@NonNull holder: CalendarParentAdapter.ViewHolder, position: Int) {
        val layoutManager = LinearLayoutManager(
            holder.childRecyclerView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        holder.dateTv.text = formatTimestampToString(data[position].date)

        val outfits = data[position].outfit
        layoutManager.initialPrefetchItemCount = outfits.clothingItems.size

        val childItemAdapter = CalendarChildAdapter(outfits.clothingItems)
        holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = childItemAdapter
        holder.childRecyclerView.setRecycledViewPool(viewPool)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ViewCalendarOutfitActivity::class.java)
            holder.itemView.context.startActivity(intent)
        }
    }

    fun setData(newData: ArrayList<Calendar>) {
        data.clear()
        data = newData
        notifyDataSetChanged()
    }

    fun formatTimestampToString(timestamp: Timestamp): String {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val date = timestamp.toDate()
        return dateFormat.format(date)
    }
}