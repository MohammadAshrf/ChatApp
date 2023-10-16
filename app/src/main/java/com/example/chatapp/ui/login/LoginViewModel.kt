package com.example.chatapp.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.SessionProvider
import com.example.chatapp.common.SingleLiveEvent
import com.example.chatapp.firestore.UsersDao
import com.example.chatapp.model.User
import com.example.chatapp.ui.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel : ViewModel() {

    val email = MutableLiveData<String>("Mohammad@route.com")
    val emailError = MutableLiveData<String?>()
    val password = MutableLiveData<String>("123456")
    val passwordError = MutableLiveData<String?>()

    val isCreated = MutableLiveData<Boolean>()
    val messageLiveData = SingleLiveEvent<Message>()

    val events = SingleLiveEvent<LoginViewEvent>()

    private val auth = Firebase.auth

    fun login() {

        if (!validForm()) return
        isCreated.value = true
        auth.signInWithEmailAndPassword(email.value!!, password.value!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getUserFromFirestore(task.result.user?.uid)
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

    private fun getUserFromFirestore(uid: String?) {
        UsersDao
            .getUser(uid) { task ->
                isCreated.value = false
                if (task.isSuccessful) {
                    val user = task.result.toObject(User::class.java)
                    SessionProvider.user = user
                    messageLiveData.postValue(
                        Message(
                            message = "Logged in Successfully",
                            posActionName = "ok",
                            onPosActionClick = {
                                //Navigate to home
                                events.postValue(LoginViewEvent.NavigateToHome)
                            },
                            isCancelable = false
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
        return isValid
    }

    fun navigateToRegister() {
        events.postValue(LoginViewEvent.NavigateToRegister)
    }
}