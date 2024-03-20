package com.example.unifood.model

data class Users(
    val uid: String,
    val name: String,
    val email: String,
    val profileImageUrl: String? = null
)
