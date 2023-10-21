package com.closeted
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ChildAdapter(private val data: List<Clothing>) : RecyclerView.Adapter<ChildAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.outfit_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ViewOutfit::class.java)
            holder.itemView.context.startActivity(intent)
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