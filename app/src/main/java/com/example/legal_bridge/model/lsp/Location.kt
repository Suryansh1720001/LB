package com.example.legal_bridge.model.lsp

data class Location(
    val city: String,
    val state: String,
    val pincode: Int,
    val coordinates: Coordinates
)