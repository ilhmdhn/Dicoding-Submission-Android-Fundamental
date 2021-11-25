package com.idnsoft.githubusers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.idnsoft.githubusers.R
import com.idnsoft.githubusers.databinding.ListFollowingBinding
import com.idnsoft.githubusers.model.User

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    private val mData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_following, parent, false)
        return FollowingViewHolder(mView)
    }

    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListFollowingBinding.bind(itemView)
        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.followingPhoto)
                .apply(RequestOptions().override(55, 55))
                .into(binding.imgFollowing)

            binding.tvFollowingName.text = user.followingName
        }
    }
}
