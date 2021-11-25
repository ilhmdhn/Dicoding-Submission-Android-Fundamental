package com.idnsoft.githubusers.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.idnsoft.githubusers.R
import com.idnsoft.githubusers.adapter.FavoriteAdapter
import com.idnsoft.githubusers.databinding.ActivityListFavoriteBinding
import com.idnsoft.githubusers.db.DatabaseContract
import com.idnsoft.githubusers.helper.MappingHelper
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

        supportActionBar?.title = getString(R.string.list_favorite)

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
                val cursor = contentResolver.query(
                    DatabaseContract.UserColumns.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.pbFavorite.visibility = View.INVISIBLE
            val user = deferredUser.await()
            if (user.size > 0) {
                adapter.listUserFav = user
            } else {
                adapter.listUserFav = ArrayList()
                Toast.makeText(this@ListFavoriteActivity, getString(R.string.no_data), Toast.LENGTH_LONG).show()
            }
        }
    }

}