package com.example.chatapp.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.SessionProvider
import com.example.chatapp.databinding.ItemRecieveMessageBinding
import com.example.chatapp.databinding.ItemSentMessageBinding
import com.example.chatapp.model.Message

class MessagesAdapter(private var messages: MutableList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        if (message.senderId == SessionProvider.user?.id) {
            return MessageType.Sent.value
        } else {
            return MessageType.Received.value
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == MessageType.Sent.value) {
            val itemBinding = ItemSentMessageBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return SentMessageViewHolder(itemBinding)
        } else {
            val itemBinding = ItemRecieveMessageBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
            return ReceivedMessageViewHolder(itemBinding)
        }
    }

    override fun getItemCount(): Int = messages.size
    fun addNewMessages(newMessages: List<Message>?) {
        if (newMessages == null) return
        val oldSize = messages.size
        messages.addAll(newMessages)
        notifyItemRangeInserted(oldSize, newMessages.size)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SentMessageViewHolder -> {
                holder.bind(messages[position])
            }

            is ReceivedMessageViewHolder -> {
                holder.bind(messages[position])
            }
        }
    }


}

enum class MessageType(val value: Int) {
    Received(200),
    Sent(100)
}

class SentMessageViewHolder(val itemBinding: ItemSentMessageBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(message: Message) {
        itemBinding.setMessage(message)
        itemBinding.executePendingBindings()
    }
}

class ReceivedMessageViewHolder(val itemBinding: ItemRecieveMessageBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {
    fun bind(message: Message) {
        itemBinding.setMessage(message)
        itemBinding.executePendingBindings()
    }
}
