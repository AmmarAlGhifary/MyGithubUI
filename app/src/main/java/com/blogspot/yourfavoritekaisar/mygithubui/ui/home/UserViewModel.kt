package com.blogspot.yourfavoritekaisar.mygithubui.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blogspot.yourfavoritekaisar.mygithubui.BuildConfig
import com.blogspot.yourfavoritekaisar.mygithubui.data.model.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class UserViewModel : ViewModel() {

//    companion object {
//        private const val TOKEN = "token 1f8d83c7d2b55d56bdbf1db91da561649086563f"
//    }

    val userList = MutableLiveData<ArrayList<User>>()

    fun setUser(username: String) {
        val listItem = ArrayList<User>()

        val url = BuildConfig.BASE_URL + "search/users?q=$username"
        val token = BuildConfig.TOKEN

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token $token")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                try {
                    val result = String(responseBody!!)
                    val responseObject = JSONObject(result)
                    val item = responseObject.getJSONArray("items")

                    for (i in 0 until item.length()) {
                        val user = item.getJSONObject(i)
                        val userItems =
                            User()
                        userItems.login = user.getString("login")
                        userItems.avatars = user.getString("avatar_url")
                        userItems.type = user.getString("type")
                        listItem.add(userItems)

                    }
                    userList.postValue(listItem)
                } catch (e: Exception){
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                Log.d("onFailure", error!!.message.toString())
            }

        })
    }

    fun getUser(): LiveData<ArrayList<User>>{
        return userList
    }
}