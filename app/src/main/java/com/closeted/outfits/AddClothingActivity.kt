package com.closeted.outfits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.DataGenerator
import com.closeted.R
import com.closeted.closet.Closet
import com.closeted.closet.ClosetAdapter
import com.closeted.database.FirebaseReferences
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddClothingActivity : AppCompatActivity() {

    private val closetData: ArrayList<Closet> = ArrayList()
    private lateinit var closetRecyclerViewItem:RecyclerView
    private val firebase: FirebaseReferences = FirebaseReferences()
    private lateinit var closetAdapter: AddClothingAdapter
    private val selectedList: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outfit_add_clothing)

        closetRecyclerViewItem = findViewById(R.id.addClothesRecycler)

        val layoutManager = LinearLayoutManager(this)
        closetAdapter = AddClothingAdapter(closetData, selectedList)
        closetRecyclerViewItem.adapter = closetAdapter
        closetRecyclerViewItem.layoutManager = layoutManager

        val confirmBtn = findViewById<Button>(R.id.addClothesConfirmBtn)
        val outfitId = intent.getStringExtra("outfit_id")!!

        confirmBtn.setOnClickListener {
            if(selectedList.isNotEmpty()) {
                lifecycleScope.launch {
                    // check if add operation
                    if(outfitId.isEmpty()) {
                        val asyncJob = async { firebase.insertOutfit(selectedList) }
                        asyncJob.await()
                        finish()
                    }
                    // else is edit
                    else {
                        val asyncJob = async { firebase.updateAddOutfit(outfitId, selectedList) }
                        asyncJob.await()
                        finish()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        firebase.getAllClothes(closetData, closetAdapter)
    }
}