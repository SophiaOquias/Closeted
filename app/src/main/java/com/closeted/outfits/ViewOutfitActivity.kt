package com.closeted.outfits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.closet.Clothing
import com.closeted.database.FirebaseReferences
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ViewOutfitActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ViewOutfitAdapter
    private lateinit var clothingList: ArrayList<Clothing>
    private lateinit var outfitId: String
    private val firebase: FirebaseReferences = FirebaseReferences()
    private lateinit var outfit: Outfit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_outfit)

        clothingList = ArrayList()
        outfitId = intent.getStringExtra("outfit_id")!!

        var initialCount = 0

        lifecycleScope.launch {
            val asyncJob = async {outfit = firebase.getOutfitById(outfitId)!!}
            asyncJob.await()
            adapter.setData(outfit.clothingItems)
            initialCount = outfit.clothingItems.size
        }


        this.recyclerView = findViewById(R.id.recyclerView)
        this.adapter = ViewOutfitAdapter(clothingList, outfitId)
        this.recyclerView.adapter = adapter
        this.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val editButton = findViewById<ImageButton>(R.id.viewEditBtn)
        val confirmButton = findViewById<Button>(R.id.viewConfirmBtn)
        val addButton = findViewById<FloatingActionButton>(R.id.viewAddBtn)
        val deleteButton = findViewById<ImageButton>(R.id.viewDeleteBtn)

        editButton.setOnClickListener {
            // Toggle the edit mode flag
            adapter.isEditModeEnabled = true
            adapter.notifyDataSetChanged()
            confirmButton.isVisible = true
            confirmButton.isEnabled = true
            addButton.isVisible = true
            addButton.isEnabled = true
            addButton.isClickable = true
            editButton.isEnabled = false
            editButton.isVisible = false
        }

        confirmButton.setOnClickListener {
            adapter.isEditModeEnabled = false
            adapter.notifyDataSetChanged()
            confirmButton.isVisible = false
            confirmButton.isEnabled = false
            addButton.isVisible = false
            addButton.isEnabled = false
            addButton.isClickable = false
            editButton.isEnabled = true
            editButton.isVisible = true

            if(initialCount != clothingList.size) {
                lifecycleScope.launch {
                    firebase.updateOutfit(outfitId, clothingList)
                }
            }

            // delete whole outfit if user deletes all clothes in the outfit
            if(clothingList.size == 0) {
                lifecycleScope.launch {
                    firebase.deleteOutfitById(outfitId)
                }
            }
        }

        deleteButton.setOnClickListener {
            lifecycleScope.launch {
                val asyncJob = async { firebase.deleteOutfitById(outfitId) }
                asyncJob.await()
                finish()
            }
        }
    }


}