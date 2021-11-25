package com.idnsoft.githubusers.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idnsoft.githubusers.R
import com.idnsoft.githubusers.adapter.UserSearchAdapter
import com.idnsoft.githubusers.databinding.ActivityMainBinding
import com.idnsoft.githubusers.getdata.MainSearchViewModel
import com.idnsoft.githubusers.getdata.url
import com.idnsoft.githubusers.model.User

class MainActivity : AppCompatActivity() {

    private lateinit var searchAdapter: UserSearchAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainSearchViewModel: MainSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        searchAdapter = UserSearchAdapter()
        searchAdapter.notifyDataSetChanged()

        showRecyclerList()

        mainSearchViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainSearchViewModel::class.java)

        mainSearchViewModel.getUser().observe(this, { userItems ->
            if (userItems != null) {
                searchAdapter.setData(userItems)
                showLoading(false)
            }
        })
    }

    private fun showRecyclerList() {
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.adapter = searchAdapter

        searchAdapter.setOnItemClickCallback(object : UserSearchAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                selectedUser(data)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                showLoading(true)
                mainSearchViewModel.setUser(url("search", query.toString()))
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                startActivity(Intent(this, SettingActivity::class.java))
            }
            R.id.menu_list_favorite -> {
                startActivity(Intent(this, ListFavoriteActivity::class.java))
            }
            else -> return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun selectedUser(data: User) {
        val moveIntent = Intent(this, DetailActivity::class.java)
        moveIntent.putExtra(DetailActivity.EXTRA_USER, data)
        startActivity(moveIntent)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.mainProgressBar.visibility = View.VISIBLE
        } else {
            binding.mainProgressBar.visibility = View.GONE
        }
    }
}