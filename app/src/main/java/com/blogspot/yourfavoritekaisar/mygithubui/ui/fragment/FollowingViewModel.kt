package com.blogspot.yourfavoritekaisar.mygithubui.ui.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blogspot.yourfavoritekaisar.mygithubui.BuildConfig
import com.blogspot.yourfavoritekaisar.mygithubui.data.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingViewModel : ViewModel() {

//    companion object {
//        private const val TOKEN = "token 1f8d83c7d2b55d56bdbf1db91da561649086563f"
//    }

    val listFollowing = MutableLiveData<ArrayList<User>>()

    fun setFollowingUser(username: String) {
        val listItem = ArrayList<User>()
        val client = AsyncHttpClient()

        val url = BuildConfig.BASE_URL + "users/$username/following"
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
                        val following = responseArray.getJSONObject(i)
                        val followingItem =
                            User()
                        followingItem.apply {
                            login = following.getString("login")
                            avatars = following.getString("avatar_url")
                            type = following.getString("type")
                        }
                        listItem.add(followingItem)
                    }
                    listFollowing.postValue(listItem)

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

    fun getFollowingUser(): LiveData<ArrayList<User>> {
        return listFollowing
    }
}