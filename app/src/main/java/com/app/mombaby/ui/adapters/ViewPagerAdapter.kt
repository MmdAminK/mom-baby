package com.app.mombaby.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private var fragmentsList: ArrayList<Fragment> = ArrayList()

    fun addFragment(fragment: Fragment): ViewPagerAdapter {
        fragmentsList.add(fragment)
        return this
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentsList[position]
    }

    override fun getItemCount(): Int {
        return fragmentsList.size
    }


}
