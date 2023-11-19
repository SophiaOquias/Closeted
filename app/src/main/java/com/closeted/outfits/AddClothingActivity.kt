package com.closeted.outfits

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.DataGenerator
import com.closeted.R
import com.closeted.closet.Closet
import com.closeted.closet.ClosetAdapter
import com.closeted.database.FirebaseReferences

class AddClothingActivity : AppCompatActivity() {

//    private val closetData: ArrayList<Closet> = DataGenerator.generateClosetData()
    private val closetData: ArrayList<Closet> = ArrayList()
    private lateinit var closetRecyclerViewItem:RecyclerView
    private val firebase: FirebaseReferences = FirebaseReferences()
    private lateinit var closetAdapter: AddClothingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_outfit_add_clothing)

        closetRecyclerViewItem = findViewById(R.id.addClothesRecycler)

        val layoutManager = LinearLayoutManager(this)
        closetAdapter = AddClothingAdapter(closetData)
        closetRecyclerViewItem.adapter = closetAdapter
        closetRecyclerViewItem.layoutManager = layoutManager

        val confirmBtn = findViewById<Button>(R.id.addClothesConfirmBtn)

        confirmBtn.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        firebase.getAllClothes(closetData, closetAdapter)
    }
}