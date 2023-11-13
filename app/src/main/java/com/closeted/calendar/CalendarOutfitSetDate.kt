package com.closeted.calendar

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.closeted.R
import com.closeted.outfits.ViewOutfitAdapter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CalendarOutfitSetDate : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ViewOutfitAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_outfit_set_date)

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

        val dateTv: TextView = findViewById(R.id.editDateTv)

        setDate(dateTv)

        dateTv.setOnClickListener {
            setDate(dateTv)
        }

        val confirmBtn: Button = findViewById(R.id.calendarConfirmAddBtn)

        confirmBtn.setOnClickListener {
            val resultIntent = Intent()
            // Set the selected outfit as a result or any other relevant data
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
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