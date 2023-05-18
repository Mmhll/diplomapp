package com.ggg.gggapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ggg.gggapp.R
import com.ggg.gggapp.model.User

class UserAdapter(val context: Context) :
    RecyclerView.Adapter<UserAdapter.UserRecyclerViewHolder>() {
    private var users: MutableList<User> = ArrayList()
    private var usersSelected: MutableList<Long> = ArrayList()

    class UserRecyclerViewHolder(itemView: View) : ViewHolder(itemView) {
        var initials: TextView? = null
        var role: TextView? = null
        var checkBox: CheckBox? = null
        var email: TextView? = null

        init {
            initials = itemView.findViewById(R.id.userInitials)
            role = itemView.findViewById(R.id.userRole)
            checkBox = itemView.findViewById(R.id.checkUser)
            email = itemView.findViewById(R.id.userEmail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserRecyclerViewHolder {
        return UserRecyclerViewHolder(
            LayoutInflater.from(context).inflate(R.layout.one_user_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserRecyclerViewHolder, position: Int) {
        if (users.size != 0) {
            val initialsText =
                "${users[position].userData.lastname} ${users[position].userData.firstname} ${users[position].userData.middlename}"
            holder.initials?.text = initialsText
            holder.role?.text = users[position].roles[0].role_name
            holder.email?.text = users[position].email
            holder.checkBox?.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    addUserId(users[position].id)
                } else {
                    deleteUserId(users[position].id)
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

    private fun addUserId(id: Long) {
        usersSelected.add(id)
    }

    private fun deleteUserId(id: Long) {
        usersSelected.remove(id)
    }

    fun getUserIds(): MutableList<Long>{
        return usersSelected
    }
}