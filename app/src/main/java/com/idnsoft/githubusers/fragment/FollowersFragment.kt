package com.idnsoft.githubusers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idnsoft.githubusers.R
import com.idnsoft.githubusers.adapter.FollowersAdapter
import com.idnsoft.githubusers.getdata.FollowersViewModel
import com.idnsoft.githubusers.getdata.url
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {


    companion object {
        var EXTRA_USERNAME = "extra_username"

        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var followersAdapter: FollowersAdapter
    private lateinit var followersViewModel: FollowersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        followersAdapter = FollowersAdapter()
        followersAdapter.notifyDataSetChanged()

        rv_followers.layoutManager = LinearLayoutManager(activity)
        rv_followers.adapter = followersAdapter

        followersViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)

        val username = arguments?.getString(EXTRA_USERNAME)
        followersViewModel.setUser(url("followers", username!!))

        followersViewModel.getUser().observe(viewLifecycleOwner, { userItems ->
            if (userItems != null) {
                followersAdapter.setData(userItems)
            }
        })
    }
}