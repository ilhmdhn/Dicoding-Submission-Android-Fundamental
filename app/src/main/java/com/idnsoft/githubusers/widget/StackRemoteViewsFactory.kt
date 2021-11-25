package com.idnsoft.githubusers.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.idnsoft.githubusers.R
import com.idnsoft.githubusers.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.idnsoft.githubusers.db.UserHelper
import com.idnsoft.githubusers.helper.MappingHelper
import java.lang.Exception
import java.lang.IllegalStateException

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private lateinit var userHelper: UserHelper
    private val widgetItem = ArrayList<Bitmap>()

    override fun onCreate() {
        userHelper = UserHelper.getInstance(mContext)
        if (!userHelper.isOpen()) userHelper.open()
    }

    override fun onDataSetChanged() {
        userHelper = UserHelper.getInstance(mContext)
        if (!userHelper.isOpen()) userHelper.open()

        val identifyToken = Binder.clearCallingIdentity()
        Binder.restoreCallingIdentity(identifyToken)

        try {
            val cursorSearch = userHelper.queryAll()
            val cursor = MappingHelper.mapCursorToArrayList(cursorSearch)
            if (cursor.size > 0) {
                widgetItem.clear()
                for (i in 0 until cursor.size) {
                    try {
                        val avatar = cursor[i].photo.toString()
                        val bitmap = Glide.with(mContext)
                            .asBitmap()
                            .load(avatar)
                            .submit()
                            .get()
                        widgetItem.add(bitmap)
                    } catch (e : Exception){
                        widgetItem.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_baseline_account_box_24))
                    }
                }
            }
        } catch (e : IllegalStateException){
            Log.d("Widget_Error", e.message.toString())
        }
    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int = widgetItem.size

    override fun getViewAt(position: Int): RemoteViews {
        val remote = RemoteViews(mContext.packageName, R.layout.favorite_item)
        remote.setImageViewBitmap(R.id.imageView, widgetItem[position])

        val extras = bundleOf(UserFavoriteWidget.EXTRA_ITEM to position)

        val fillIntent = Intent()
        fillIntent.putExtras(extras)

        remote.setOnClickFillInIntent(R.id.imageView, fillIntent)
        return remote
    }


    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}