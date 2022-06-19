package com.example.gratefulnote

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp

class MainActivity : AppCompatActivity() {
    private lateinit var navController : NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = findNavController(R.id.nav_host)

        // parameter pertama : biar top destination itu yang paling awal di navigation graph
        // parameter kedua : biar up button nanti punnya fungsionalitas dari drawerlayout (muncul bergeser) kalo dipencet pada di top destination
        appBarConfiguration = AppBarConfiguration(navController.graph , findViewById<DrawerLayout>(R.id.drawer_layout))

        // biar ada upbutton/drawer button
        NavigationUI.setupActionBarWithNavController(this , navController , appBarConfiguration)
    }

    override fun onSupportNavigateUp() : Boolean{
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.set_new_password)
            Toast.makeText(this , "Mencoba ngeset password" , Toast.LENGTH_SHORT).show()
        return super.onOptionsItemSelected(item)
    }

}