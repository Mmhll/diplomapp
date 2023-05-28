package com.ggg.gggapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ggg.gggapp.R
import com.ggg.gggapp.model.User

class UserClickableAdapter(val context: Context) :
    RecyclerView.Adapter<UserClickableAdapter.UserRecyclerClickableViewHolder>() {
    private var users: MutableList<User> = ArrayList()

    private lateinit var myListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    class UserRecyclerClickableViewHolder(listener: OnItemClickListener, itemView: View) :
        ViewHolder(itemView) {
        var initials: TextView = itemView.findViewById(R.id.userInitials)
        var role: TextView = itemView.findViewById(R.id.userRole)
        var email: TextView = itemView.findViewById(R.id.userEmail)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserRecyclerClickableViewHolder {
        return UserRecyclerClickableViewHolder(
            myListener,
            LayoutInflater.from(context)
                .inflate(R.layout.one_user_without_checkbox_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserRecyclerClickableViewHolder, position: Int) {
        if (users.size != 0) {
            val initialsText =
                "${users[position].userData.lastname} ${users[position].userData.firstname} ${users[position].userData.middlename}"
            holder.initials.text = initialsText
            holder.role.text = users[position].roles[0].role_name
            holder.email.text = users[position].email
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