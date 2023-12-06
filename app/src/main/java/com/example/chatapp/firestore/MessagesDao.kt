package com.example.chatapp.firestore

import com.example.chatapp.model.Message
import com.example.chatapp.model.Message.Companion.CollectionName
import com.google.android.gms.tasks.OnCompleteListener

object MessagesDao {
    fun getMessagesCollection(roomId: String) =
        RoomsDao
            .getRoomsCollection()
            .document(roomId)
            .collection(CollectionName)

    fun sendMessage(message: Message, completeListener: OnCompleteListener<Void>) {
        val messageDoc = getMessagesCollection(message.roomId ?: "")
            .document()
        message.id = messageDoc.id
        messageDoc.set(message)
            .addOnCompleteListener(completeListener)
    }
}