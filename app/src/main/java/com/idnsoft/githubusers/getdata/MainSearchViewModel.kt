package com.idnsoft.githubusers.getdata

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.idnsoft.githubusers.BuildConfig
import com.idnsoft.githubusers.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject


class MainSearchViewModel : ViewModel() {

    val listUser = MutableLiveData<ArrayList<User>>()

    fun setUser(url: String) {
        val listItem = ArrayList<User>()
        val client = AsyncHttpClient()
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = String(responseBody!!)
                try {
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val listUser = list.getJSONObject(i)
                        val user = User()

                        user.userName = listUser.getString("login")
                        user.url = listUser.getString("html_url")
                        user.photo = listUser.getString("avatar_url")
                        user.userId = listUser.getInt("id")

                        listItem.add(user)
                        Log.d("check4", listUser.toString())
                    }
                    listUser.postValue(listItem)
                    Log.d("check3", listItem.toString())
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d("error", errorMessage)
            }

        })
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return listUser
    }
}