package com.closeted.closet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.DataGenerator
import com.closeted.R


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClosetFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClosetFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val closetData: ArrayList<Closet> = DataGenerator.generateClosetData()

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

        val view = inflater.inflate(R.layout.fragment_closet, container, false)

        val closetRecyclerViewItem = view.findViewById<RecyclerView>(R.id.closetRecycler)

        val layoutManager = LinearLayoutManager(requireContext())

        val closetAdapter = ClosetAdapter(closetData)

        closetRecyclerViewItem.adapter = closetAdapter
        closetRecyclerViewItem.layoutManager = layoutManager

        val addButton = view.findViewById<ImageButton>(R.id.selectButton)
        addButton.setOnClickListener(View.OnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            val currentFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.closetView)
            if (currentFragment != null) {
                transaction.remove(currentFragment)
            }
            transaction.replace(R.id.frame, AddClothingFragment()) // Replace with your destination fragment
            transaction.addToBackStack(null)
            transaction.commit()
        })

        val editButton = view.findViewById<ImageButton>(R.id.editButton)
        editButton.setOnClickListener(View.OnClickListener {
            closetAdapter.toggleEditMode()
            //closetAdapter.notifyDataSetChanged()
        })

        val laundryButton = view.findViewById<ImageButton>(R.id.laundryButton)
        val addToLaundryButton = view.findViewById<Button>(R.id.addToLaundry)
        laundryButton.setOnClickListener(View.OnClickListener {
            val isSelectMode = !closetAdapter.selectMode
            // Iterate through the clothing items and set their selectMode based on isSelectMode.
            for (i in closetData.indices) {
                for (j in closetData[i].clothing.indices) {
                    if(closetData[i].clothing[j].laundry == false) {
                        closetData[i].clothing[j].selectMode = isSelectMode
                        addToLaundryButton.visibility =
                            if (closetData[i].clothing[j].selectAllMode || closetData[i].clothing[j].selectMode) View.VISIBLE else View.GONE
                    }
                }
            }
            // Notify the adapter to refresh the RecyclerView.
            closetAdapter.selectMode = isSelectMode
            closetAdapter.notifyDataSetChanged()
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
         * @return A new instance of fragment ClosetFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ClosetFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}