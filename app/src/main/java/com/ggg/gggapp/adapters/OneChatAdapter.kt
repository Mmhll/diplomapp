package com.ggg.gggapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ggg.gggapp.R
import com.ggg.gggapp.model.Message
import com.ggg.gggapp.utils.makeDateTimeValid


class OneChatAdapter(private val id: Long) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM_LEFT = 1
    private val ITEM_RIGHT = 2

    private var chatMessages: MutableList<Message> = ArrayList()


    override fun getItemViewType(position: Int): Int {
        return if (chatMessages[position].user.id == id) 2 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return when (viewType) {
            ITEM_LEFT -> LeftChatViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.chatleft_item, parent, false)
            )
            ITEM_RIGHT -> RightChatViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.chatright_item, parent, false)
            )
            else -> {
                LeftChatViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.chatleft_item, parent, false)
                )
            }
        }
    }


    override fun getItemCount(): Int {
        return chatMessages.size
    }

    fun addMessages(messages: MutableList<Message>) {
        this.chatMessages = messages
        notifyDataSetChanged()
    }


    fun getMessages(): MutableList<Message> {
        return chatMessages
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message: Message = chatMessages[position]
        if (holder.itemViewType == ITEM_LEFT) {
            val viewHolder: LeftChatViewHolder = holder as LeftChatViewHolder
            val initials = "${message.user.userData.firstname} ${message.user.userData.lastname}"
            viewHolder.receiver.text = initials
            viewHolder.content.text = message.text
            viewHolder.time.text = makeDateTimeValid(message.created_at)
        } else {
            val viewHolder: RightChatViewHolder = holder as RightChatViewHolder
            viewHolder.content.text = message.text
            viewHolder.time.text = makeDateTimeValid(message.created_at)

        }
    }
}

class RightChatViewHolder(itemView: View) : ChatViewHolder(itemView) {
    var content: TextView
    var time: TextView

    init {
        content = itemView.findViewById(R.id.chatItem_right_text)
        time = itemView.findViewById(R.id.chatItem_right_time)
    }
}

class LeftChatViewHolder(itemView: View) : ChatViewHolder(itemView) {
    var content: TextView
    var receiver: TextView
    var time: TextView

    init {
        receiver = itemView.findViewById(R.id.chatItem_left_receiver)
        content = itemView.findViewById(R.id.chatItem_left_text)
        time = itemView.findViewById(R.id.chatItem_left_time)
    }
}

open class ChatViewHolder(itemView: View?) : RecyclerView.ViewHolder(
    itemView!!
)