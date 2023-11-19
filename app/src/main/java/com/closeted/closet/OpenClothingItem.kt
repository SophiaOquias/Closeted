package com.closeted.closet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.closeted.R
import com.closeted.database.FirebaseReferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class OpenClothingItem : AppCompatActivity() {

    private var clothingTypes =arrayOf(
        "Blouse",
        "Dress",
        "Hoodie",
        "Jeans",
        "Jacket",
        "Pants",
        "Shirt",
        "Skirt",
        "Sweater",
        "T-Shirt"
    )

    private var isEditMode = false
    private val firebase: FirebaseReferences = FirebaseReferences()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothing_item_view)


        // Get the intent that started this activity
        val intent = intent

        var viewedClothing = Clothing(
            intent.getStringExtra("id")!!,
            intent.getStringExtra("image_url")!!,
            intent.getStringExtra("clothing_type")!!,
            intent.getStringExtra("notes"),
            intent.getBooleanExtra("laundry", false)
        )

        Log.d("TEST", "loading image from ${viewedClothing.imageUrl}")

        // Load the image into an ImageView
        val clothingImageView: ImageView = findViewById(R.id.clothingImage)
        Picasso.get().load(viewedClothing.imageUrl).into(clothingImageView)

        val clothingTypeView: TextView = findViewById(R.id.itemType)
        clothingTypeView.text = viewedClothing.type

        //Edit Button
        val editButton = findViewById<ImageButton>(R.id.editButton)
        editButton.setOnClickListener {
            val changeImage: LinearLayout = findViewById(R.id.changeImage)
            val editNotes: EditText = findViewById(R.id.clothingNotes)
            val outfitInfo: LinearLayout = findViewById(R.id.outfitInfo)
            val clothingTypeOptions: Spinner = findViewById(R.id.spinner_clothing_type)

            if (isEditMode) {
                // Save changes logic
                changeImage.visibility = View.GONE
                editNotes.isEnabled = false
                outfitInfo.visibility = View.VISIBLE
                clothingTypeOptions.visibility = View.GONE

                // TODO: Save changes to your data model
                //Need notes in datagenerator per clothing item
            } else {
                // Edit mode logic
                // TODO: add functionality to change image
                changeImage.visibility = View.VISIBLE
                editNotes.isEnabled = true
                outfitInfo.visibility = View.GONE
                clothingTypeOptions.visibility = View.VISIBLE

                // Create an ArrayAdapter using the string array and a default spinner layout
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, clothingTypes)
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                clothingTypeOptions.adapter = adapter

                val defaultClothingTypePosition = clothingTypes.indexOf(viewedClothing.type)
                if (defaultClothingTypePosition != -1) {
                    clothingTypeOptions.setSelection(defaultClothingTypePosition)
                }
            }

            // Toggle edit mode
            isEditMode = !isEditMode

        }

        // Delete button
        val deleteButton = findViewById<ImageButton>(R.id.trashButton)
        deleteButton.setOnClickListener {

            lifecycleScope.launch {
                val deletionJob = async { firebase.deleteClothingById(viewedClothing.id) }
                deletionJob.await()
                finish()
            }
        }
    }
}