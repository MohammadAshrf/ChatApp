package com.example.chatapp.ui.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.SessionProvider
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.firestore.MessagesDao
import com.example.chatapp.model.Message
import com.example.chatapp.model.Room
import com.google.firebase.firestore.DocumentChange

class ChatViewModel : ViewModel() {
    private var room: Room? = null
    val messageLiveData = MutableLiveData<String>()
    val toastLiveData = SingleLiveEvent<String>()
    val newMessagesLiveData = SingleLiveEvent<List<Message>>()
    val roomName = SingleLiveEvent<String>()

    fun sendMessage() {
        if (messageLiveData.value.isNullOrBlank()) return
        val message = Message(
            content = messageLiveData.value,
            senderId = SessionProvider.user?.id,
            senderName = SessionProvider.user?.userName,
            roomId = room?.id
        )
        MessagesDao.sendMessage(message) { task ->
            if (task.isSuccessful) {
                messageLiveData.value = ""
                return@sendMessage
            }
            toastLiveData.value = "Something went wrong, try again later!"

        }
    }

    fun changeRoom(room: Room?) {
        this.room = room
        listenForMessagesInRoom()
    }

    private fun listenForMessagesInRoom() {
        MessagesDao.getMessagesCollection(room?.id ?: "").orderBy("dateTime").limitToLast(100)
            .addSnapshotListener { snapShot, error ->
                val newMessages = mutableListOf<Message>()
                snapShot?.documentChanges?.forEach { docChange ->
                    if (docChange.type == DocumentChange.Type.ADDED) {
                        val message = docChange.document.toObject(Message::class.java)
                        newMessages.add(message)
                    }
//                    } else if (docChange.type == DocumentChange.Type.MODIFIED) {
//
//                    } else if (docChange.type == DocumentChange.Type.REMOVED) {
//
//                    }
                }
                newMessagesLiveData.value = newMessages
            }
    }
}