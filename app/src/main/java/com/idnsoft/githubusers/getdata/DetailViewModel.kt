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

class DetailViewModel : ViewModel() {

    private val userDetail = MutableLiveData<User>()

    fun setUser(url: String) {
        val client = AsyncHttpClient()
//        client.addHeader("Authorization", "token a4f1e971e220f0f331ba1b42958c77814ec40186")
        client.addHeader("Authorization", BuildConfig.GITHUB_TOKEN)
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?
            ) {
                val result = String(responseBody!!)
                try {
                    val user = User()
                    val responseObject = JSONObject(result)
                    user.name = responseObject.getString("name")
                    user.url = responseObject.getString("url")
                    user.email = responseObject.getString("email")
                    user.bio = responseObject.getString("bio")
                    user.photo = responseObject.getString("avatar_url")

                    Log.d("testuname", user.name.toString())

                    userDetail.postValue(user)
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

    fun getUser(): LiveData<User> {
        return userDetail
    }

}