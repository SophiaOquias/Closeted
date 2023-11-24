package com.closeted.laundry

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.closet.Closet
import com.closeted.closet.Clothing
import com.closeted.closet.EditMode
import com.closeted.database.FirebaseReferences
import kotlinx.coroutines.launch

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
    private lateinit var laundryAdapter: LaundryAdapter
    private val firebase = FirebaseReferences()

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
        val view = inflater.inflate(R.layout.fragment_laundry, container, false)

        val laundryRecyclerViewItem = view.findViewById<RecyclerView>(R.id.laundryRecycler)

        val layoutManager = LinearLayoutManager(requireContext())

        val laundryAdapter = LaundryAdapter(laundryData, lifecycleScope)

        laundryRecyclerViewItem.adapter = laundryAdapter
        laundryRecyclerViewItem.layoutManager = layoutManager

        val clearAllButton = view.findViewById<Button>(R.id.clearAllButton)
        val selectButton = view.findViewById<Button>(R.id.selectButton)
        val addToClosetButton = view.findViewById<Button>(R.id.addToCloset)

        clearAllButton.setOnClickListener(View.OnClickListener {
            //laundryAdapter.toggleSelectAllMode()
            addToClosetButton.visibility =
                if (laundryAdapter.editMode == EditMode.SELECT || laundryAdapter.editMode == EditMode.SELECT_ALL) View.VISIBLE else View.GONE
            lifecycleScope.launch {
                try {
                    // Iterate over sections and update laundry status
                    val iterator = laundryData.iterator()
                    while (iterator.hasNext()) {
                        val section = iterator.next()
                        // Update laundry status for all items in the section
                        firebase.setLaundry(section.clothing, false)
                    }

                    // Clear the laundryData list
                    laundryData.clear()

                    // Notify the adapter that the data has changed
                    laundryAdapter.notifyDataSetChanged()

                    // Clear the selection
                    laundryAdapter.clearSelection()

                    // Hide the button after the changes
                    addToClosetButton.visibility = View.GONE

                    // Hide the checkboxes in the child adapter
                    laundryAdapter.setCheckboxVisibility(EditMode.NORMAL)
                    laundryAdapter.childItemAdapter.notifyDataSetChanged()

                    // Print a message (optional)
                    println("Laundry status updated successfully.")
                } catch (e: Exception) {
                    // Handle the exception
                    println("Error updating laundry status: $e")
                }
            }
        })

        selectButton.setOnClickListener(View.OnClickListener {
            laundryAdapter.toggleSelectMode()
            addToClosetButton.visibility =
                if (laundryAdapter.editMode == EditMode.SELECT || laundryAdapter.editMode == EditMode.SELECT_ALL) View.VISIBLE else View.GONE
        })

        addToClosetButton.setOnClickListener {
            // Get the selected clothing items
            val selectedClothing = laundryAdapter.getSelectedClothing()

            Log.d("addToClosetButton", "Before for loop")

            lifecycleScope.launch {
                firebase.setLaundry(selectedClothing, false)
                removeClothingFromClosets(laundryData, selectedClothing)
                laundryAdapter.notifyDataSetChanged()

                // Hide the button after it's clicked
                addToClosetButton.visibility = View.GONE
                laundryAdapter.clearSelection()
                laundryAdapter.toggleSelectMode()
            }
        }
        firebase.getAllLaundry(laundryData, laundryAdapter)

        return view
    }

    private fun removeClothingFromClosets(closets: ArrayList<Closet>, clothingToRemove: ArrayList<Clothing>) {
        val sectionsToRemove: ArrayList<Closet> = ArrayList()

        for (closet in closets) {
            val updatedClothingList = ArrayList<Clothing>()

            for (clothing in closet.clothing) {
                if (!clothingToRemove.contains(clothing)) {
                    updatedClothingList.add(clothing)
                }
            }
            // Update the closet with the filtered list
            closet.clothing = updatedClothingList

            // Check if the closet is empty
            if (closet.clothing.isEmpty()) {
                sectionsToRemove.add(closet)
            }
        }

        // Remove empty closets from the UI
        for (c in sectionsToRemove) {
            // Remove closetToRemove from your UI (e.g., remove it from the data list used by your adapter)
            // For example, if you have a list named "closetsData" in your adapter, you can use:
            laundryData.remove(c)
        }

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