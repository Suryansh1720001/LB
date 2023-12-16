package com.example.legal_bridge.model.user

data class UserResponse(
    val _id: String,
    val role: String,
    val name: String,
    val email: String,
    val phone: String,
    val dob: String,
    val age: Int,
    val gender: String,
    val password: String,
    val pic: String,
    val address : Address,
    val token: String,
    val message : String,
)