package com.closeted.closet

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.closeted.R
import com.closeted.database.FirebaseReferences
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OpenClothingItem : AppCompatActivity() {

    private val CAMERA_PERMISSION_CODE = 1

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
    private lateinit var clothingImageView: ImageView
    private var currentPhotoUri: Uri? = null


    // this basically just asks user for permission to use camera
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, proceed with the camera action
                openCamera()
            } else {
                Toast.makeText(
                    this,
                    "Please allow this app to access the camera",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    // gets result from taking a picture and sets the thumbnail to the captured image and gets URI of image
    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (currentPhotoUri != null) {
                    this.clothingImageView = findViewById(R.id.clothingImage)
                    this.clothingImageView.setImageURI(currentPhotoUri)
                } else {
                    Toast.makeText(
                        this,
                        "Failed to retrieve the photo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    // gets the result from picking an image from photo gallery and updates image thumbnail and gets URI of image
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    val selected: Uri? = data.data
                    selected?.let {
                        this.clothingImageView = findViewById(R.id.clothingImage)
                        this.clothingImageView.setImageURI(it)
                        this.currentPhotoUri = it
                    }
                }
            }
        }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile: File? = createImageFile()
        if (photoFile != null) {
            currentPhotoUri = FileProvider.getUriForFile(
                this,
                "com.closeted.closet.fileprovider",
                photoFile
            )
            Log.d("ASDF", "Saved to Uri $currentPhotoUri")
            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
            takePictureLauncher.launch(intent)
        }
        else {
            Toast.makeText(
                this,
                "Failed to create an image file",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // saves image captured from camera to local
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

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
        clothingImageView = findViewById(R.id.clothingImage)
        Picasso.get().load(viewedClothing.imageUrl).into(clothingImageView)

        val clothingTypeView: TextView = findViewById(R.id.itemType)
        clothingTypeView.text = viewedClothing.type

        val editNotes: EditText = findViewById(R.id.clothingNotes)
        editNotes.setText(viewedClothing.notes)

        //Edit Button
        val editButton = findViewById<ImageButton>(R.id.editButton)
        editButton.setOnClickListener {
            val changeImage: LinearLayout = findViewById(R.id.changeImage)
            val clothingTypeOptions: Spinner = findViewById(R.id.spinner_clothing_type)

            if (isEditMode) {
                // Save changes logic
                changeImage.visibility = View.GONE
                editNotes.isEnabled = false
                clothingTypeOptions.visibility = View.GONE

                var imageUrl = viewedClothing.imageUrl

                // using global scope so that if user clicks the back button, image upload persists
                GlobalScope.launch {
                    if(currentPhotoUri != null) {
                        val asyncJob = async {
                            imageUrl = firebase.uploadImageToFirebaseStorage(currentPhotoUri!!)
                        }

                        asyncJob.await()
                    }

                    val edits = Clothing(
                        viewedClothing.id,
                        imageUrl,
                        clothingTypeOptions.selectedItem.toString(),
                        editNotes.text.toString(),
                        viewedClothing.laundry
                    )

                    if(viewedClothing != edits) {
                        firebase.updateClothing(edits)
                    }
                }


            } else {
                // Edit mode logic
                // TODO: add functionality to change image
                changeImage.visibility = View.VISIBLE
                editNotes.isEnabled = true
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
                val deletionJob = async { firebase.deleteClothing(viewedClothing) }
                deletionJob.await()
                finish()
            }
        }

        val cameraBtn = findViewById<ImageButton>(R.id.cameraButton)

        cameraBtn.setOnClickListener {
            checkCameraPermission()
        }

        val galleryBtn = findViewById<ImageButton>(R.id.galleryButton)

        galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == CAMERA_PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
            else {
                Toast.makeText(
                    this,
                    "You denied permission for camera, you can still allow it in settings",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && data != null) {
            val selected: Uri = data.data!!
            this.clothingImageView.setImageURI(selected)
        }
        else if (requestCode == takePictureLauncher.hashCode() && currentPhotoUri != null) {
            this.clothingImageView.setImageURI(currentPhotoUri)
        }
    }
}