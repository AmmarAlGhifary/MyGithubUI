package com.blogspot.yourfavoritekaisar.mygithubui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.yourfavoritekaisar.mygithubui.R
import com.blogspot.yourfavoritekaisar.mygithubui.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_detail.view.*

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.ViewHolder>() {

    private val mData = ArrayList<User>()

    fun setData(item : ArrayList<User>) {
        mData.clear()
        mData.addAll(item)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    class ViewHolder(item : View) : RecyclerView.ViewHolder(item) {
        fun bind(userDetail : User) {
            with(itemView) {
                Glide.with(context)
                    .load(userDetail.avatars)
                    .into(imgUserDetail)
                tvName.text = userDetail.name
                tvCompany.text = userDetail.company
                tvFollowers.text = userDetail.followers.toString()
                tvFollowing.text = userDetail.following.toString()
                tvLocation.text = userDetail.location
                textFollowers.text = resources.getString(R.string.followers)
                textFollowing.text = resources.getString(R.string.following)            }
        }
    }
}