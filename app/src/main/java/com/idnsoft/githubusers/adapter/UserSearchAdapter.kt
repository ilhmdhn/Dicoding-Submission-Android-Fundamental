package com.idnsoft.githubusers.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.idnsoft.githubusers.R
import com.idnsoft.githubusers.databinding.ListUserBinding
import com.idnsoft.githubusers.model.User

class UserSearchAdapter : RecyclerView.Adapter<UserSearchAdapter.UserViewHolder>() {

    private val mData = ArrayList<User>()
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserSearchAdapter.UserViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.list_user, parent, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(holder: UserSearchAdapter.UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListUserBinding.bind(itemView)
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.photo)
                .apply(RequestOptions().override(55, 55))
                .into(binding.mainImgAvatar)

            binding.mainTvUsername.text = user.userName
            binding.mainTvUrl.text = user.url

            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}