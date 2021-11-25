package com.idnsoft.favoriteapp.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.idnsoft.favoriteapp.R
import com.idnsoft.favoriteapp.databinding.ListFavoriteBinding
import com.idnsoft.favoriteapp.model.User

class FavoriteAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteAdapter.UserViewHolder>() {

    var listUserFav = ArrayList<User>()
        set(listUserFav) {
            if (listUserFav.size > 0) {
                this.listUserFav.clear()
            }
            this.listUserFav.addAll(listUserFav)
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): FavoriteAdapter.UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_favorite, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.UserViewHolder, position: Int) {
        holder.bind(listUserFav[position])
    }

    override fun getItemCount(): Int {
        return this.listUserFav.size
    }


    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListFavoriteBinding.bind(itemView)
        fun bind(userFav: User) {
            binding.tvListFavName.text = userFav.userName

            Glide.with(itemView.context)
                .load(userFav.photo)
                .into(binding.imgListFavAvatar)

        }
    }

}
