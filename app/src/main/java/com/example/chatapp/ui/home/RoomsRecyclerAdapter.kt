package com.example.chatapp.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.ItemRoomBinding
import com.example.chatapp.model.Category
import com.example.chatapp.model.Room

class RoomsRecyclerAdapter(private var rooms: List<Room>? = listOf()) :
    RecyclerView.Adapter<RoomsRecyclerAdapter.ViewHolder>() {

    class ViewHolder(private val itemBinding: ItemRoomBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(room: Room?) {
            itemBinding.categoryImage.setImageResource(Category.getCategoryImageByCategoryId(room?.categoryId))
            itemBinding.title.text = room?.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = ItemRoomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = rooms?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(rooms?.get(position))
    }

    fun changeData(rooms: List<Room>?) {
        this.rooms = rooms
        notifyDataSetChanged()
    }


}