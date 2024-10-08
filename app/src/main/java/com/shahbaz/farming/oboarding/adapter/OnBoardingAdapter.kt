package com.shahbaz.farming.oboarding.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingAdapter(val list :ArrayList<Fragment>,fragmentManger:FragmentManager,lifecycle: Lifecycle):FragmentStateAdapter(fragmentManger,lifecycle) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}