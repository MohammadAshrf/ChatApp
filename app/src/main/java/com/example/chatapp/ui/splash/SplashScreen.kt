package com.example.chatapp.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.R
import com.example.chatapp.ui.home.HomeActivity
import com.example.chatapp.ui.login.LoginActivity

class SplashScreen : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        subscribeToLiveData()
        Handler(Looper.getMainLooper())
            .postDelayed({
                viewModel.redirect()
            }, 1300)
    }

    private fun subscribeToLiveData() {
        viewModel.events.observe(this) {
            when (it) {
                SplashViewEvent.NavigateToLogin -> {
                    startLoginActivity()
                }

                SplashViewEvent.NavigateToHome -> {
                    startHomeActivity()
                }

            }
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}