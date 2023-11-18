package com.closeted.calendar

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.DataGenerator
import com.closeted.R
import com.closeted.outfits.Outfit

class AddOutfitToCalendarActivity : AppCompatActivity() {
//    private val outfitData: ArrayList<Outfit> = DataGenerator.generateOutfitData()
    private val outfitData: ArrayList<Outfit> = ArrayList()
    private lateinit var outfitRecyclerViewItem: RecyclerView

    companion object {
        private const val CALENDAR_DATE_REQUEST_CODE = 1
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_add_outfit)

        outfitRecyclerViewItem = findViewById(R.id.selectOutfitRecycler)
        val layoutManager = LinearLayoutManager(this)
        val outfitAdapter = OutfitPickerAdapter(outfitData, this)
        outfitRecyclerViewItem.adapter = outfitAdapter
        outfitRecyclerViewItem.layoutManager = layoutManager
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CALENDAR_DATE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            finish()
        }
    }

}