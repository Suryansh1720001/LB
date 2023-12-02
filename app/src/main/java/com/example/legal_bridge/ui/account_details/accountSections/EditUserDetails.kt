package com.example.legal_bridge.ui.account_details.accountSections

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.legal_bridge.R
import com.example.legal_bridge.databinding.ActivityEditUserDetailsBinding
import com.example.legal_bridge.databinding.ActivityMainBinding

class EditUserDetails : AppCompatActivity() {
    private lateinit var binding: ActivityEditUserDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)





    }
}