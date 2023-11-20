package com.closeted.calendar

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.database.FirebaseReferences
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var calendarData: ArrayList<Calendar> = ArrayList()
    private val firebase: FirebaseReferences = FirebaseReferences()
    private lateinit var closetAdapter: CalendarParentAdapter
    private lateinit var historyAdapter: CalendarParentAdapter
    private var historyData: ArrayList<Calendar> = ArrayList()

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

        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        val calendarRecyclerView = view.findViewById<RecyclerView>(R.id.calendarParentRecycler)
        val layoutManager = LinearLayoutManager(requireContext())
        closetAdapter = CalendarParentAdapter(calendarData)
        calendarRecyclerView.adapter = closetAdapter
        calendarRecyclerView.layoutManager = layoutManager

        // history
        val historyRecyclerView = view.findViewById<RecyclerView>(R.id.historyRecycler)
        historyAdapter = CalendarParentAdapter(historyData)
        historyRecyclerView.adapter = historyAdapter
        val historyLayoutManager = LinearLayoutManager(requireContext())
        historyRecyclerView.layoutManager = historyLayoutManager

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btn = view.findViewById<ImageButton>(R.id.addOutfitCalendar)

        btn.setOnClickListener {
            val intent = Intent(view.context, AddOutfitToCalendarActivity::class.java)
            this.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val asyncJob = async {
                calendarData = firebase.getAllCalendarEntries()
                historyData = firebase.getHistoricOutfits()
            }
            asyncJob.await()
            closetAdapter.setData(calendarData)
            historyAdapter.setData(historyData)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}