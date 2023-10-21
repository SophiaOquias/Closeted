package com.closeted

import com.closeted.calendar.Calendar
import com.closeted.closet.Closet
import com.closeted.closet.Clothing
import com.closeted.outfits.Outfit

class DataGenerator {
    companion object {
        private val item1 : Clothing = Clothing(R.drawable.shirt_sample, false, false)
        private val item2 : Clothing = Clothing(R.drawable.skirt_sample, true, false)
        private val item3 : Clothing = Clothing(R.drawable.dress_sample, false, false)
        private val item4 : Clothing = Clothing(R.drawable.shirt2_sample,  false, false)
        private val item5 : Clothing = Clothing(R.drawable.shirt3_sample, true, false)
        private val item6 : Clothing = Clothing(R.drawable.shirt4_sample, true, false)

        private val item7 : Clothing = Clothing(R.drawable.top_sample, true, false)
        private val item8 : Clothing = Clothing(R.drawable.dress2_sample, false, false)
        private val item9 : Clothing = Clothing(R.drawable.pants_sample,  false, false)
        private val item10 : Clothing = Clothing(R.drawable.skirt2_sample, true, false)
        private val item11 : Clothing = Clothing(R.drawable.shoes_sample, true, false)

        private val section3: Closet = Closet(arrayListOf(item3), "Dresses")
        private val section1: Closet = Closet(arrayListOf(item1, item4, item5, item6), "Shirts")
        private val section2: Closet = Closet(arrayListOf(item2), "Skirts")

        private val outfit1: Outfit = Outfit(arrayListOf(item1, item2, item5))
        private val outfit2: Outfit = Outfit(arrayListOf(item7, item10, item11))
        private val outfit3: Outfit = Outfit(arrayListOf(item8, item11))
        private val outfit4: Outfit = Outfit(arrayListOf(item7, item9, item11))

        private val calendar1: Calendar = Calendar(outfit1, "March 21, 2024")
        private val calendar2: Calendar = Calendar(outfit2, "April 16, 2024")
        private val calendar3: Calendar = Calendar(outfit3, "September 25, 2024")

        fun generateClosetData(): ArrayList<Closet> {
            return arrayListOf(section1, section2, section3)
        }

        fun generateOutfitData(): ArrayList<Outfit> {
            return arrayListOf(outfit1,outfit2,outfit3,outfit4)
        }

        fun generateCalendarData(): ArrayList<Calendar> {
            return arrayListOf(calendar1, calendar2, calendar3)
        }

        fun getLaundry(closet : ArrayList<Closet>): ArrayList<Closet>  {
            val laundryList= arrayListOf<Closet>()
            for(section in closet){
                val updatedSection = arrayListOf<Clothing>()
                for(item in section.clothing){
                    if(item.laundry == true)
                        updatedSection.add(item)
                }
                if(updatedSection.size != 0)
                    laundryList.add(Closet(updatedSection, section.section))

            }

            return laundryList
        }
    }
}