package com.shahbaz.farming

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessaging
import com.shahbaz.farming.databinding.ActivityDashboardBinding
import com.shahbaz.farming.datamodel.User
import com.shahbaz.farming.permission.isLocationEnabled
import com.shahbaz.farming.util.Constant.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.shahbaz.farming.util.OuthToken
import com.shahbaz.farming.util.Resources
import com.shahbaz.farming.util.progressDialgoue
import com.shahbaz.farming.util.showDialogue
import com.shahbaz.farming.viewmodel.HomeFragmentViewmodel
import com.shahbaz.farming.viewmodel.languageSelectionViewmodel
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private val homeFragmentViewmodel by viewModels<HomeFragmentViewmodel>()
    private var selectedProfileUrl = ""
    private lateinit var progressDialog: ProgressDialog
    private val languageChangeViewmodel by viewModels<languageSelectionViewmodel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.statusBarColor = ContextCompat.getColor(this, R.color.green)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        lifecycleScope.launch(Dispatchers.IO) {
            val accessToken = OuthToken.getAccessToken()
            Log.d("Access Token", accessToken.toString())
        }

        progressDialog = ProgressDialog(this)

        setupNavigationdrawer()
        homeFragmentViewmodel.getCurrentUserDetails()
        observeCurrentUserDetails()
        observeProfileChange()

        if (isLocationEnabled(this)) {
            //this is the function placed in the location permission file
            if (com.shahbaz.farming.permission.checkPermission(this@DashboardActivity)) {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
                val navController = navHostFragment.navController
                binding.bottomNavigationView.setupWithNavController(navController)

                binding.myToolbar.setNavigationOnClickListener {
                    if (binding.main.isDrawerOpen(GravityCompat.START)) {
                        binding.main.closeDrawer(GravityCompat.START)
                    } else {
                        binding.main.openDrawer(GravityCompat.START)
                    }
                }
            }

            binding.navigationview.setNavigationItemSelectedListener {
                when (it.itemId) {

                    R.id.profile -> {
                        navigateToFragmentfromDrawer(R.id.profileFragment)
                    }

                    R.id.lang -> {
                        showDialogueWithRadioButton("Change Language", "Select Language")
                    }

                    R.id.logout -> {
                        homeFragmentViewmodel.signOut()
                        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                        goToMainActivity()
                    }

//                    R.id.weather -> {
//                        navigateToFragmentfromDrawer(R.id.weatherFragment)
//                    }

                    R.id.sellProduct -> {
                        navigateToFragmentfromDrawer(R.id.addProductFragment)
                    }

                    R.id.seeYourProduct -> {
                        navigateToFragmentfromDrawer(R.id.yourProductFragment)
                    }

                    R.id.cartItem -> {
                        navigateToFragmentfromDrawer(R.id.cartFragment)
                    }

                    R.id.orderedItem -> {
                        navigateToFragmentfromDrawer(R.id.orderedFragment)
                    }

                    R.id.orderedrecevied -> {
                        navigateToFragmentfromDrawer(R.id.orderReceivedFragment)
                    }
                }
                binding.main.closeDrawer(GravityCompat.START)
                return@setNavigationItemSelectedListener true
            }

        } else {
            showLocationServicesDialog()
        }
    }


    private fun observeProfileChange() {
        lifecycleScope.launch {
            homeFragmentViewmodel.updateProfileStatus.collect {
                when (it) {
                    is Resources.Error -> {
                        Toast.makeText(this@DashboardActivity, it.message, Toast.LENGTH_SHORT)
                            .show()

                        progressDialog.hide()
                    }

                    is Resources.Loading -> {

                    }

                    is Resources.Success -> {
                        val headerView = binding.navigationview.getHeaderView(0)
                        val profile = headerView.findViewById<CircleImageView>(R.id.profile_image)

                        if (it.data != null) {
                            Glide.with(this@DashboardActivity)
                                .load(it.data)
                                .into(profile)
                        }
                        progressDialog.hide()


                    }

                    is Resources.Unspecified -> {

                    }
                }
            }
        }
    }

    private fun observeCurrentUserDetails() {
        lifecycleScope.launch {
            homeFragmentViewmodel.userDetailState.collect {
                when (it) {
                    is Resources.Error -> {
                        Toast.makeText(this@DashboardActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }

                    is Resources.Loading -> {

                    }

                    is Resources.Success -> {
                        val data = it.data
                        data.let { user ->
                            setUpNavHeaderdata(user!!)
                        }

                    }

                    is Resources.Unspecified -> {

                    }
                }
            }
        }
    }

    private fun setUpNavHeaderdata(data: User) {
        val headerview = binding.navigationview.getHeaderView(0)
        val name = headerview.findViewById<TextView>(R.id.profileName)
        name.text = data.name
        val email = headerview.findViewById<TextView>(R.id.email)
        email.text = data.email
        val profileImage = headerview.findViewById<CircleImageView>(R.id.profile_image)

        if (data.profileUrl != "") {
            Glide.with(this).load(data.profileUrl).into(profileImage)
        }

        profileImage.setOnClickListener {
            showDialogue(
                this@DashboardActivity,
                "Profile Picture",
                "Choose to change profile picture",
                "Change Photo",
                "Cancel",
                onClick = {
                    selectImage()
                }
            )
        }


    }


    private fun setupNavigationdrawer() {
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this@DashboardActivity,
            binding.main,
            R.string.nav_open,
            R.string.nav_close
        )
        actionBarDrawerToggle.getDrawerArrowDrawable()
            .setColor(getResources().getColor(R.color.white));

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
        grantResults: IntArray,
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
                // Permission denied, show a dialog or take appropriate action
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


    private fun goToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun selectImage() {
        selectedProfileLauncher.launch("image/*")
    }


    private val selectedProfileLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedProfileUrl = it.toString()
            progressDialgoue(
                progressDialog,
                "Uploading Image...",
                "Upload In Progress..."
            )
            homeFragmentViewmodel.updateProfile(selectedProfileUrl)
        }

    }

    fun navigateToFragmentfromDrawer(fragmentId: Int) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController
        if (fragmentId == R.id.addProductFragment) {
            val data = Bundle().apply {
                putParcelable("productUpdate", null)
            }
            navController.navigate(fragmentId, data)
        } else {
            navController.navigate(fragmentId)
        }

    }

    fun showDialogueWithRadioButton(
        title: String,
        message: String,
    ) {
        // List of available languages
        val languages = arrayOf("English", "Hindi")

        val selectedLanguage = languageChangeViewmodel.getSelectedLanguage(this)
        var selectedLanguageIndex = languages.indexOf(selectedLanguage).takeIf { it >= 0 }?: 0

        // Create the AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)

        // Display languages as radio button list
        builder.setSingleChoiceItems(languages, selectedLanguageIndex) { _, which ->
            selectedLanguageIndex = which // Update the selected language index
        }

        // Handle the OK button click
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(
                this, // Context should be correct here
                "Selected Language: ${languages[selectedLanguageIndex]}",
                Toast.LENGTH_SHORT
            ).show()
            // You can now handle the language change here, for example:
            // changeLanguage(selectedLanguageIndex)
            changeLanguage(selectedLanguageIndex)
            languageChangeViewmodel.saveSelectedLanguage(this, languages[selectedLanguageIndex])
        }

        // Show the dialog
        val dialog = builder.create()
        dialog.show()
    }

    private fun changeLanguage(languageIndex: Int) {
        val locale = when (languageIndex) {
            1 -> Locale("hi") // Hindi
            else -> Locale("en") // English
        }

        // Update the app's locale
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        createConfigurationContext(config) // Update the configuration context
        resources.updateConfiguration(config, resources.displayMetrics)

        // Restart the activity to reflect the changes
        val intent = Intent(this, this::class.java)
        finish()
        startActivity(intent)
    }

}