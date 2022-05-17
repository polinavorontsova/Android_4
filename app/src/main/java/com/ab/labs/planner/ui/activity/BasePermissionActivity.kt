package com.ab.labs.planner.ui.activity

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ab.labs.R

abstract class BasePermissionActivity : AppCompatActivity() {

    private val READ_EXTERNAL_STORAGE_CODE = 1001
    //private val CAMERA_CODE = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupPermissions()
    }

    private fun setupPermissions() {
        setupPermission(
            READ_EXTERNAL_STORAGE,
            READ_EXTERNAL_STORAGE_CODE,
            R.string.perm_read_external_storage_message
        )
        /*
        setupPermission(
            CAMERA,
            CAMERA_CODE,
            R.string.perm_camera_message
        )
         */
    }

    private fun setupPermission(name: String, code: Int, @StringRes messageRes: Int) {
        if (checkSelfPermission(name) != PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(name)) {
                AlertDialog.Builder(this)
                    .setMessage(messageRes)
                    .setTitle(R.string.permission_requied)
                    .setPositiveButton("OK") { _, _ -> requestPermissions(arrayOf(name), code) }
                    .create()
                    .show()
            } else {
                requestPermissions(arrayOf(name), code)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            READ_EXTERNAL_STORAGE_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PERMISSION_GRANTED) {
                    //toast("READ_EXTERNAL_STORAGE permission has been denied by user")
                } else {
                    //toast("READ_EXTERNAL_STORAGE permission has been granted by user")
                }
            }
            /*
            CAMERA_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    //toast("CAMERA permission has been denied by user")
                } else {
                    //toast("CAMERA permission has been granted by user")
                }
            }
             */
        }
    }

}



