package com.example.gratefulnote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var navController : NavController
    private lateinit var drawerLayout : DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navController = findNavController(R.id.nav_host)


        // biar ada upbutton(untuk di non-start destination) dan drawer button (di start destination)
        // start destination bisa dilihat di navigation.xml, di bagian app:startDestination
        NavigationUI.setupActionBarWithNavController(this , navController , drawerLayout)

        // biar bisa navigate sesuai id dari menu item
        NavigationUI.setupWithNavController(findViewById<NavigationView>(R.id.nav_view) , navController)

        navController.addOnDestinationChangedListener{
            nc , nd , _ ->
            drawerLayout.setDrawerLockMode(
                if (nd.id == nc.graph.startDestinationId)
                    DrawerLayout.LOCK_MODE_UNLOCKED
                else
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            )
        }
    }

    override fun onSupportNavigateUp() =
        NavigationUI.navigateUp(navController , drawerLayout)


}