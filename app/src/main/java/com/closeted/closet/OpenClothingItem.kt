package com.closeted.closet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import com.closeted.R

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clothing_item_view)


        // Get the intent that started this activity
        val intent = intent

        val imageUrl = intent.getIntExtra("image_url", 0)
        val clothing_type = intent.getStringExtra("clothing_type")

        // Load the image into an ImageView
        val clothingImageView: ImageView = findViewById(R.id.clothingImage)
        clothingImageView.setImageResource(imageUrl)

        val clothingTypeView: TextView = findViewById(R.id.itemType)
        clothingTypeView.setText(clothing_type)

        //Back Button
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        //Edit Button
        val editButton = findViewById<ImageButton>(R.id.editButton)
        editButton.setOnClickListener(View.OnClickListener {
            val changeImage: LinearLayout = findViewById(R.id.changeImage)
            val editNotes: EditText = findViewById(R.id.clothingNotes)
            val outfitInfo: LinearLayout = findViewById(R.id.outfitInfo)
            val clothingTypeOptions: Spinner = findViewById(R.id.spinner_clothing_type)

            /*
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

            val defaultClothingTypePosition = clothingTypes.indexOf(clothing_type)
            if (defaultClothingTypePosition != -1) {
                clothingTypeOptions.setSelection(defaultClothingTypePosition)
            }

             */

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

                val defaultClothingTypePosition = clothingTypes.indexOf(clothing_type)
                if (defaultClothingTypePosition != -1) {
                    clothingTypeOptions.setSelection(defaultClothingTypePosition)
                }
            }

            // Toggle edit mode
            isEditMode = !isEditMode

        })
    }
}