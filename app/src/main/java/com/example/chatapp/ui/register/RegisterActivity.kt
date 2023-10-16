package com.example.chatapp.ui.register

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityRegisterBinding
import com.example.chatapp.ui.home.HomeActivity
import com.example.chatapp.ui.login.LoginActivity
import com.example.chatapp.ui.showMessage

class RegisterActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.messageLiveData.observe(this) { message ->
            showMessage(
                message = message.message ?: "something went wrong",
                posActionName = "ok",
                posAction = message.onPosActionClick,
                negActionName = message.negActionName,
                negAction = message.onNegActionClick,
                isCancelable = message.isCancelable
            )

        }
        viewModel.events.observe(this, ::handleEvents)
    }

    private fun handleEvents(registerViewEvent: RegisterViewEvent?) {
        when (registerViewEvent) {
            RegisterViewEvent.NavigateToHome -> {
                navigateToHome()
            }

            RegisterViewEvent.NavigateToLogin -> {
                navigateToLogin()
            }

            else -> {}
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun initViews() {
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        viewModel = ViewModelProvider(this)[RegisterViewModel::class.java]
        viewBinding.lifecycleOwner = this
        viewBinding.vm = viewModel

    }

}
