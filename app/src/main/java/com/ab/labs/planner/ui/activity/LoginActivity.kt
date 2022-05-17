package com.ab.labs.planner.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.ab.labs.R
import com.ab.labs.databinding.ActivityLoginBinding

const val USER_ID_PREF = "USER_ID_PREF"

class LoginActivity : BasePermissionActivity(),
    ImageChooserInterface {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityLoginBinding
    private var imageChooserResponse: ((Uri?) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        checkUserId()
    }

    private fun checkUserId() {
        val userId = getSharedPreferences(USER_ID_PREF, 0).getLong(USER_ID_PREF, -1)
        if (userId > 0) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private val imageChooserResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        imageChooserResponse?.invoke(result.data?.data)
    }

    override fun showImageChooser(response: (Uri?) -> Unit) {
        imageChooserResponse = response
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imageChooserResult.launch(intent)
    }

}