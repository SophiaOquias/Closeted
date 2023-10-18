package com.closeted

class DataGenerator {
    companion object {
        private val item1 : Clothing = Clothing(R.drawable.shirt_sample)
        private val item2 : Clothing = Clothing(R.drawable.skirt_sample)
        private val item3 : Clothing = Clothing(R.drawable.dress_sample)
        private val item4 : Clothing = Clothing(R.drawable.shirt2_sample)
        private val item5 : Clothing = Clothing(R.drawable.shirt3_sample)
        private val item6 : Clothing = Clothing(R.drawable.shirt4_sample)

        private val section3: Closet = Closet(arrayListOf(item3), "Dresses")
        private val section1: Closet = Closet(arrayListOf(item1, item4, item5, item6), "Shirts")
        private val section2: Closet = Closet(arrayListOf(item2), "Skirts")

        fun generateClosetData(): ArrayList<Closet> {
            return arrayListOf(section1, section2, section3)
        }
    }
}