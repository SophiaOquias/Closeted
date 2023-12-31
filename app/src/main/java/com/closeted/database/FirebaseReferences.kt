package com.closeted.database

import android.net.Uri
import android.util.Log
import com.closeted.calendar.Calendar
import com.closeted.closet.Closet
import com.closeted.closet.ClosetAdapter
import com.closeted.closet.Clothing
import com.closeted.outfits.AddClothingAdapter
import com.closeted.outfits.Outfit
import com.closeted.laundry.LaundryAdapter
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
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

        const val CALENDAR_COLLECTION = "calendar"
        const val CALENDAR_OUTFIT = "outfit"
        const val CALENDAR_DATE = "timestamp"

        const val HISTORY_COLLECTION = "history"
        const val HISTORY_IMAGE_URLS = "history_image_urls"
        const val HISTORY_DATE = "history_date"

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
                        if(clothing.type == type) {
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

    fun getAllClothes(closet: ArrayList<Closet>, adapter: AddClothingAdapter) {
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
                    clothes.add(temp)
                }

                // separates clothes into clothing types
                for(type in clothingTypes) {
                    val temp: ArrayList<Clothing> = ArrayList()
                    for(clothing in clothes) {
                        if(clothing.type == type) {
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

    fun getAllLaundry(closet: ArrayList<Closet>, adapter: LaundryAdapter) {
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
                    if(temp.laundry) {
                        clothes.add(temp)
                    }
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

    suspend fun uploadImageToFirebaseStorage(imageUri: Uri): String {
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
            // Identify outfits that include the clothing to be deleted
            val outfitsQuerySnapshot = db.collection(OUTFITS_COLLECTION)
                .whereArrayContains(OUTFIT_CLOTHING_LIST, c.id)
                .get()
                .await()

            // Update each outfit to remove the clothing ID
            for (outfitDoc in outfitsQuerySnapshot.documents) {
                val outfitId = outfitDoc.id
                val existingClothingIds = outfitDoc.get(OUTFIT_CLOTHING_LIST) as? ArrayList<String> ?: ArrayList()

                // Remove the clothing ID from the list
                existingClothingIds.remove(c.id)

                // Update the outfit document with the modified clothing list
                db.collection(OUTFITS_COLLECTION).document(outfitId)
                    .update(OUTFIT_CLOTHING_LIST, existingClothingIds).await()

                Log.d(TAG, "Removed clothing ${c.id} from outfit $outfitId")
            }

            // Delete the clothing document
            docRef.delete().await()
            Log.d(TAG, "Document deleted successfully")
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

    suspend fun setLaundry(clothingList: ArrayList<Clothing>, bool: Boolean) {
        val db = Firebase.firestore

        // Use a batch to update multiple documents atomically
        val batch = db.batch()

        try {
            for (clothing in clothingList) {
                val docRef = db.collection(CLOTHES_COLLECTION).document(clothing.id)

                val updates = hashMapOf(
                    CLOTHING_LAUNDRY to bool.toString()
                )

                // Update the document in the batch
                batch.update(docRef, updates as Map<String, Any>)
                Log.d(TAG, "Document ${clothing.id} added to batch for laundry update")
            }

            // Commit the batch to update all documents atomically
            batch.commit().await()
            Log.d(TAG, "Batch update successful")
        } catch (e: Exception) {
            // Handle exceptions (e.g., FirestoreException)
            Log.e(TAG, "Error updating documents in batch: $e")
            throw e
        }
    }


    suspend fun insertOutfit(clothingIds: ArrayList<String>): String {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                val outfitRef = db.collection(OUTFITS_COLLECTION).document()

                outfitRef.set(
                    hashMapOf(
                        OUTFIT_CLOTHING_LIST to clothingIds
                    )
                ).await()

                Log.d(TAG, "Outfit inserted successfully with ID: ${outfitRef.id}")

                outfitRef.id
            } catch (e: Exception) {
                // Handle exceptions (e.g., FirestoreException)
                Log.e(TAG, "Error inserting outfit: $e")
                throw e
            }
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

    suspend fun updateAddOutfit(outfitId: String, updatedClothingIds: ArrayList<String>) {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                // Retrieve the existing list of clothing IDs
                val existingClothingIds = db.collection(OUTFITS_COLLECTION)
                    .document(outfitId)
                    .get()
                    .await()
                    .get(OUTFIT_CLOTHING_LIST) as? ArrayList<String> ?: ArrayList()

                // Append the new clothing IDs to the existing list
                existingClothingIds.addAll(updatedClothingIds)

                // Update the outfit document with the new clothing list
                db.collection(OUTFITS_COLLECTION).document(outfitId)
                    .update(OUTFIT_CLOTHING_LIST, existingClothingIds).await()

                Log.d(TAG, "Outfit with ID $outfitId updated successfully")
            } catch (e: Exception) {
                // Handle exceptions (e.g., FirestoreException)
                Log.e(TAG, "Error updating outfit document: $e")
                throw e
            }
        }
    }


    suspend fun deleteOutfitById(outfitId: String) {
        val db = Firebase.firestore

        try {
            // Identify calendar entries that use the outfit to be deleted
            val calendarQuerySnapshot = db.collection(CALENDAR_COLLECTION)
                .whereEqualTo(CALENDAR_OUTFIT, outfitId)
                .get()
                .await()

            // Delete each calendar entry
            for (calendarDoc in calendarQuerySnapshot.documents) {
                val calendarEntryId = calendarDoc.id

                // Delete the calendar entry document
                db.collection(CALENDAR_COLLECTION).document(calendarEntryId).delete().await()

                Log.d(TAG, "Calendar entry with ID $calendarEntryId deleted successfully")
            }

            // Delete the outfit document
            db.collection(OUTFITS_COLLECTION).document(outfitId).delete().await()

            Log.d(TAG, "Outfit with ID $outfitId deleted successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting outfit document: $e")
            throw e
        }
    }

    suspend fun updateOutfit(outfitId: String, updatedClothingIds: ArrayList<String>) {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
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

    suspend fun insertCalendarEntry(calendar: Calendar): Calendar {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                // Create a new calendar entry document and get its reference
                val calendarRef = db.collection(CALENDAR_COLLECTION).document()

                // Set the calendar entry document with the provided data
                calendarRef.set(
                    hashMapOf(
                        CALENDAR_OUTFIT to calendar.outfit.id,
                        CALENDAR_DATE to calendar.date
                    )
                ).await()

                Log.d(TAG, "Calendar entry inserted successfully with ID: ${calendarRef.id}")

                // Return a new Calendar object with the document ID
                Calendar(calendarRef.id, calendar.outfit, calendar.date)
            } catch (e: Exception) {
                // Handle exceptions (e.g., FirestoreException)
                Log.e(TAG, "Error inserting calendar entry: $e")
                throw e
            }
        }
    }

    suspend fun getAllCalendarEntries(): ArrayList<Calendar> {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                val currentTime = Timestamp.now()

                val calendarQuerySnapshot = db.collection(CALENDAR_COLLECTION)
                    .whereGreaterThanOrEqualTo(CALENDAR_DATE, currentTime)
                    .get()
                    .await()

                Log.d(TAG, "Queried ${calendarQuerySnapshot.size()} future calendar entries")

                // Extract calendar entry data from the documents
                val calendarList = calendarQuerySnapshot.documents.map { calendarDoc ->
                    val calendarId = calendarDoc.id
                    val outfitId = calendarDoc.getString(CALENDAR_OUTFIT) ?: ""
                    val date = calendarDoc.getTimestamp(CALENDAR_DATE)!!
                    val outfit = getOutfitById(outfitId)!!

                    Calendar(calendarId, outfit, date)
                } as ArrayList<Calendar>

                // Sort the list by date
                calendarList.sortBy { it.date.seconds }

                return@withContext calendarList
            } catch (e: Exception) {
                // Handle exceptions (e.g., FirestoreException)
                Log.e(TAG, "Error getting future calendar entries: $e")
                throw e
            }
        }
    }

    suspend fun getHistoricOutfits(): ArrayList<History> {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                val currentTime = Timestamp.now()

                // Get past calendar entries
                val calendarQuerySnapshot = db.collection(CALENDAR_COLLECTION)
                    .whereLessThan(CALENDAR_DATE, currentTime)
                    .get()
                    .await()

                Log.d(TAG, "Queried ${calendarQuerySnapshot.size()} past calendar entries")

                // Extract calendar entry data from the documents
                val historyList = ArrayList<History>()

                for (calendarDoc in calendarQuerySnapshot.documents) {
                    val calendarId = calendarDoc.id
                    val outfitId = calendarDoc.getString(CALENDAR_OUTFIT) ?: ""
                    val date = calendarDoc.getTimestamp(CALENDAR_DATE)!!
                    val outfit = getOutfitById(outfitId)!!

                    // Check if the date is in the past
                    if (date.seconds < currentTime.seconds) {
                        // Move to history collection
                        val history = History(
                            imageUrls = outfit.clothingItems.map { it.imageUrl } as ArrayList<String>,
                            date = date
                        )

                        // Add to history collection
                        val historyRef = db.collection(HISTORY_COLLECTION).document()
                        historyRef.set(
                            hashMapOf(
                                HISTORY_IMAGE_URLS to history.imageUrls,
                                HISTORY_DATE to history.date
                            )
                        ).await()

                        // Delete from calendar collection
                        db.collection(CALENDAR_COLLECTION).document(calendarId).delete().await()

                        Log.d(TAG, "Moved calendar entry with ID $calendarId to history")
                    }
                }

                // Append existing items from the history collection
                val historyQuerySnapshot = db.collection(HISTORY_COLLECTION)
                    .get()
                    .await()

                for (historyDoc in historyQuerySnapshot.documents) {
                    val historyId = historyDoc.id
                    val imageUrls = historyDoc.get(HISTORY_IMAGE_URLS) as ArrayList<String>
                    val date = historyDoc.getTimestamp(HISTORY_DATE)!!

                    historyList.add(History(historyId, imageUrls, date))
                }

                // Sort the list by date
                historyList.sortBy { it.date.seconds }

                return@withContext historyList
            } catch (e: Exception) {
                // Handle exceptions (e.g., FirestoreException)
                Log.e(TAG, "Error moving calendar entries to history: $e")
                throw e
            }
        }
    }

    suspend fun deleteHistoryById(historyId: String) {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                // Delete the history document
                db.collection(HISTORY_COLLECTION).document(historyId).delete().await()

                Log.d(TAG, "History entry with ID $historyId deleted successfully")
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting history entry: $e")
                throw e
            }
        }
    }

    suspend fun getCalendarById(calendarId: String): Calendar? {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                val calendarDoc = db.collection(CALENDAR_COLLECTION)
                    .document(calendarId)
                    .get()
                    .await()

                if (calendarDoc.exists()) {
                    val outfitId = calendarDoc.getString(CALENDAR_OUTFIT) ?: ""
                    val date = calendarDoc.getTimestamp(CALENDAR_DATE)!!
                    val outfit = getOutfitById(outfitId)!!

                    return@withContext Calendar(calendarId, outfit, date)
                } else {
                    // Calendar entry with the specified ID does not exist
                    return@withContext null
                }
            } catch (e: Exception) {
                // Handle exceptions (e.g., FirestoreException)
                Log.e(TAG, "Error getting calendar entry by ID: $e")
                throw e
            }
        }
    }

    suspend fun deleteCalendarEntryById(calendarId: String) {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                val calendarRef = db.collection(CALENDAR_COLLECTION).document(calendarId)
                calendarRef.delete().await()
                Log.d(TAG, "Calendar entry deleted successfully with ID: $calendarId")
            } catch (e: Exception) {
                // Handle exceptions (e.g., FirestoreException)
                Log.e(TAG, "Error deleting calendar entry with ID $calendarId: $e")
                throw e
            }
        }
    }

    suspend fun updateCalendarDate(calendarId: String, newDate: Timestamp) {
        return withContext(Dispatchers.IO) {
            val db = Firebase.firestore

            try {
                val calendarRef = db.collection(CALENDAR_COLLECTION).document(calendarId)

                // Update the date field in the calendar document
                calendarRef.update(CALENDAR_DATE, newDate).await()

                Log.d(TAG, "Calendar date updated successfully")
            } catch (e: Exception) {
                // Handle exceptions (e.g., FirestoreException)
                Log.e(TAG, "Error updating calendar date: $e")
                throw e
            }
        }
    }
}