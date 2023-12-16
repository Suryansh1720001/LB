package com.example.legal_bridge.helper

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.View
import com.example.legal_bridge.R
import com.github.ybq.android.spinkit.SpinKitView

class ProgressBarHelper(private val activity: Activity) {
    private val progressBar: SpinKitView = activity.findViewById(R.id.progressBar)

    init {
        progressBar.visibility = View.GONE // Initially hide the progress bar
    }

    fun showProgressBar() {
        // Make the progress bar transparent
        progressBar.indeterminateDrawable.setColorFilter(Color.parseColor("#80FFFFFF"), PorterDuff.Mode.SRC_IN)
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

}
