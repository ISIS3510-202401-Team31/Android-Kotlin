package com.example.unifood.model

data class Restaurant(
    val id: String,
    val imageUrl: String,
    val logoUrl: String,
    val name: String,
    val isOpen: Boolean,
    val distance: Double,
    val rating: Double,
    val avgPrice: Double,
    val foodType: String,
    val phoneNumber: String,
    val workingHours: String,
    val likes: Int,
    val address: String,
    val addressDetail: String,
    val latitude: String,
    val longitude: String
)
