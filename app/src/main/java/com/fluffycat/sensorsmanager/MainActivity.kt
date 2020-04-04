package com.fluffycat.sensorsmanager

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
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
            ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close).apply {
                drawerLayout.addDrawerListener(this)
                syncState()
            }
    }

    private fun setupNavigationView() {
        val navigationView = findViewById<NavigationView>(R.id.navigation)
        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.account -> showToast("My Account")
                R.id.settings -> showToast("Settings")
                R.id.mycart -> showToast("My Cart")
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        actionBarDrawerToggle.apply {
            if (onOptionsItemSelected(item))
                return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}