package com.closeted.calendar


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.database.FirebaseReferences
import com.closeted.database.History
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryParentAdapter(private var data: ArrayList<History>, private val lifecycleScope: LifecycleCoroutineScope): RecyclerView.Adapter<HistoryParentAdapter.ViewHolder>() {
    private val viewPool = RecyclerView.RecycledViewPool()
    private val firebase: FirebaseReferences = FirebaseReferences()

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val dateTv: TextView = itemView.findViewById(R.id.calendarDateTv)
        val childRecyclerView: RecyclerView = itemView.findViewById(R.id.calendarChildRecycler)
        val deleteButton: ImageButton = itemView.findViewById(R.id.trashButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.history_outfit_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(@NonNull holder: HistoryParentAdapter.ViewHolder, position: Int) {
        val layoutManager = LinearLayoutManager(
            holder.childRecyclerView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        holder.dateTv.text = formatTimestampToString(data[position].date)

        val history = data[position]
        layoutManager.initialPrefetchItemCount = history.imageUrls.size

        val childItemAdapter = HistoryChildAdapter(history.imageUrls)
        holder.childRecyclerView.layoutManager = layoutManager
        holder.childRecyclerView.adapter = childItemAdapter
        holder.childRecyclerView.setRecycledViewPool(viewPool)

        holder.deleteButton.setOnClickListener {
            lifecycleScope.launch {
                firebase.deleteHistoryById(history.id)
                data.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    fun setData(newData: ArrayList<History>) {
        data.clear()
        data = newData
        notifyDataSetChanged()
    }

    private fun formatTimestampToString(timestamp: Timestamp): String {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val date = timestamp.toDate()
        return dateFormat.format(date)
    }
}