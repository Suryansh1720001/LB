package com.example.legal_bridge.helper

import android.content.Context
import com.cloudinary.android.MediaManager

object CloudinaryInitializer {
    private var isInitialized = false
//
//    fun init(context: Context) {
//        if (!isInitialized) {
//            val config = hashMapOf(
//                "cloud_name" to "dmeer8vir",
//                "api_key" to "334712144964473",
//                "api_secret" to "WkSCOB5LcMBtY73a0t-AAznlve8"
//            )
//            MediaManager.init(context, config)
//            isInitialized = true
//        }
//    }

    fun init(context: Context) {
        if (!isInitialized) {
            val config = hashMapOf(
                "cloud_name" to APIConstantValue.CLOUD_NAME_CLOUDINARY,
                "api_key" to APIConstantValue.API_KEY_CLOUDINARY,
                "api_secret" to APIConstantValue.API_SECRET_CLOUDINARY
            )
            MediaManager.init(context, config)
            isInitialized = true
        }
    }
}
