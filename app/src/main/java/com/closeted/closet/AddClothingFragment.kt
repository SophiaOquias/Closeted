package com.closeted.closet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import com.closeted.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_clothing, container, false)

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
        val finishAddingButton = view.findViewById<ImageButton>(R.id.editButton)
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

        return view

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