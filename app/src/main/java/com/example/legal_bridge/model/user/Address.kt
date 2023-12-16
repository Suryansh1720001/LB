package com.example.legal_bridge.model.user

data class Address (
    val city : String,
    val state : String,
    val pincode : String,
    val coordinates : Location
)