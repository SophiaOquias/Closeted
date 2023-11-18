package com.closeted.database

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.closeted.closet.Closet
import com.closeted.closet.ClosetAdapter
import com.closeted.closet.Clothing
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID

class FirebaseReferences {

    companion object {
        const val TAG = "FIREBASE"

        const val CLOTHES_COLLECTION = "clothes"
        const val CLOTHING_IMAGE = "imageUrl"
        const val CLOTHING_TYPE = "type"
        const val CLOTHING_NOTE = "notes"
        const val CLOTHING_LAUNDRY = "laundry"

        private var clothingTypes = arrayOf(
            "Blouse",
            "Dress",
            "Hoodie",
            "Jeans",
            "Jacket",
            "Pants",
            "Shirt",
            "Skirt",
            "Sweater",
            "T-Shirt"
        )
    }

    fun getAllClothes(closet: ArrayList<Closet>, adapter: ClosetAdapter) {
        val db = Firebase.firestore
        val clothes: ArrayList<Clothing> = ArrayList()
        closet.clear()

        db.collection(CLOTHES_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                for(document in result) {
                    Log.d("TEST", "${document.id} => ${document.data}")
                    clothes.add(
                        Clothing(
                            document.id,
                            document.getString(CLOTHING_IMAGE)!!,
                            document.getString(CLOTHING_TYPE)!!,
                            document.getString(CLOTHING_NOTE),
                            document.getString(CLOTHING_LAUNDRY).toBoolean()
                        )
                    )
                }

                // separates clothes into clothing types
                for(type in clothingTypes) {
                    val temp: ArrayList<Clothing> = ArrayList()
                    for(clothing in clothes) {
                        if(clothing.type.equals(type)) {
                            temp.add(clothing)
                        }
                    }
                    if(temp.isNotEmpty()) {
                        closet.add(Closet(temp, type))
                    }
                }
                if(closet.isNotEmpty()) {
                    adapter.notifyDataSetChanged()
                }

            }
            .addOnFailureListener { exception ->
                Log.d("TEST", "Error getting documents: $exception")
            }

    }

    fun uploadImageToFirebaseStorage(imageUri: Uri, context: Context, clothing: Clothing) {
        val storage = Firebase.storage
        val storageRef = storage.reference

        // Create a reference to the image file in Firebase Storage
        val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

        // Upload the image to Firebase Storage
        imageRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                // Image upload successful
                // Now, you can get the download URL
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    clothing.imageUrl = uri.toString()

                    // Now, you can save the download URL and other details to Firestore
                    saveClothing(clothing)
                }
            }
            .addOnFailureListener { exception ->
                // Handle failures
                Toast.makeText(
                    context,
                    "Failed to upload image to Firebase Storage",
                    Toast.LENGTH_SHORT
                ).show()
                Log.w(TAG, "Error adding document", exception)
            }
    }

    fun saveClothing(c: Clothing) {
        val db = Firebase.firestore

        val clothing = hashMapOf(
            CLOTHING_IMAGE to c.imageUrl,
            CLOTHING_TYPE to c.type,
            CLOTHING_NOTE to "${c.notes}",
            CLOTHING_LAUNDRY to "${c.laundry}"
        )

        db.collection(CLOTHES_COLLECTION)
            .add(clothing)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }
}