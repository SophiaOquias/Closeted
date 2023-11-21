package com.closeted.database

import com.google.firebase.Timestamp

class History {
    var id = ""
        private set
    var imageUrls: ArrayList<String>
        private set
    var date: Timestamp
        private set

    constructor(imageUrls: ArrayList<String>, date: Timestamp) {
        this.imageUrls = imageUrls
        this.date = date
    }

    constructor(id: String, imageUrls: ArrayList<String>, date: Timestamp) {
        this.id = id
        this.imageUrls = imageUrls
        this.date = date
    }
}