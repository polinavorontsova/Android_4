package com.ab.labs.planner.ui.activity

import android.net.Uri


interface ImageChooserInterface {

    fun showImageChooser(response: (Uri?) -> Unit)

}