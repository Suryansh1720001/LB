package com.example.legal_bridge.model.user
data class EditUserDetails (
    val name: String,
    val dob: String,
    val role: String = "role",
    val gender: String,
    val pic: String,
    val address: Address,
)