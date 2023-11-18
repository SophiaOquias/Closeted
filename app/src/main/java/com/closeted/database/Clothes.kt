package com.closeted.database

import android.net.Uri

class Clothes {

    var imageUri: Uri
        private set
    var type: String
        private set
    var notes: String? = null
        private set
    var id: String = ""
        private set

    constructor(imageUri: Uri, type: String, notes: String?) {
        this.imageUri = imageUri
        this.type = type
        this.notes = notes
    }

    constructor(imageUri: Uri, type: String, notes: String?, id: String) {
        this.imageUri = imageUri
        this.type = type
        this.notes = notes
        this.id = id
    }
}