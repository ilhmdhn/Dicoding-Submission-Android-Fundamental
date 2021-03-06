package com.idnsoft.githubusers.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.idnsoft.githubusers.R
import com.idnsoft.githubusers.fragment.FollowersFragment
import com.idnsoft.githubusers.fragment.FollowingFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    var username: String? = null

    @StringRes
    private val TAB_TITLES = arrayOf(R.string.following, R.string.followers)
    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowingFragment.newInstance(username!!)
            1 -> fragment = FollowersFragment.newInstance(username!!)
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}