package com.example.chatapp.ui.addRoom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.SessionProvider
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.firestore.RoomsDao
import com.example.chatapp.model.Category
import com.example.chatapp.ui.Message

class AddRoomViewModel : ViewModel() {
    val isAddedRoom = MutableLiveData<Boolean>()
    val loadingLiveData = MutableLiveData<Message?>()
    val events = SingleLiveEvent<AddRoomViewEvent>()
    val roomName = MutableLiveData<String>()
    val roomDesc = MutableLiveData<String>()
    val roomNameError = MutableLiveData<String?>()
    val roomDescError = MutableLiveData<String?>()
    val categories = Category.getCategories()
    val messageLiveData = SingleLiveEvent<Message>()
    private var selectedCategory: Category = categories[0]

    fun createRoom() {
        if (!validForm()) return
        isAddedRoom.value = true
        loadingLiveData.postValue(
            Message(
                message = "loading...",
                isCancelable = false

            )
        )
        RoomsDao.createRoom(
            title = roomName.value ?: "",
            desc = roomDesc.value ?: "",
            ownerId = SessionProvider.user?.id ?: "",
            categoryId = selectedCategory.id,
        ) { task ->
            loadingLiveData.postValue(null)
            if (task.isSuccessful) {
                messageLiveData.postValue(
                    Message(
                        message = "Room created successfully",
                        posActionName = "ok",
                        onPosActionClick = {
                            events.postValue(AddRoomViewEvent.NavigateToHomeAndFinish)
                        }
                    )
                )
                return@createRoom
            }
            isAddedRoom.value = false
            messageLiveData.postValue(
                Message(
                    message = "Something went wrong ${task.exception?.localizedMessage}",
                    posActionName = "ok"
                )
            )

        }
    }

    private fun validForm(): Boolean {
        var isValid = true
        if (roomName.value.isNullOrBlank()) {
            roomNameError.postValue("Please enter room title")
            isValid = false
        } else {
            roomNameError.postValue(null)
        }
        if (roomDesc.value.isNullOrBlank()) {
            roomDescError.postValue("Please enter room description")
            isValid = false
        } else {
            roomDescError.postValue(null)
        }
        return isValid
    }

    fun onCategorySelected(position: Int) {
        selectedCategory = categories[position]
    }
}