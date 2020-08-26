package com.blogspot.yourfavoritekaisar.consumerapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.yourfavoritekaisar.consumerapp.R
import com.blogspot.yourfavoritekaisar.consumerapp.helper.CustomOnItemClickListener
import com.blogspot.yourfavoritekaisar.consumerapp.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_user.view.*

class ConsumerAdapter : RecyclerView.Adapter<ConsumerAdapter.ViewHolder>() {

    var listFavorite = ArrayList<User>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return this.listFavorite.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        fun bind(user: User) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.avatars)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_refresh))
                    .error(R.drawable.ic_broken_image)
                    .into(img_user)
                tv_username.text = user.login

                rv_list.setOnClickListener(
                    CustomOnItemClickListener(
                        adapterPosition,
                        object : CustomOnItemClickListener.OnitemClickCallback {
                            override fun onItemClicked(view: View, position: Int) {
                                Toast.makeText(context, "${user.login}", Toast.LENGTH_SHORT).show()
                            }
                        })
                )
            }
        }
    }
}