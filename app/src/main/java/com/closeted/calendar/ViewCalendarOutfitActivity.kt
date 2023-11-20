package com.closeted.calendar

import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.closet.Clothing
import com.closeted.database.FirebaseReferences
import com.closeted.outfits.Outfit
import com.closeted.outfits.ViewOutfitAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Timestamp
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ViewCalendarOutfitActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ViewOutfitAdapter
    private lateinit var clothingList: ArrayList<Clothing>
    private lateinit var currentCalendar: com.closeted.calendar.Calendar
    private val firebase: FirebaseReferences = FirebaseReferences()
    private lateinit var outfit: Outfit
    private lateinit var currentDate: Timestamp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view_outfit)

        val editButton = findViewById<ImageButton>(R.id.viewEditBtn)
        val confirmButton = findViewById<Button>(R.id.viewConfirmBtn)
        val addButton = findViewById<FloatingActionButton>(R.id.viewAddBtn)
        val dateTv = findViewById<TextView>(R.id.dateTv)
        val deleteButton = findViewById<ImageButton>(R.id.viewDeleteBtn)

        clothingList = ArrayList()
        val calendarId = intent.getStringExtra("calendar_id")!!
        val outfitId = intent.getStringExtra("outfit_id")!!

        lifecycleScope.launch {
            val calendarJob = async { currentCalendar = firebase.getCalendarById(calendarId)!! }
            calendarJob.await()

            currentDate = currentCalendar.date
            dateTv.text = formatTimestampToString(currentDate)

            val outfitJob = async {outfit = firebase.getOutfitById(currentCalendar.outfit.id)!!}
            outfitJob.await()

            adapter.setData(outfit.clothingItems)
        }

        this.recyclerView = findViewById(R.id.recyclerView)
        this.adapter = ViewOutfitAdapter(clothingList, outfitId)
        this.recyclerView.adapter = adapter
        this.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        editButton.setOnClickListener {
            // Toggle the edit mode flag
            adapter.isEditModeEnabled = true
            adapter.notifyDataSetChanged()
            confirmButton.isVisible = true
            confirmButton.isEnabled = true
            addButton.isVisible = true
            addButton.isEnabled = true
            addButton.isClickable = true
            editButton.isEnabled = false
            editButton.isVisible = false
            dateTv.setTextColor(Color.BLUE)
            dateTv.setOnClickListener {
                setDate(dateTv)
            }
        }

        confirmButton.setOnClickListener {
            adapter.isEditModeEnabled = false
            adapter.notifyDataSetChanged()
            confirmButton.isVisible = false
            confirmButton.isEnabled = false
            addButton.isVisible = false
            addButton.isEnabled = false
            addButton.isClickable = false
            editButton.isEnabled = true
            editButton.isVisible = true
            dateTv.setTextColor(Color.BLACK)
            dateTv.setOnClickListener(null)

            // TODO: update calendar
        }

        deleteButton.setOnClickListener {
            lifecycleScope.launch {
                val asyncJob = async { firebase.deleteCalendarEntryById(calendarId) }
                asyncJob.await()
                finish()
            }
        }
    }

    private fun formatTimestampToString(timestamp: Timestamp): String {
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val date = timestamp.toDate()
        return dateFormat.format(date)
    }

    private fun setDate(dateTv: TextView) {
        val c = Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                // Update the Calendar instance with the selected date
                c.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(c.time)
                dateTv.text = formattedDate

                val timestampMillis = c.timeInMillis

                currentDate = Timestamp(timestampMillis / 1000, 0)
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}