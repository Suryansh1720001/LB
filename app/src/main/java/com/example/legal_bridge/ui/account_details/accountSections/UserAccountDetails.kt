package com.example.legal_bridge.ui.account_details.accountSections

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.legal_bridge.R
import com.example.legal_bridge.databinding.ActivityEditUserDetailsBinding
import com.example.legal_bridge.databinding.ActivityMainBinding
import com.example.legal_bridge.databinding.ActivityUserAccountDetailsBinding
import com.example.legal_bridge.helper.SharedPreference
import com.example.legal_bridge.helper.SpinnerConstants

class UserAccountDetails : AppCompatActivity() {
    private lateinit var binding: ActivityUserAccountDetailsBinding
    private lateinit var sharedViewModel: SharedPreference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =ActivityUserAccountDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedViewModel = SharedPreference(this)

        Toast.makeText(this@UserAccountDetails,"${sharedViewModel.gender!!}",Toast.LENGTH_LONG).show()
        Toast.makeText(this@UserAccountDetails,"${ SpinnerConstants.gender[sharedViewModel.gender!!]}",Toast.LENGTH_LONG).show()

        binding?.tvUserName?.text = sharedViewModel.fullName
        binding?.tvUserEmail?.text = sharedViewModel.email
        binding?.tvUserMobile?.text = sharedViewModel.phone
        binding?.tvGender?.text =  SpinnerConstants.gender[sharedViewModel.gender!!]
        binding?.tvDob?.text = sharedViewModel.tvDob




    }
}