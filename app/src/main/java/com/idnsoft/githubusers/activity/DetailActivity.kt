package com.idnsoft.githubusers.activity

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.tabs.TabLayout
import com.idnsoft.githubusers.R
import com.idnsoft.githubusers.adapter.SectionsPagerAdapter
import com.idnsoft.githubusers.db.DatabaseContract
import com.idnsoft.githubusers.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.idnsoft.githubusers.getdata.DetailViewModel
import com.idnsoft.githubusers.getdata.url
import com.idnsoft.githubusers.helper.MappingHelper
import com.idnsoft.githubusers.model.User
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var uriWithId: Uri

    private var isFavorite = false
    private var username: String? = null
    private var avatar: String? = null
    private var id: Int? = null
    private var fromFavorite = false

    companion object {
        const val EXTRA_USER = "extra_user"
        const val FROM_FAVORITE = "from_favorite"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        fromFavorite = intent.getBooleanExtra(FROM_FAVORITE, false)

        fb_favorite.setOnClickListener(this)

        username = user.userName
        avatar = user.photo
        id = user.userId

        supportActionBar?.title = username

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        supportActionBar?.elevation = 0f
        sectionsPagerAdapter.username = username

        getUser()

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user.userId)
        val dataFav  = contentResolver?.query(uriWithId, null, null, null, null)
        favoriteCheck(dataFav)

        setStatusFavorite(isFavorite)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (fromFavorite) {
            startActivity(Intent(this, ListFavoriteActivity::class.java))
            finish()
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun getUser() {
        detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        showLoading(true)
        detailViewModel.setUser(url("detail", username!!))

        detailViewModel.getUser().observe(this, { userItems ->
            if (userItems != null) {
                showLoading(false)
                Glide.with(this@DetailActivity)
                    .load(userItems.photo)
                    .apply(RequestOptions().override(90, 90))
                    .into(img_detail_avatar)

                tv_detail_name.text = userItems.name
                tv_detail_email.text = userItems.email
                tv_detail_url.text = userItems.url
                tv_detail_bio.text = userItems.bio

                if (userItems.bio == "null") tv_detail_bio.visibility = View.GONE
                if (userItems.email == "null") tv_detail_email.visibility = View.GONE
                if (userItems.name == "null") tv_detail_name.visibility = View.GONE
                showLoading(false)
            }
        })

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            detail_progress_bar.visibility = View.VISIBLE
        } else {
            detail_progress_bar.visibility = View.GONE
        }
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fb_favorite -> {
                if (isFavorite) {
                    contentResolver.delete(uriWithId, null, null)
                    Toast.makeText(this, "$username "+ getString(R.string.unfavorite), Toast.LENGTH_SHORT).show()
                    isFavorite = false
                } else {
                    val values = ContentValues()
                    values.put(DatabaseContract.UserColumns.USERNAME, username.toString())
                    values.put(DatabaseContract.UserColumns.AVATAR_URL, avatar.toString())
                    values.put(DatabaseContract.UserColumns.USER_ID, id)
                    contentResolver.insert(CONTENT_URI, values)
                    Toast.makeText(this, "$username "+ getString(R.string.favorite), Toast.LENGTH_SHORT).show()
                    isFavorite = true
                }
                setStatusFavorite(isFavorite)
            }
        }
    }

    private fun favoriteCheck(dataFav: Cursor?) {
        val dataFavObject = MappingHelper.mapCursorToArrayList(dataFav)
        for (data in dataFavObject) {
            if (this.id == data.userId) {
                isFavorite = true
            }
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            fb_favorite.setImageResource(R.drawable.ic_unfavorite)
        } else {
            fb_favorite.setImageResource(R.drawable.ic_favorite)
        }
    }
}
