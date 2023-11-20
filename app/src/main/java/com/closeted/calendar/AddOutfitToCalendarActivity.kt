package com.closeted.calendar

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.DataGenerator
import com.closeted.R
import com.closeted.database.FirebaseReferences
import com.closeted.outfits.Outfit
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddOutfitToCalendarActivity : AppCompatActivity() {
    private var outfitData: ArrayList<Outfit> = ArrayList()
    private lateinit var outfitRecyclerViewItem: RecyclerView
    private val firebase: FirebaseReferences = FirebaseReferences()
    private lateinit var outfitAdapter: OutfitPickerAdapter

    companion object {
        private const val CALENDAR_DATE_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_add_outfit)

        outfitRecyclerViewItem = findViewById(R.id.selectOutfitRecycler)
        val layoutManager = LinearLayoutManager(this)
        outfitAdapter = OutfitPickerAdapter(outfitData, this)
        outfitRecyclerViewItem.adapter = outfitAdapter
        outfitRecyclerViewItem.layoutManager = layoutManager

        lifecycleScope.launch {
            val asyncJob = async { outfitData = firebase.getAllOutfits() }
            asyncJob.await()
            outfitAdapter.setData(outfitData)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CALENDAR_DATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

}