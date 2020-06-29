package com.blogspot.yourfavoritekaisar.mygithubui.ui.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blogspot.yourfavoritekaisar.mygithubui.BuildConfig
import com.blogspot.yourfavoritekaisar.mygithubui.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowersViewModel : ViewModel() {

//    companion object {
//        private const val TOKEN = "token 1f8d83c7d2b55d56bdbf1db91da561649086563f"
//    }

    val listFollower = MutableLiveData<ArrayList<User>>()

    fun setFollowersUser(username: String) {
        val listItem = ArrayList<User>()
        val client = AsyncHttpClient()

        val url = BuildConfig.BASE_URL + "users/$username/followers"
        val token = BuildConfig.TOKEN

        client.addHeader("Authorization", "token $token")
        client.addHeader("User-agent", "request")

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val followers = responseArray.getJSONObject(i)
                        val followersItem = User()
                        followersItem.login = followers.getString("login")
                        followersItem.avatars = followers.getString("avatar_url")
                        followersItem.type = followers.getString("type")
                        listItem.add(followersItem)
                    }
                    listFollower.postValue(listItem)

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
                Log.d("onFailure", error!!.message.toString())
            }
        })
    }

    fun getFollowersUser(): LiveData<ArrayList<User>> {
        return listFollower
    }
}