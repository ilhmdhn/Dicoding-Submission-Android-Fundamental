package com.idnsoft.githubusers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.idnsoft.githubusers.R
import com.idnsoft.githubusers.databinding.ListFollowersBinding
import com.idnsoft.githubusers.model.User

class FollowersAdapter : RecyclerView.Adapter<FollowersAdapter.FollowersViewHolder>() {

    private val mData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowersViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_followers, parent, false)
        return FollowersViewHolder(mView)
    }

    override fun onBindViewHolder(holder: FollowersViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class FollowersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListFollowersBinding.bind(itemView)
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.followersPhoto)
                .apply(RequestOptions().override(55, 55))
                .into(binding.imgFollowers)

            binding.tvFollowersName.text = user.followersName
        }
    }
}
