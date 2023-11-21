package com.closeted.calendar

import com.closeted.outfits.Outfit
import com.google.firebase.Timestamp

class Calendar {

    var id: String = ""
        private set
    var outfit: Outfit
        private set

    var date: Timestamp

    constructor(outfit: Outfit, date: Timestamp) {
        this.outfit = outfit
        this.date = date
        this.id = ""
    }

    constructor(id: String, outfit: Outfit, date: Timestamp) {
        this.id = id
        this.outfit = outfit
        this.date = date
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Calendar

        if (id != other.id) return false
        if (outfit != other.outfit) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + outfit.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }


}