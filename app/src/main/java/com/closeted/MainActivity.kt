package com.closeted

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.closeted.calendar.CalendarFragment
import com.closeted.databinding.ActivityMainBinding
import com.closeted.closet.ClosetFragment
import com.closeted.laundry.LaundryFragment
import com.closeted.outfits.OutfitsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(ClosetFragment())
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding.bottomNavigationView.setOnItemSelectedListener { item ->

            when(item.itemId) {
                R.id.navCloset -> {
                    replaceFragment(ClosetFragment())
                }
                R.id.navOutfits -> {
                    replaceFragment(OutfitsFragment())
                }
                R.id.navCalendar -> {
                    replaceFragment(CalendarFragment())
                }
                R.id.navLaundry -> {
                    replaceFragment(LaundryFragment())
                }
            }

            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame, fragment)
        fragmentTransaction.commit()
    }
}
