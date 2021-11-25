package com.idnsoft.githubusers.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.idnsoft.githubusers.R
import com.idnsoft.githubusers.adapter.FollowingAdapter
import com.idnsoft.githubusers.getdata.FollowingViewModel
import com.idnsoft.githubusers.getdata.url
import kotlinx.android.synthetic.main.fragment_following.*


class FollowingFragment : Fragment() {

    companion object {
        var EXTRA_USERNAME = "extra_username"

        fun newInstance(username: String): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var followingAdapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followingAdapter = FollowingAdapter()
        followingAdapter.notifyDataSetChanged()

        rv_following.layoutManager = LinearLayoutManager(activity)
        rv_following.adapter = followingAdapter

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
        val username = arguments?.getString(EXTRA_USERNAME)

        followingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)

        followingViewModel.setUser(url("following", username!!))

        followingViewModel.getUser().observe(viewLifecycleOwner, { userItems ->
            if (userItems != null) {
                followingAdapter.setData(userItems)
            }
        })
    }
}
