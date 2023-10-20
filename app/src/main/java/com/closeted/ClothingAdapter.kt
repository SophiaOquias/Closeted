package com.closeted
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

public class ClothingAdapter(private val data: List<Clothing>, private val laundryView: Boolean): RecyclerView.Adapter<ClothingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClothingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.clothing_cell, parent, false)
        return ClothingViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClothingViewHolder, position: Int) {
        holder.bindData(data[position], laundryView)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}