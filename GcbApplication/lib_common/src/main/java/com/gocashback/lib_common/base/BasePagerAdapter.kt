package com.gocashback.lib_common.base

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * 通用pagerAdapter
 *
 * @Author 55HAITAO
 * @Date 2019-05-14 13:39
 */
class BasePagerAdapter(fm: FragmentManager, private var fragmentList: List<Fragment>, private var tabs: List<String>) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs[position]
    }
}