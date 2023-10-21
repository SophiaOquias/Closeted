package com.closeted

import com.closeted.closet.Closet

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

        private val outfit1: ParentModel = ParentModel(arrayListOf(item1, item2, item5))
        private val outfit2: ParentModel = ParentModel(arrayListOf(item7, item10, item11))
        private val outfit3: ParentModel = ParentModel(arrayListOf(item8, item11))
        private val outfit4: ParentModel = ParentModel(arrayListOf(item7, item9, item11))

        fun generateClosetData(): ArrayList<Closet> {
            return arrayListOf(section1, section2, section3)
        }

        fun generateOutfitData(): ArrayList<ParentModel> {
            return arrayListOf(outfit1,outfit2,outfit3,outfit4)
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