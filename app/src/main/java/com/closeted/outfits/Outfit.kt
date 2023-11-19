package com.closeted.outfits

import com.closeted.closet.Clothing

class Outfit{
    var id = ""
        private set
    var clothingItems: ArrayList<Clothing>
        private set

    constructor(clothingItems: ArrayList<Clothing>) {
        this.clothingItems = clothingItems
    }

    constructor(id: String, clothingItems: ArrayList<Clothing>) {
        this.id = id
        this.clothingItems = clothingItems
    }
}
