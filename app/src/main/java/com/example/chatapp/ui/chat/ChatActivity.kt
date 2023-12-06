package com.example.chatapp.ui.chat

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityChatBinding
import com.example.chatapp.model.Room
import com.example.chatapp.ui.Constants

class ChatActivity : AppCompatActivity() {
    lateinit var viewBinding: ActivityChatBinding
    val viewModel: ChatViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initParams()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() {
        viewModel.toastLiveData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
        }
        viewModel.roomName
            .observe(this) {

            }
        viewModel.newMessagesLiveData
            .observe(this) {
                messagesAdapter.addNewMessages(it)
                viewBinding.content.messagesRecycler.smoothScrollToPosition(
                    messagesAdapter.itemCount
                )
            }
    }

    private fun initParams() {
        val room = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(Constants.EXTRA_ROOM, Room::class.java)
        } else {
            intent.getParcelableExtra(Constants.EXTRA_ROOM) as Room?
        }
        viewModel.changeRoom(room)
        viewBinding.invalidateAll()
    }

    private val messagesAdapter = MessagesAdapter(mutableListOf())
    private fun initViews() {
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat)
        viewBinding.vm = viewModel
        viewBinding.lifecycleOwner = this

        viewBinding.content.messagesRecycler.adapter = messagesAdapter
        (viewBinding.content.messagesRecycler.layoutManager as LinearLayoutManager)
            .stackFromEnd = true
    }
}