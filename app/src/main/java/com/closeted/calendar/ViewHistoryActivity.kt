package com.closeted.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.database.FirebaseReferences
import com.closeted.database.History
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ViewHistoryActivity : AppCompatActivity() {

    private var historyData: ArrayList<History> = ArrayList()
    private val firebase: FirebaseReferences = FirebaseReferences()
    private lateinit var historyAdapter: HistoryParentAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_history)

        val historyRecyclerView = findViewById<RecyclerView>(R.id.calendarParentRecycler)
        val layoutManager = LinearLayoutManager(this)
        historyAdapter = HistoryParentAdapter(historyData, lifecycleScope)
        historyRecyclerView.adapter = historyAdapter
        historyRecyclerView.layoutManager = layoutManager

        lifecycleScope.launch {
            val asyncJob = async {
                historyData = firebase.getHistoricOutfits()
            }
            asyncJob.await()
            historyAdapter.setData(historyData)
        }
    }
}