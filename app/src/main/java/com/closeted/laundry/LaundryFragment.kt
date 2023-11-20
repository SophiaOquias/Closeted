package com.closeted.laundry

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.closet.Closet

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LaundryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LaundryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val laundryData: ArrayList<Closet> = ArrayList()
    private var isSelectAllMode = false

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
        //return inflater.inflate(R.layout.fragment_laundry, container, false)
        val view = inflater.inflate(R.layout.fragment_laundry, container, false)

        val laundryRecyclerViewItem = view.findViewById<RecyclerView>(R.id.laundryRecycler)

        val layoutManager = LinearLayoutManager(requireContext())

        val laundryAdapter = LaundryAdapter(laundryData)

        laundryRecyclerViewItem.adapter = laundryAdapter
        laundryRecyclerViewItem.layoutManager = layoutManager

        val selectAllButton = view.findViewById<Button>(R.id.selectAllButton)
        val selectButton = view.findViewById<Button>(R.id.selectButton)
        val addToClosetButton = view.findViewById<Button>(R.id.addToCloset)


        selectAllButton.setOnClickListener(View.OnClickListener {
            laundryAdapter.toggleSelectAllMode()

        })

        selectButton.setOnClickListener(View.OnClickListener {
            laundryAdapter.toggleSelectMode()
        })

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LaundryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LaundryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}