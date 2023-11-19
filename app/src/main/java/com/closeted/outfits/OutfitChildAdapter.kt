package com.closeted.outfits
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.closeted.closet.Clothing
import com.closeted.R
import com.squareup.picasso.Picasso

class OutfitChildAdapter(private val data: List<Clothing>, private val outfitId: String) : RecyclerView.Adapter<OutfitChildAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.outfit_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, ViewOutfitActivity::class.java)
            intent.putExtra("outfit_id", outfitId)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val imageView: ImageView = itemView.findViewById(R.id.outfit_img)
        fun bind(childItem: Clothing) {
            Picasso.get().load(childItem.imageUrl).into(imageView)
        }
    }
}