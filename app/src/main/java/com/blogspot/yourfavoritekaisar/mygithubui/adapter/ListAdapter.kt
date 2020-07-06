package com.blogspot.yourfavoritekaisar.mygithubui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.yourfavoritekaisar.mygithubui.R
import com.blogspot.yourfavoritekaisar.mygithubui.data.model.User
import com.blogspot.yourfavoritekaisar.mygithubui.ui.detail.DetailActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    private val mData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(user: User) {
            with(itemView) {
                Glide.with(context)
                    .load(user.avatars)
                    .into(img_user)

                tv_user.text = user.login
                itemView.setOnClickListener {
                    Intent(context, DetailActivity::class.java).apply {
                        putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                        putExtra(DetailActivity.EXTRA_AVATAR_URL, user.avatars)
                        putExtra(DetailActivity.EXTRA_TYPE, user.type)
                    }.run {
                        context.startActivity(this)
                    }
                }
            }
        }
    }
}