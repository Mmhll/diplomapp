package com.ggg.gggapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ggg.gggapp.R
import com.ggg.gggapp.model.Chat
import com.ggg.gggapp.model.User

class ChatAdapter(val chats: ArrayList<Chat>, val context: Context) :
    RecyclerView.Adapter<ChatAdapter.VH>() {

    private var array = ArrayList<User>()


    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.messageText)
        var initials: TextView = itemView.findViewById(R.id.chatName)
        var time: TextView = itemView.findViewById(R.id.messageTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {

    }

    override fun getItemCount(): Int {
        return chats.size
    }
}