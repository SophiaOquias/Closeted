package com.closeted.outfits

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.DataGenerator
import com.closeted.R
import com.closeted.closet.Closet
import com.closeted.closet.ClosetAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OutfitsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OutfitsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var outfitListRV: RecyclerView
    private val outfitData: ArrayList<Outfit> = DataGenerator.generateOutfitData()
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
        val view = inflater.inflate(R.layout.fragment_outfits, container, false)
        val outfitRecyclerViewItem = view.findViewById<RecyclerView>(R.id.outfitsList_rv)
        val layoutManager = LinearLayoutManager(requireContext())

        val outfitAdapter = OutfitParentAdapter(outfitData)
        outfitRecyclerViewItem.adapter = outfitAdapter
        outfitRecyclerViewItem.layoutManager = layoutManager

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn = view.findViewById<ImageButton>(R.id.addOutfit)
        val closetAdapter = ClosetAdapter(closetData)
        val addOutfitButton = view.findViewById<Button>(R.id.selectClothingforOutfit)

        btn.setOnClickListener {
            val intent = Intent(view.context, AddClothingActivity::class.java)
            this.startActivity(intent)

            val isSelectMode = !closetAdapter.selectMode
            // Iterate through the clothing items and set their selectMode based on isSelectMode.
            for (i in closetData.indices) {
                for (j in closetData[i].clothing.indices) {
                    closetData[i].clothing[j].selectMode = isSelectMode
                }
            }

            addOutfitButton?.let {
                it.visibility = if (closetData.any { it.clothing.any { clothing -> clothing.selectAllMode || clothing.selectMode } }) View.VISIBLE else View.GONE
            }

            // Notify the adapter to refresh the RecyclerView.
            closetAdapter.selectMode = isSelectMode
            closetAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OutfitsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OutfitsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}