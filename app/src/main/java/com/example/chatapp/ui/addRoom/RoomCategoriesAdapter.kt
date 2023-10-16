package com.example.chatapp.ui.addRoom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.chatapp.databinding.ItemRoomCategoryBinding
import com.example.chatapp.model.Category

class RoomCategoriesAdapter(private val items: List<Category>) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        if (view == null) {
            //create view holder
            val itemBinding = ItemRoomCategoryBinding
                .inflate(
                    LayoutInflater.from(parent?.context),
                    parent, false
                )
            viewHolder = ViewHolder(itemBinding)
            itemBinding.root.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        // bind
        viewHolder.bind(items[position])

        return viewHolder.itemBinding.root
    }


    class ViewHolder(val itemBinding: ItemRoomCategoryBinding) {
        fun bind(item: Category) {
            itemBinding.image.setImageResource(item.imageResId)
            itemBinding.title.text = item.title
        }
    }

}
