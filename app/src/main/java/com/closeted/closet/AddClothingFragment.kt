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
import android.graphics.Bitmap
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.closeted.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val CAMERA_PERMISSION_CODE = 1
private const val CAMERA_REQUEST_CODE = 2

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

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

    private val takePictureLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val captured: Bitmap? = data?.extras?.get("data") as Bitmap?
                captured?.let { bitmap ->
                    val image: ImageView = requireView().findViewById(R.id.addImage)
                    image.setImageBitmap(bitmap)
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
        takePictureLauncher.launch(intent)
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
            transaction.replace(R.id.frame, ClosetFragment()) // Replace with your destination fragment
            transaction.addToBackStack(null)
            transaction.commit()
        })

        //Adding clothing types to the spinner (drop-down list)
        val spinner = view.findViewById<Spinner>(R.id.spinner_clothing_type)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, clothingTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val cameraBtn = view.findViewById<ImageButton>(R.id.cameraButton)

        cameraBtn.setOnClickListener {
            checkCameraPermission()
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == CAMERA_REQUEST_CODE) {
                val captured: Bitmap = data!!.extras!!.get("data") as Bitmap
                val image: ImageView = requireView().findViewById(R.id.addImage)
                image.setImageBitmap(captured)
            }
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