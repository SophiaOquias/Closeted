package com.closeted.calendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.DataGenerator
import com.closeted.R
import com.closeted.outfits.Outfit
import com.closeted.outfits.OutfitParentAdapter

class AddOutfitToCalendarActivity : AppCompatActivity() {
    private val outfitData: ArrayList<Outfit> = DataGenerator.generateOutfitData()
    private lateinit var outfitRecyclerViewItem: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_add_outfit)

        outfitRecyclerViewItem = findViewById(R.id.selectOutfitRecycler)
        val layoutManager = LinearLayoutManager(this)
        val outfitAdapter = OutfitParentAdapter(outfitData)
        outfitRecyclerViewItem.adapter = outfitAdapter
        outfitRecyclerViewItem.layoutManager = layoutManager

        val confirmBtn = findViewById<Button>(R.id.selectOutfitConfirmBtn)

        confirmBtn.setOnClickListener {
            finish()
        }
    }
}