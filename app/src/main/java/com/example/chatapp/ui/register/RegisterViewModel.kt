package com.example.chatapp.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.SessionProvider
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.firestore.UsersDao
import com.example.chatapp.model.User
import com.example.chatapp.ui.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterViewModel : ViewModel() {
    val userName = MutableLiveData<String>()
    val userNameError = MutableLiveData<String?>()
    val email = MutableLiveData<String>()
    val emailError = MutableLiveData<String?>()
    val password = MutableLiveData<String>()
    val passwordError = MutableLiveData<String?>()
    val passwordConfirmation = MutableLiveData<String>()
    val passwordConfirmationError = MutableLiveData<String?>()
    val isCreated = MutableLiveData<Boolean>()
    val messageLiveData = SingleLiveEvent<Message>()

    val events = SingleLiveEvent<RegisterViewEvent>()

    private val auth = Firebase.auth

    fun register() {

        if (!validForm()) return
        isCreated.value = true
        auth.createUserWithEmailAndPassword(email.value!!, password.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    insertUserToFirestore(task.result.user?.uid)
                } else {
                    //show error
                    isCreated.value = false
                    messageLiveData.postValue(
                        Message(
                            message = task.exception?.localizedMessage
                        )
                    )
                }
            }

    }

    private fun insertUserToFirestore(uid: String?) {
        val user = User(
            id = uid,
            userName = userName.value,
            email = email.value
        )
        UsersDao.createUser(user) { task ->
            isCreated.value = false
            if (task.isSuccessful) {
                messageLiveData.postValue(
                    Message(
                        message = "User Registered Successfully",
                        posActionName = "ok",
                        onPosActionClick = {
                            //Save User in memory to save performance
                            SessionProvider.user = user
                            //Navigate to home
                            events.postValue(RegisterViewEvent.NavigateToHome)
                        }
                    )
                )
            } else {
                //show error
                messageLiveData.postValue(
                    Message(
                        message = task.exception?.localizedMessage
                    )
                )
            }

        }
    }

    private fun validForm(): Boolean {
        var isValid = true
        if (userName.value.isNullOrBlank()) {
            //show error
            userNameError.postValue("Please enter username")
            isValid = false
        } else {
            userNameError.postValue(null)
        }
        if (email.value.isNullOrBlank()) {
            //show error
            emailError.postValue("Please enter email")
            isValid = false
        } else {
            emailError.postValue(null)
        }
        if (password.value.isNullOrBlank()) {
            //show error
            passwordError.postValue("Please enter password")
            isValid = false
        } else {
            passwordError.postValue(null)
        }
        if (passwordConfirmation.value.isNullOrBlank() ||
            passwordConfirmation.value != password.value
        ) {
            //show error
            passwordConfirmationError.postValue("Password doesn't match")
            isValid = false
        } else {
            passwordConfirmationError.postValue(null)
        }
        return isValid
    }

    fun navigateToLogin() {
        events.postValue(RegisterViewEvent.NavigateToLogin)
    }


}