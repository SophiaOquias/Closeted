package com.closeted.database

import android.net.Uri
import android.util.Log
import com.closeted.closet.Closet
import com.closeted.closet.ClosetAdapter
import com.closeted.closet.Clothing
import com.closeted.outfits.Outfit
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import java.util.UUID
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

        const val OUTFITS_COLLECTION = "outfits"
        const val OUTFIT_CLOTHING_LIST = "clothing_list"
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
                    val temp = Clothing(
                        document.id,
                        document.getString(CLOTHING_IMAGE)!!,
                        document.getString(CLOTHING_TYPE)!!,
                        document.getString(CLOTHING_NOTE),
                        document.getString(CLOTHING_LAUNDRY).toBoolean()
                    )
                    if(!temp.laundry) {
                        clothes.add(temp)
                    }
                }

                // separates clothes into clothing types
                for(type in clothingTypes) {
                    val temp: ArrayList<Clothing> = ArrayList()
                    for(clothing in clothes) {
                        if(clothing.type == type && !clothing.laundry) {
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

    suspend fun getClothingById(id: String): Clothing {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            val clothes = db.collection(CLOTHES_COLLECTION)
            val docRef = clothes.document(id)

            try {
                val documentSnapshot = docRef.get().await()

                if (documentSnapshot.exists()) {
                    return@withContext Clothing(
                        documentSnapshot.id,
                        documentSnapshot.getString(CLOTHING_IMAGE)!!,
                        documentSnapshot.getString(CLOTHING_TYPE)!!,
                        documentSnapshot.getString(CLOTHING_NOTE),
                        documentSnapshot.getString(CLOTHING_LAUNDRY).toBoolean()
                    )
                } else {
                    Log.d(TAG, "No such document")
                    throw NoSuchElementException("No such document")
                }
            } catch (e: Exception) {
                Log.d(TAG, e.toString())
                throw e
            }
        }
    }

    private suspend fun uploadImageToFirebaseStorage(imageUri: Uri): String {
        return withContext(Dispatchers.IO) {
            val storage = Firebase.storage
            val storageRef = storage.reference

            // Create a reference to the image file in Firebase Storage
            val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

            return@withContext try {
                // Upload the image to Firebase Storage
                imageRef.putFile(imageUri).await()

                val url = imageRef.downloadUrl.await()

                // Return the URL
                url.toString()
            } catch (e: Exception) {
                // Handle upload failure
                Log.e(TAG, "Error uploading image", e)
                throw e
            }
        }
    }


    suspend fun saveClothing(c: Clothing, uri: Uri): String {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            val imageUrl = uploadImageToFirebaseStorage(uri)

            val clothing = hashMapOf(
                CLOTHING_IMAGE to imageUrl,
                CLOTHING_TYPE to c.type,
                CLOTHING_NOTE to "${c.notes}",
                CLOTHING_LAUNDRY to "${c.laundry}"
            )

            return@withContext try {
                // Save the clothing details to Firestore
                val documentReference = db.collection(CLOTHES_COLLECTION).add(clothing).await()

                // Return the document ID
                documentReference.id
            } catch (e: Exception) {
                // Handle save failure
                Log.e(TAG, "Error saving clothing", e)
                throw e
            }
        }
    }

    suspend fun deleteClothing(c: Clothing) {
        val db = Firebase.firestore

        // Reference to the Firestore document
        val docRef = db.collection(CLOTHES_COLLECTION).document(c.id)

        try {
            docRef.delete().await()
            Log.d(TAG, "Document deleted successfully")
            deleteImageFromStorage(c.imageUrl)
        } catch (e: Exception) {
            Log.d(TAG, "Error deleting document: $e")
            throw e
        }
    }

    private suspend fun deleteImageFromStorage(imageUrl: String) {
        val storage = Firebase.storage

        // Create a reference to the image file in Firebase Storage using the full URL
        val imageRef = storage.getReferenceFromUrl(imageUrl)

        try {
            imageRef.delete().await()
            Log.d(TAG, "Image deleted successfully")
        } catch (e: Exception) {
            Log.d(TAG, "Error deleting image: $e")
            throw e
        }
    }

    suspend fun updateClothing(newClothing: Clothing) {
        val db = Firebase.firestore

        // Reference to the Firestore document
        val docRef = db.collection(CLOTHES_COLLECTION).document(newClothing.id)

        try {
            // Create a map of fields to be updated
            Log.d(TAG, "notes ${newClothing.notes}")
            val updates = hashMapOf(
                CLOTHING_IMAGE to newClothing.imageUrl,
                CLOTHING_TYPE to newClothing.type,
                CLOTHING_NOTE to newClothing.notes.orEmpty(),
                CLOTHING_LAUNDRY to newClothing.laundry.toString()
            )

            docRef.update(updates as Map<String, Any>).await()

            Log.d(TAG, "Document ${newClothing.id} updated successfully")
        } catch (e: Exception) {
            // Handle exceptions (e.g., FirestoreException)
            Log.d(TAG, "Error updating document: $e")
            throw e
        }
    }

    suspend fun setLaundry(id: String, bool: Boolean) {
        val db = Firebase.firestore

        // Reference to the Firestore document
        val docRef = db.collection(CLOTHES_COLLECTION).document(id)

        try {
            val updates = hashMapOf(
                CLOTHING_LAUNDRY to bool.toString()
            )

            docRef.update(updates as Map<String, Any>).await()

            Log.d(TAG, "Document ${id} updated successfully")
        } catch (e: Exception) {
            // Handle exceptions (e.g., FirestoreException)
            Log.d(TAG, "Error updating document: $e")
            throw e
        }
    }

    suspend fun getAllOutfits(): ArrayList<Outfit> {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                val outfitsQuerySnapshot = db.collection(OUTFITS_COLLECTION).get().await()
                Log.d(TAG, "queried outfits ${outfitsQuerySnapshot.size()}")

                // Extract outfit data from the documents
                return@withContext outfitsQuerySnapshot.documents.map { outfitDoc ->
                    val outfitId = outfitDoc.id
                    Log.d(TAG, "found $outfitId")
                    val clothingIds = outfitDoc.get(OUTFIT_CLOTHING_LIST) as? ArrayList<String> ?: ArrayList()

                    // Fetch details of each clothing item in the outfit
                    val clothingItems = ArrayList<Clothing>()
                    for (clothingId in clothingIds) {
                        try {
                            Log.d(TAG, "querying clothing by id $clothingId")
                            val clothing = getClothingById(clothingId)
                            clothingItems.add(clothing)
                        } catch (e: Exception) {
                            // Handle exceptions (e.g., NoSuchElementException)
                            Log.e(TAG, "Error fetching clothing item: $e")
                        }
                    }

                    Log.d(TAG, "outfit with id $outfitId has ${clothingItems.size} clothes")

                    Outfit(outfitId, clothingItems)
                } as ArrayList<Outfit>
            }
            catch (e: Exception) {
                // Handle exceptions (e.g., FirestoreException)
                Log.e(TAG, "Error getting outfit data: $e")
                throw e
            }
        }
    }

    suspend fun getOutfitById(outfitId: String): Outfit? {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                val outfitDoc = db.collection(OUTFITS_COLLECTION).document(outfitId).get().await()
                Log.d(TAG, "queried outfit with ID $outfitId")

                return@withContext if (outfitDoc.exists()) {
                    val clothingIds = outfitDoc.get(OUTFIT_CLOTHING_LIST) as? ArrayList<String> ?: ArrayList()

                    // Fetch details of each clothing item in the outfit
                    val clothingItems = ArrayList<Clothing>()
                    for (clothingId in clothingIds) {
                        try {
                            Log.d(TAG, "querying clothing by id $clothingId")
                            val clothing = getClothingById(clothingId)
                            clothingItems.add(clothing)
                        } catch (e: Exception) {
                            // Handle exceptions (e.g., NoSuchElementException)
                            Log.e(TAG, "Error fetching clothing item: $e")
                        }
                    }

                    Log.d(TAG, "outfit with id $outfitId has ${clothingItems.size} clothes")

                    Outfit(outfitId, clothingItems)
                } else {
                    null
                }
            } catch (e: Exception) {
                // Handle exceptions (e.g., FirestoreException)
                Log.e(TAG, "Error getting outfit data: $e")
                throw e
            }
        }
    }

    suspend fun deleteOutfitById(outfitId: String) {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                // Delete the outfit document
                db.collection(OUTFITS_COLLECTION).document(outfitId).delete().await()
                Log.d(TAG, "Outfit with ID $outfitId deleted successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting outfit document: $e")
                throw e
            }
        }
    }

    suspend fun updateOutfit(outfitId: String, updatedClothingList: ArrayList<Clothing>) {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                // Extract the IDs from the updatedClothingList
                val updatedClothingIds = updatedClothingList.map { it.id }

                // Update the outfit document with the new clothing list
                db.collection(OUTFITS_COLLECTION).document(outfitId)
                    .update(OUTFIT_CLOTHING_LIST, updatedClothingIds).await()

                Log.d(TAG, "Outfit with ID $outfitId updated successfully")
            } catch (e: Exception) {
                // Handle exceptions (e.g., FirestoreException)
                Log.e(TAG, "Error updating outfit document: $e")
                throw e
            }
        }
    }
}