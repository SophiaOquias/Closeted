package com.closeted.calendar

import com.closeted.outfits.Outfit
import com.google.firebase.Timestamp

class Calendar {

    var id: String = ""
        private set
    var outfit: Outfit
        private set

    var date: Timestamp
        private set

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
}