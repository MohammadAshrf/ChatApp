package com.example.chatapp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.SessionProvider
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.firestore.RoomsDao
import com.example.chatapp.model.Room
import com.example.chatapp.ui.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {
    val loadingLiveData = MutableLiveData<Message?>()
    val messageLiveData = SingleLiveEvent<Message>()
    val events = SingleLiveEvent<HomeViewEvent>()
    val roomsLiveData = MutableLiveData<List<Room>>()
    fun navigateToAddRoom() {
        events.postValue(HomeViewEvent.NavigateToAddRoom)
    }

    fun loadRooms() {
        RoomsDao.getAllRooms { task ->
            if (!task.isSuccessful) {
                //show message
                return@getAllRooms
            }
            val rooms = task.result
                .toObjects(Room::class.java)
            roomsLiveData.postValue(rooms)
        }
    }

    fun logout() {
        messageLiveData.postValue(
            Message(
                message = "Are you want to logout?",
                posActionName = "yes",
                onPosActionClick = {
                    Firebase.auth.signOut()
                    SessionProvider.user = null
                    events.postValue(HomeViewEvent.NavigateToLogin)
                },
                negActionName = "cancel"
            )
        )

    }
}