package com.shahbaz.farming

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.provider.Settings
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.shahbaz.farming.databinding.ActivityDashboardBinding
import com.shahbaz.farming.permission.isLocationEnabled
import com.shahbaz.farming.util.Constant.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.weather.WeatherViewmodel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (isLocationEnabled(this)) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.green)
            binding = ActivityDashboardBinding.inflate(layoutInflater)
            setContentView(binding.root)
            setSupportActionBar(binding.myToolbar)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }


            //this is the function placed in the location permission file
            if (com.shahbaz.farming.permission.checkPermission(this@DashboardActivity)) {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
                val navController = navHostFragment.navController
                binding.bottomNavigationView.setupWithNavController(navController)

                setupNavigationdrawer()


                binding.myToolbar.setNavigationOnClickListener {
                    if (binding.main.isDrawerOpen(GravityCompat.START)) {
                        binding.main.closeDrawer(GravityCompat.START)
                    } else {
                        binding.main.openDrawer(GravityCompat.START)
                    }
                }

                binding.navigationview.setNavigationItemSelectedListener {
                    when (it.itemId) {
                        R.id.logout -> {
                            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                        }
                    }
                    binding.main.closeDrawer(GravityCompat.START)
                    return@setNavigationItemSelectedListener true
                }
            }


        } else {
            showLocationServicesDialog()
        }

    }


    private fun setupNavigationdrawer() {
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this@DashboardActivity,
            binding.main,
            R.string.nav_open,
            R.string.nav_close
        )
        binding.main.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else return super.onOptionsItemSelected(item)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can now fetch the location
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.homeFragment) // Ensure you navigate to HomeFragment
            } else {
                com.shahbaz.farming.permission.checkPermission(this)
            }
        }
    }


    private fun showLocationServicesDialog() {
        AlertDialog.Builder(this)
            .setTitle("Enable Location Services")
            .setMessage("Location services are required to use this app. Please enable location services ,Restart the App")
            .setPositiveButton("Enable") { dialog, _ ->
                // Redirect user to location settings
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                finish() // Close the app if user cancels
            }
            .setCancelable(false)
            .show()
    }


}