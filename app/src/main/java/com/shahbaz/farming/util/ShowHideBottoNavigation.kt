package com.shahbaz.farming.util

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.shahbaz.farming.DashboardActivity
import com.shahbaz.farming.R

fun Fragment.showBottomNavigationBar(){
    val bottomNav = (activity as DashboardActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    bottomNav.visibility = View.VISIBLE
}



fun Fragment.hideBottomNavigationBar(){
    val bottomNav = (activity as DashboardActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    bottomNav.visibility = View.GONE
}