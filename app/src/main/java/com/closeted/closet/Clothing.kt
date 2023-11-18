package com.closeted.closet

class Clothing {
    var id = ""
    var imageUrl: String
    var type: String
    var notes: String?
    var laundry: Boolean
    var isEditMode = false
    var selectAllMode = false
    var selectMode = false

    constructor(imageUrl: String, type: String, notes: String?, laundry: Boolean) {
        this.imageUrl = imageUrl
        this.type = type
        this.notes = notes
        this.laundry = laundry
    }

    constructor(id: String, imageUrl: String, type: String, notes: String?, laundry: Boolean) {
        this.id = id
        this.imageUrl = imageUrl
        this.type = type
        this.notes = notes
        this.laundry = laundry
    }



    fun setType(newType: String) {
        type = newType
    }


}