package com.ggg.gggapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ggg.gggapp.R
import com.ggg.gggapp.model.Chat

class ChatAdapter(val context: Context) :
    RecyclerView.Adapter<ChatAdapter.VH>() {

    private var chats: MutableList<Chat> = ArrayList()

    private lateinit var myListener: OnItemClickListener


    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }


    class VH(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        var chatName: TextView = itemView.findViewById(R.id.chatName)
        var message: TextView = itemView.findViewById(R.id.messageText)
        var initials: TextView = itemView.findViewById(R.id.messageName)
        var time: TextView = itemView.findViewById(R.id.messageTime)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            LayoutInflater.from(context).inflate(R.layout.chat_item, parent, false),
            myListener
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        if (chats.isNotEmpty()) {
            chats.sortByDescending { it.updatedAt }
            if (chats[position].updatedAt == null) {
                holder.time.text = chats[position].createdAt
                holder.message.text = "Чат создан"
            } else {
                holder.time.text = chats[position].updatedAt
                val message = chats[position].messages.last()
                holder.message.text = message.text
                val initials =
                    "${message.user.userData.firstname} ${message.user.userData.lastname}"
                holder.initials.text = initials
            }
            holder.chatName.text = chats[position].title
        }
    }

    fun setChats(chats: ArrayList<Chat>) {
        this.chats = chats
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int {
        return chats.size
    }
}