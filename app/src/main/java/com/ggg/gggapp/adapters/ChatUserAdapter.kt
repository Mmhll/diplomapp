package com.ggg.gggapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ggg.gggapp.R
import com.ggg.gggapp.model.User

class ChatUserAdapter(val context: Context) :
    RecyclerView.Adapter<ChatUserAdapter.ChatUserRecyclerViewHolder>() {
    private var users: MutableList<User> = ArrayList()

    class ChatUserRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var initials: TextView? = null
        var role: TextView? = null
        var email: TextView? = null

        init {
            initials = itemView.findViewById(R.id.userInitials)
            role = itemView.findViewById(R.id.userRole)
            email = itemView.findViewById(R.id.userEmail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUserRecyclerViewHolder {
        return ChatUserRecyclerViewHolder(
            LayoutInflater.from(context).inflate(R.layout.one_user_without_checkbox_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatUserRecyclerViewHolder, position: Int) {
        if (users.size != 0) {
            val initialsText =
                "${users[position].userData.lastname} ${users[position].userData.firstname} ${users[position].userData.middlename}"
            holder.initials?.text = initialsText
            holder.role?.text = users[position].roles[0].role_name
            holder.email?.text = users[position].email
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun setUsers(users: MutableList<User>) {
        this.users = users
        notifyDataSetChanged()
    }
}