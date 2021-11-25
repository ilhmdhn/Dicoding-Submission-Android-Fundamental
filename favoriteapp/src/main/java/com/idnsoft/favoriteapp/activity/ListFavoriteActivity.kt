package com.idnsoft.favoriteapp.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.idnsoft.favoriteapp.adapter.FavoriteAdapter
import com.idnsoft.favoriteapp.databinding.ActivityListFavoriteBinding
import com.idnsoft.favoriteapp.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.idnsoft.favoriteapp.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListFavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteAdapter
    private lateinit var binding: ActivityListFavoriteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title ="List Favorite"

        binding.rvListFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvListFavorite.setHasFixedSize(true)

        adapter = FavoriteAdapter(this)
        binding.rvListFavorite.adapter = adapter


        loadUserFavAsync()
    }

    private fun loadUserFavAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.pbFavorite.visibility = View.VISIBLE
            val deferredUser = async(Dispatchers.IO) {

                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.pbFavorite.visibility = View.INVISIBLE
            val user = deferredUser.await()
            if (user.size > 0){
                adapter.listUserFav = user
            } else {
                adapter.listUserFav = ArrayList()
                Toast.makeText(this@ListFavoriteActivity, "Tidak ada data", Toast.LENGTH_LONG).show()
            }
        }
    }
}