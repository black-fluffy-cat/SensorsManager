package com.fluffycat.sensorsmanager.activities

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.fluffycat.sensorsmanager.R
import com.fluffycat.sensorsmanager.navigation_view.MyNavigationItemSelectedListener
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBarDrawerToggle()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupNavigationView()
    }

    private fun setupActionBarDrawerToggle() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.mainDrawerLayout)
        actionBarDrawerToggle =
            ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.open,
                R.string.close
            ).apply {
                drawerLayout.addDrawerListener(this)
                syncState()
            }
    }

    private fun setupNavigationView() {
        val navigationView = findViewById<NavigationView>(R.id.navigation)
        navigationView.setNavigationItemSelectedListener(
            MyNavigationItemSelectedListener(this)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) return true
        return super.onOptionsItemSelected(item)
    }
}