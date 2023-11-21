package com.closeted.closet

class Closet(clothing: ArrayList<Clothing>, section: String){
    var clothing = clothing

    var section = section
        private set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Closet

        if (clothing != other.clothing) return false
        if (section != other.section) return false

        return true
    }

    override fun hashCode(): Int {
        var result = clothing.hashCode()
        result = 31 * result + section.hashCode()
        return result
    }


}