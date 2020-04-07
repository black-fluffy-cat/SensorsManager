package com.fluffycat.sensorsmanager.navigation_view

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.fragments.ExampleFragment
import com.fluffycat.sensorsmanager.utils.showToast
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MyNavigationItemSelectedListener(private val activity: AppCompatActivity) :
    NavigationView.OnNavigationItemSelectedListener {

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        selectDrawerItem(menuItem)
        return true
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        val menuTitle = menuItem.title.toString()
        val exampleFragment = ExampleFragment(menuTitle)
        activity.apply {
            supportFragmentManager.beginTransaction().replace(R.id.navDrawerFragmentContainer, exampleFragment).commit()
            title = menuTitle
            mainDrawerLayout.closeDrawer(navigation)
        }
    }
}