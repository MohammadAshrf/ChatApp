package com.example.chatapp.firestore

import com.example.chatapp.model.Room
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object RoomsDao {
    private fun getRoomsCollection(): CollectionReference {
        return Firebase.firestore
            .collection(Room.CollectionName)
    }

    fun createRoom(
        title: String,
        desc: String,
        categoryId: Int,
        ownerId: String,
        onCompleteListener: OnCompleteListener<Void>
    ) {
        val collection = getRoomsCollection()
        val docRef = collection.document()
        val room = Room(
            id = docRef.id,
            title = title,
            description = desc,
            ownerId = ownerId,
            categoryId = categoryId,
        )
        docRef.set(room)
            .addOnCompleteListener(onCompleteListener)

    }

    fun getAllRooms(onCompleteListener: OnCompleteListener<QuerySnapshot>) {
        getRoomsCollection()
            .get()
            .addOnCompleteListener(onCompleteListener)
    }
}