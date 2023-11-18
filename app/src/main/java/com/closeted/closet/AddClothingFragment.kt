package com.closeted.closet

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.closeted.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val CAMERA_PERMISSION_CODE = 1
private const val GALLERY_REQUEST_CODE = 2

/**
 * A simple [Fragment] subclass.
 * Use the [AddClothingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddClothingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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
    private lateinit var image: ImageView
    private var currentPhotoUri: Uri? = null
    private var notes: String? = null
    private var type: String? = null
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    // this basically just asks user for permission to use camera
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                // Permission granted, proceed with the camera action
                openCamera()
            } else {
                Toast.makeText(
                    requireContext(),
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
                    this.image = requireView().findViewById(R.id.addImage)
                    this.image.setImageURI(currentPhotoUri)
                } else {
                    Toast.makeText(
                        requireContext(),
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
                        this.image = requireView().findViewById(R.id.addImage)
                        this.image.setImageURI(it)
                    }
                }
            }
        }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
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
                requireContext(),
                "com.closeted.closet.fileprovider",
                photoFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
            takePictureLauncher.launch(intent)
        }
        else {
            Toast.makeText(
                requireContext(),
                "Failed to create an image file",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // saves image captured from camera to local
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_clothing, container, false)

        if(ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }

        //backButton logic
        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener(View.OnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            val currentFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.closetView)
            if (currentFragment != null) {
                transaction.remove(currentFragment)
            }
            transaction.replace(R.id.frame, ClosetFragment()) // Replace with your destination fragment
            transaction.addToBackStack(null)
            transaction.commit()
        })

        //finishAdding Button logic
        val finishAddingButton = view.findViewById<ImageButton>(R.id.checkButton)
        finishAddingButton.setOnClickListener(View.OnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            val currentFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.closetView)
            if (currentFragment != null) {
                transaction.remove(currentFragment)
            }

            this.type = spinner.selectedItem.toString()
            this.notes = view.findViewById<EditText>(R.id.etNotes).text.toString()

            // FIREBASE FIRESTORE TEST
            val db = Firebase.firestore

            val clothing = hashMapOf(
                "clothingImg" to "$currentPhotoUri",
                "type" to "$type",
                "notes" to "$notes"
            )

            Log.d("TEST", "Adding item: $currentPhotoUri, $type, $notes")

            db.collection("clothes")
                .add(clothing)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    Log.d("TEST", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

            transaction.replace(R.id.frame, ClosetFragment()) // Replace with your destination fragment
            transaction.addToBackStack(null)
            transaction.commit()
        })

        //Adding clothing types to the spinner (drop-down list)
        spinner = view.findViewById<Spinner>(R.id.spinner_clothing_type)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, clothingTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val cameraBtn = view.findViewById<ImageButton>(R.id.cameraButton)

        cameraBtn.setOnClickListener {
            checkCameraPermission()
        }

        val galleryBtn = view.findViewById<ImageButton>(R.id.galleryButton)

        galleryBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryLauncher.launch(intent)
        }

        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == CAMERA_PERMISSION_CODE) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
            else {
                Toast.makeText(
                    requireContext(),
                    "You denied permission for camera, you can still allow it in settings",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK) {
            val selected: Uri = data!!.data!!
            this.image = requireView().findViewById(R.id.addImage)
            this.image.setImageURI(selected)
        }
        else if (requestCode == takePictureLauncher.hashCode() && currentPhotoUri != null) {
            this.image = requireView().findViewById(R.id.addImage)
            this.image.setImageURI(currentPhotoUri)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddClothingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddClothingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}