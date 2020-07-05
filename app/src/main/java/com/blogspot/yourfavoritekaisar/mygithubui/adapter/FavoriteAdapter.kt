package com.blogspot.yourfavoritekaisar.mygithubui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.yourfavoritekaisar.mygithubui.R
import com.blogspot.yourfavoritekaisar.mygithubui.data.model.UserFavorite
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_favorite.view.*
import java.util.*

class FavoriteAdapter(private val listener: (UserFavorite) -> Unit) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    var listFavoriteUser = ArrayList<UserFavorite>()
        set(listFavoriteUser) {
            if (listFavoriteUser.size > 0) {
                this.listFavoriteUser.clear()
            }
            this.listFavoriteUser.addAll(listFavoriteUser)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = this.listFavoriteUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavoriteUser[position], listener)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favoriteUser: UserFavorite, listener: (UserFavorite) -> Unit) {
            with(itemView) {
                tv_favorite.text = favoriteUser.login
                Glide.with(context)
                    .load(favoriteUser.avatar_url)
                    .apply(
                        RequestOptions()
                            .override(56, 56)
                            .placeholder(R.drawable.ic_baseline_person_24)
                            .error(R.drawable.ic_baseline_person_24)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .priority(Priority.HIGH)
                    )
                    .into(img_favorite)
                setOnClickListener {
                    listener(favoriteUser)
                }
            }
        }
    }
}