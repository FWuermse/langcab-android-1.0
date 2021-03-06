package com.langcab.app

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

    lateinit var toggle: ActionBarDrawerToggle

    lateinit var toolbar: Toolbar

    // TODO outsource some code here
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.navView)

        toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)


        toggle.syncState()

        setSupportActionBar(findViewById(R.id.toolBar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.add_item -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.to_add)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.list_item -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.to_list)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.train_item -> {
                    findNavController(R.id.nav_host_fragment).navigate(R.id.to_train)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            true
        }

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                1)
    }

    // Show actions menu (including search and menu button)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Modify toggle when user clicks on manu button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true

        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }
}
