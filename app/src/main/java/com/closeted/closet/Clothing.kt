package com.closeted.closet

class Clothing {
    var id = ""
    var imageUrl: String
    var type: String
    var notes: String?
    var laundry: Boolean

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Clothing

        if (id != other.id) return false
        if (imageUrl != other.imageUrl) return false
        if (type != other.type) return false
        if (notes != other.notes) return false
        if (laundry != other.laundry) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + (notes?.hashCode() ?: 0)
        result = 31 * result + laundry.hashCode()
        return result
    }


}