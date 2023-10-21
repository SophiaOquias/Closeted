package com.closeted

class Clothing(imageId: Int, laundry: Boolean){
    var imageId = imageId
        private set

    var laundry = laundry
        private set

    var isEditMode = false

    var selectAllMode = false

    var selectMode = false
}