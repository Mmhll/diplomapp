package com.ggg.gggapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ggg.gggapp.R
import com.ggg.gggapp.model.Role
import com.ggg.gggapp.utils.PermissionName
import com.ggg.gggapp.utils.permissionNameAscii

class RoleAdapter(private val context: Context) :
    RecyclerView.Adapter<RoleAdapter.RoleViewHolder>() {
    private lateinit var myListener: OnItemClickListener
    private var roles: MutableList<Role> = ArrayList()

    fun setOnItemClickListener(listener: OnItemClickListener) {
        myListener = listener
    }

    class RoleViewHolder(listener: OnItemClickListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var roleName: TextView = itemView.findViewById(R.id.itemRoleName)
        var permission: TextView = itemView.findViewById(R.id.itemPermission)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        return RoleViewHolder(
            myListener,
            LayoutInflater.from(context).inflate(R.layout.role_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        holder.roleName.text = roles[position].role_name
        var permission = roles[position].permissions.name
        when (permission) {
            PermissionName.ROLE_USER.name -> permission = permissionNameAscii[0]
            PermissionName.ROLE_EDITUSER.name -> permission = permissionNameAscii[1]
            PermissionName.ROLE_MODERATOR.name -> permission = permissionNameAscii[2]
            PermissionName.ROLE_ADMIN.name -> permission = permissionNameAscii[3]
        }
        holder.permission.text = permission
    }

    override fun getItemCount(): Int {
        return roles.size
    }

    fun setRoles(roles: MutableList<Role>) {
        this.roles = roles
        notifyDataSetChanged()
    }
}