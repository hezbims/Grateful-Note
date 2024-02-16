package com.example.gratefulnote

import android.app.AlarmManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController : NavController
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var alarmManager: AlarmManager
    private lateinit var exactAlarmPermissionRequestLauncher : ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarmManager = getSystemService()!!
        drawerLayout = findViewById(R.id.drawer_layout)
        navController = findNavController(R.id.nav_host)
        exactAlarmPermissionRequestLauncher =  registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if (result.resultCode == RESULT_OK)
                navController.navigate(R.id.notificationSettingsFragment)
        }
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
                R.id.notificationSettingsFragment -> {
                    // Untuk android dibawah S, kita udah langsung bisa ngegunain exact alarm
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                        !alarmManager.canScheduleExactAlarms()){
                        exactAlarmPermissionRequestLauncher.launch(
                            Intent(
                                Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
                                Uri.parse("package:$packageName")
                            ),
                        )
                    } else {
                        navController.navigate(it.itemId)
                    }
                }
                R.id.backupRestoreFragment -> navController.navigate(R.id.backupRestoreFragment)
            }
            false
        }
    }

    override fun onSupportNavigateUp() =
        NavigationUI.navigateUp(navController, drawerLayout)
}