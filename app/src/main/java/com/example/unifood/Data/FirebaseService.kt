package com.example.unifood.Data

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseService(private val context: Context) {
    private val _database: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getDatabase(): FirebaseFirestore {
        return _database
    }
}
