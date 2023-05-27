package com.ggg.gggapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ggg.gggapp.R
import com.ggg.gggapp.model.User


class UserRadioAdapter(private val context: Context) :
    RecyclerView.Adapter<UserRadioAdapter.UserRecyclerViewHolder>() {
    private var users: MutableList<User> = ArrayList()
    private var selectedPosition: Int = -1
    private lateinit var myListener: OnItemClickListener

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    class UserRecyclerViewHolder(itemView: View, listener: OnItemClickListener) :
        ViewHolder(itemView) {
        var initials: TextView? = null
        var role: TextView? = null
        var radioButton: RadioButton? = null
        var email: TextView? = null

        init {
            initials = itemView.findViewById(R.id.userInitials)
            role = itemView.findViewById(R.id.userRole)
            radioButton = itemView.findViewById(R.id.radioUser)
            email = itemView.findViewById(R.id.userEmail)
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRecyclerViewHolder {
        return UserRecyclerViewHolder(
            LayoutInflater.from(context).inflate(R.layout.user_with_radio_button, parent, false),
            myListener
        )
    }

    override fun onBindViewHolder(holder: UserRecyclerViewHolder, position: Int) {
        if (users.size != 0) {
            val initialsText =
                "${users[position].userData.lastname} ${users[position].userData.firstname} ${users[position].userData.middlename}"
            holder.initials?.text = initialsText
            holder.role?.text = users[position].roles[0].role_name
            holder.email?.text = users[position].email

            holder.radioButton?.isChecked = position == selectedPosition
            holder.radioButton?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedPosition = holder.adapterPosition
                    myListener.onItemClick(holder.adapterPosition)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun setUsers(users: MutableList<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    fun getUser(): User? {
        if (selectedPosition != -1) {
            return users[selectedPosition]
        }
        return null
    }
}