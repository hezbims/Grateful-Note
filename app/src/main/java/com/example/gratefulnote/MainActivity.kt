package com.example.gratefulnote

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView

const val STORAGE_PERMISSION_REQUEST_CODE = 0

class MainActivity : AppCompatActivity() {
    private lateinit var navController : NavController
    private lateinit var drawerLayout : DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        navController = findNavController(R.id.nav_host)
        val navView = findViewById<NavigationView>(R.id.nav_view)


        // biar ada upbutton(untuk di non-start destination) dan drawer button (di start destination)
        // start destination bisa dilihat di navigation.xml, di bagian app:startDestination
        NavigationUI.setupActionBarWithNavController(this , navController , drawerLayout)

        navController.addOnDestinationChangedListener{
            nc , nd , _ ->
            drawerLayout.setDrawerLockMode(
                if (nd.id == nc.graph.startDestinationId)
                    DrawerLayout.LOCK_MODE_UNLOCKED
                else
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            )
        }

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.feedback -> {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data =
                            Uri.parse(getString(R.string.email_uri)) // only email apps should handle this
                        putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email_address)))
                        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject))
                    }
                    try {
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(
                            applicationContext,
                            "Ada bug, lapor ke : ${getString(R.string.email_address)}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                R.id.notificationSettingsFragment -> navController.navigate(it.itemId)
                R.id.backupRestoreFragment -> navController.navigate(R.id.backupRestoreFragment)
            }
            false
        }
    }

    override fun onSupportNavigateUp() =
        NavigationUI.navigateUp(navController, drawerLayout)


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_REQUEST_CODE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                navController.navigate(R.id.backupRestoreFragment)
            else
                Toast.makeText(
                    application ,
                    "Tolong izinkan akses penyimpanan!" ,
                    Toast.LENGTH_LONG
                ).show()
        }
    }
}