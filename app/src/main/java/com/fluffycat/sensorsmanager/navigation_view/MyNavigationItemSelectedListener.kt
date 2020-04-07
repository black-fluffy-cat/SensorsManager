package com.fluffycat.sensorsmanager.navigation_view

import android.content.Context
import android.view.MenuItem
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.utils.showToast
import com.google.android.material.navigation.NavigationView

class MyNavigationItemSelectedListener(private val context: Context) :
    NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        showToast(context, item.title.toString())
        return true
    }
}