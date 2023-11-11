package com.closeted.closet

import android.net.Uri
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class MyFirestoreReferences {
    // All our instances of Firestore and Storage
    private var firebaseFirestoreInstance: FirebaseFirestore? = null
    private var storageReferenceInstance: StorageReference? = null
    private var usersRef: CollectionReference? = null
    private var postsRef: CollectionReference? = null

    // Collection and document names
    val USERS_COLLECTION = "User"
    val POST_COLLECTION = "Post"

    fun getFirestoreInstance(): FirebaseFirestore {
        if (firebaseFirestoreInstance == null) {
            firebaseFirestoreInstance = FirebaseFirestore.getInstance()
        }
        return firebaseFirestoreInstance!!
    }

    fun getStorageReferenceInstance(): StorageReference {
        if (storageReferenceInstance == null) {
            storageReferenceInstance = FirebaseStorage.getInstance().reference
        }
        return storageReferenceInstance!!
    }

    fun getUserCollectionReference(): CollectionReference {
        if (usersRef == null) {
            usersRef = getFirestoreInstance().collection(USERS_COLLECTION)
        }
        return usersRef!!
    }

    fun getPostCollectionReference(): CollectionReference {
        if (postsRef == null) {
            postsRef = getFirestoreInstance().collection(POST_COLLECTION)
        }
        return postsRef!!
    }

    fun getUserDocumentReference(stringRef: String?): DocumentReference {
        return getUserCollectionReference().document(stringRef!!)
    }

    fun getPostDocumentReference(stringRef: String?): DocumentReference {
        return getPostCollectionReference().document(stringRef!!)
    }

    /* As an image download + inserting into an ImageView is done in both the ViewHolder and the
     * PostActivity, I decided to centralize the logic to this class.
     *
     * The method takes in a Post object and an ImageView. It handles (1) identifying the path of
     * the image, (2) downloading the image, and (3) inserting it into the supplied ImageView.
     * */
    fun downloadImageIntoImageView(p: Post, iv: ImageView) {
        val path = "images/${p.getUserRef().id}-${Uri.parse(p.getImageUri()).lastPathSegment}"
        getStorageReferenceInstance().child(path).downloadUrl
            .addOnCompleteListener(OnCompleteListener { task ->
                val circularProgressDrawable = CircularProgressDrawable(iv.context)
                circularProgressDrawable.centerRadius = 30f
                Picasso.get()
                    .load(task.result)
                    .error(R.drawable.ic_error_foreground)
                    .placeholder(circularProgressDrawable)
                    .into(iv)
            })
    }

    /* This method is actually used only in one place -- the AddPostActivity. However, I thought
     * that abstracting the "nitty-gritty" look of the string helps in readability.
     * */
    fun generateNewImagePath(userRef: DocumentReference, imageUri: Uri): String {
        return "images/${userRef.id}-${imageUri.lastPathSegment}"
    }
}
