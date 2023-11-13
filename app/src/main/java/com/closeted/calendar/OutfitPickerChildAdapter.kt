package com.closeted.calendar
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.closeted.closet.Clothing
import com.closeted.R

class OutfitPickerChildAdapter(private val data: List<Clothing>, private val activity: Activity) : RecyclerView.Adapter<OutfitPickerChildAdapter.ViewHolder>() {

    companion object {
        private const val CALENDAR_DATE_REQUEST_CODE = 1
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.outfit_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, CalendarOutfitSetDate::class.java)
            ActivityCompat.startActivityForResult(
                activity,
                intent,
                CALENDAR_DATE_REQUEST_CODE,
                null
            )
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val imageView: ImageView = itemView.findViewById(R.id.outfit_img)
        fun bind(childItem: Clothing) {
            imageView.setImageResource(childItem.imageId)
        }
    }
}