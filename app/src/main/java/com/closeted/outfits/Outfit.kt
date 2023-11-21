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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Outfit

        if (id != other.id) return false
        if (clothingItems != other.clothingItems) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + clothingItems.hashCode()
        return result
    }


}
