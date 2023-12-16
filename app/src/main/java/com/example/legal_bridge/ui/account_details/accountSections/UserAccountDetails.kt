package com.example.legal_bridge.ui.account_details.accountSections

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.legal_bridge.R
import com.example.legal_bridge.databinding.ActivityEditUserDetailsBinding
import com.example.legal_bridge.databinding.ActivityMainBinding
import com.example.legal_bridge.databinding.ActivityUserAccountDetailsBinding
import com.example.legal_bridge.helper.SharedPreference
import com.example.legal_bridge.helper.SpinnerConstants
import com.example.legal_bridge.helper.utils
import com.squareup.picasso.Picasso

class UserAccountDetails : AppCompatActivity() {
    private lateinit var binding: ActivityUserAccountDetailsBinding
    private lateinit var sharedViewModel: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityUserAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedViewModel = SharedPreference(this)

      addDataTOfield()


        binding?.btnEditButton?.setOnClickListener {
            startActivity(Intent(this@UserAccountDetails,EditUserDetails::class.java))
        }



    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addDataTOfield() {
        binding?.tvUserName?.text = sharedViewModel.fullName
        binding?.tvUserEmail?.text = sharedViewModel.email
        binding?.tvUserMobile?.text = sharedViewModel.phone
        binding?.tvGender?.text = sharedViewModel.gender!!
        binding?.tvDob?.text = utils().convertTimestamp(sharedViewModel.tvDob!!)
        val address = sharedViewModel.city +", " +sharedViewModel.state +"\nPin: "+ sharedViewModel.pincode
        binding?.tvAddress?.text = address

        try {
            if(sharedViewModel.image!=null) {
                val imageUrl = sharedViewModel.image?.replace("http://", "https://")

                Picasso.get()
                    .load(imageUrl)
                    .into(binding?.ivUserProfile)
            }
            else{
                binding?.ivUserProfile?.setImageResource(R.drawable.man_chatbot_image)
            }
        } catch (e: Exception) {
            // Handle exceptions related to image loading
            Log.d("USER","${e.printStackTrace()}")
            e.printStackTrace()
            // You can display a placeholder image or show an error message here
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        // Fetch data from shared view model and populate fields
        if(sharedViewModel.isUserDetailsUpdated) {
            addDataTOfield()
        }

    }

}