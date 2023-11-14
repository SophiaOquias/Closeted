package com.closeted.calendar

import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.outfits.ViewOutfitAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ViewCalendarOutfitActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ViewOutfitAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_view_outfit)

        val imgList: ArrayList<Int> = ArrayList()

        // fill sample data
        repeat(10) {
            imgList.add(R.drawable.temp_pic)
        }

        this.recyclerView = findViewById(R.id.recyclerView)
        this.adapter = ViewOutfitAdapter(imgList)
        this.recyclerView.adapter = adapter
        this.recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val editButton = findViewById<ImageButton>(R.id.viewEditBtn)
        val confirmButton = findViewById<Button>(R.id.viewConfirmBtn)
        val addButton = findViewById<FloatingActionButton>(R.id.viewAddBtn)
        val dateTv = findViewById<TextView>(R.id.dateTv)

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
        }
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
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}