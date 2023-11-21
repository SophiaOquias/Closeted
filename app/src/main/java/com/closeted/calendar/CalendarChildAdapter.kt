package com.closeted.calendar

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.closet.Clothing
import com.closeted.outfits.ViewOutfitActivity
import com.squareup.picasso.Picasso

class CalendarChildAdapter(private val data: List<Clothing>, private val calendarId: String, private val outfitId: String) : RecyclerView.Adapter<CalendarChildAdapter.ViewHolder>() {
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val imageView: ImageView = itemView.findViewById(R.id.calendarClothingIv)
        fun bind(childItem: Clothing) {
            Picasso.get().load(childItem.imageUrl).into(imageView)
            if(childItem.laundry) {
                val colorFilter = PorterDuffColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY)
                imageView.colorFilter = colorFilter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.calendar_clothing, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ViewCalendarOutfitActivity::class.java)
            intent.putExtra("calendar_id", calendarId)
            intent.putExtra("outfit_id", outfitId)
            holder.itemView.context.startActivity(intent)
        }
    }
}