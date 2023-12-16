package com.example.legal_bridge.model.PhoneVerification.OTPVerification

data class OTPverifyRequest (
    val phoneNumber: String,
    val userOTP : String,
)