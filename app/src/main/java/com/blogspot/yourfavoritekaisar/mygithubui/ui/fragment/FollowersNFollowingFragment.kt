package com.blogspot.yourfavoritekaisar.mygithubui.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.yourfavoritekaisar.mygithubui.R
import com.blogspot.yourfavoritekaisar.mygithubui.adapter.ListAdapter
import com.blogspot.yourfavoritekaisar.mygithubui.model.FollowersViewModel
import com.blogspot.yourfavoritekaisar.mygithubui.model.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_followers_and_following.*

class FollowersNFollowingFragment : Fragment() {

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        const val EXTRA_USERNAME = "extra_username"

        fun newInstance(index: Int): FollowersNFollowingFragment {
            val fragment = FollowersNFollowingFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_SECTION_NUMBER, index)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var adapter: ListAdapter

    private lateinit var followersViewModel: FollowersViewModel

    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followers_and_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var index = 1
        if (arguments != null) {
            index = arguments?.getInt(ARG_SECTION_NUMBER, 0) as Int
        }

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)

        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowersViewModel::class.java)

        when (index) {
            1 -> {
                showListFollowers()
                linkFollowing()
            }
            else -> {
                showListFollowers()
                linkFollowers()
            }
        }
    }

    private fun showListFollowers() {
        adapter = ListAdapter()

        rv_list.layoutManager = LinearLayoutManager(context)
        rv_list.adapter = adapter
        rv_list.setHasFixedSize(true)
    }

    private fun linkFollowers() {
        followersViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowersViewModel::class.java)

        val username = activity?.intent?.getStringExtra(EXTRA_USERNAME)
        showLoading(true)
        followersViewModel.setFollowersUser(username!!)

        followersViewModel.getFollowersUser()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { followersItem ->
                if (followersItem != null) {
                    adapter.setData(followersItem)
                    showLoading(false)
                }
            })
    }

    private fun linkFollowing() {
        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)

        val username = activity?.intent?.getStringExtra(EXTRA_USERNAME)
        followingViewModel.setFollowingUser(username!!)

        followingViewModel.getFollowingUser()
            .observe(viewLifecycleOwner, Observer { followingItem ->
                if (followingItem != null) {
                    adapter.setData(followingItem)
                    showLoading(false)
                }
            })
    }

    private fun showLoading(b: Boolean) {
        if (b) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}