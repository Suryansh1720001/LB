package com.example.legal_bridge.model.user

data class RegisterUserResquest(
    val name: String,
    val email: String,
    val phone: String,
    val dob: String,
    val role: String = "role",
    val password: String,
    val gender: String,
    val pic: String,
    val location: Location,
)