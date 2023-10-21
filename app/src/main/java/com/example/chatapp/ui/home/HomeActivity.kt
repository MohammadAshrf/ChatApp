package com.example.chatapp.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityHomeBinding
import com.example.chatapp.model.Room
import com.example.chatapp.ui.Constants
import com.example.chatapp.ui.addRoom.AddRoomActivity
import com.example.chatapp.ui.chat.ChatActivity
import com.example.chatapp.ui.login.LoginActivity
import com.example.chatapp.ui.showLoadingProgressDialog
import com.example.chatapp.ui.showMessage

class HomeActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        initViews()
        subscribeToLiveData()
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadRooms()
    }

    private var loadingDialog: android.app.AlertDialog? = null

    private fun subscribeToLiveData() {
        viewModel.events.observe(this, ::handleEvent)
        viewModel.roomsLiveData.observe(this) {
            adapter.changeData(it)
        }
        viewModel.loadingLiveData.observe(this) {
            if (it == null) {
                //hide
                loadingDialog?.dismiss()
                loadingDialog = null
                return@observe
            }
            //show
            loadingDialog = showLoadingProgressDialog(
                message = it.message ?: "",
                isCancelable = it.isCancelable
            )
            loadingDialog?.show()
        }
        viewModel.messageLiveData.observe(this) { message ->
            showMessage(
                message.message ?: "",
                posActionName = message.posActionName,
                posAction = message.onPosActionClick,
                negActionName = message.negActionName,
                negAction = message.onNegActionClick,
                isCancelable = message.isCancelable
            )
        }
    }

    private fun handleEvent(homeViewEvent: HomeViewEvent?) {
        when (homeViewEvent) {
            HomeViewEvent.NavigateToAddRoom -> {
                navigateToAddRoom()
            }

            HomeViewEvent.NavigateToLogin -> {
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

    private fun navigateToAddRoom() {
        val intent = Intent(this, AddRoomActivity::class.java)
        startActivity(intent)
    }

    private val adapter = RoomsRecyclerAdapter()
    private fun initViews() {
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this
        viewBinding.content.roomsRecycler.adapter = adapter
        adapter.onItemClickListener = RoomsRecyclerAdapter.OnItemClickListener { position, room ->
            navigateToRoom(room)
        }
    }

    private fun navigateToRoom(room: Room) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(Constants.EXTRA_ROOM, room)
        startActivity(intent)
    }
}