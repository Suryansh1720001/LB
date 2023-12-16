package com.example.legal_bridge.helper

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.legal_bridge.ui.account_details.accountSections.UserAccountDetails
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class utils {
    private var overlay: View? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertTimestamp(timestamp: String): String {
        // Define the input and output date time formatters
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        // Parse the input timestamp string into LocalDateTime object
        val dateTime = LocalDateTime.parse(timestamp, inputFormatter)

        // Format the LocalDateTime object to the desired output format
        return dateTime.format(outputFormatter)
    }


//    fun showErrorInDialog(context: Context, mess: String) {
//        val builder = AlertDialog.Builder(context)
//        val message = mess
//        builder.apply {
//            setMessage(message)
//            setPositiveButton("OK") { dialog, _ ->
//                dialog.dismiss()
//            }
//        }.create().show()
//    }




    fun createTransparentOverlay(activity: Activity) {
        // Check if overlay already exists to avoid duplicates
        if (overlay == null) {
            overlay = View(activity)
            overlay?.apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setBackgroundColor(Color.TRANSPARENT)
                isClickable = true // Intercept touch events
            }

            // Add the overlay to the root layout of the activity
            (activity.window.decorView as ViewGroup).addView(overlay)
        }
    }

//    fun removeTransparentOverlay(activity: Activity) {
//        overlay?.let {
//            // Remove the overlay from the root layout
//            (activity.window.decorView as ViewGroup).removeView(it)
//            overlay = null // Reset overlay reference
//        }
//    }

//    fun removeTransparentOverlay(activity: Activity) {
//        Log.d("Overlay", "Removing overlay") // Add this line for debugging
//
//        overlay?.let {
//            // Remove the overlay from the root layout
//            (activity.window.decorView as ViewGroup).removeView(it)
//            overlay = null // Reset overlay reference
//            Log.d("Overlay", "Overlay removed successfully") // Add this line for debugging
//        }
//    }


    fun removeTransparentOverlay(activity: Activity) {
        Log.d("Overlay", "Removing overlay") // Add this line for debugging

        activity.runOnUiThread {
            overlay?.let {
                val rootView = activity.window.decorView as ViewGroup
                rootView.removeView(it)
                overlay = null // Reset overlay reference

                // Ensure that the activity's views become clickable again
                rootView.isClickable = true
                rootView.isFocusable = true
                rootView.isFocusableInTouchMode = true

                Log.d("Overlay", "Overlay removed successfully") // Add this line for debugging
            }
        }
    }





}