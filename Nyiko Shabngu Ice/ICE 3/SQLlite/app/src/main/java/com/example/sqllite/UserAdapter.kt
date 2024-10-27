package com.example.sqllite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sqllite.databinding.ListItemUserBinding

class UserAdapter(
    private var users: List<DatabaseHelper.User>,
    private val listener: UserClickListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    interface UserClickListener {
        fun onUserClick(user: DatabaseHelper.User)
        fun onUserLongClick(user: DatabaseHelper.User)
    }

    inner class UserViewHolder(private val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: DatabaseHelper.User) {
            binding.apply {
                tvUserName.text = user.name
                tvUserEmail.text = user.email

                root.setOnClickListener { listener.onUserClick(user) }
                root.setOnLongClickListener {
                    listener.onUserLongClick(user)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ListItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size

    fun updateUsers(newUsers: List<DatabaseHelper.User>) {
        users = newUsers
        notifyDataSetChanged()
    }
}