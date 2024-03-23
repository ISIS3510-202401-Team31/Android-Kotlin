package com.example.unifood.repository

import android.content.Context
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class PreferencesRepository() {
    private val databaseInstance = FirebaseFirestore.getInstance()

    suspend fun getPreferences(): Map<String, Any> {
        return try {
            val querySnapshot = databaseInstance.collection("preferences").document("oqwTeOFRkxL6VPPrO9vH").get().await()
            querySnapshot.data as Map<String,Any>

        } catch (error: Exception) {
            Log.e("DietRestrictionsRepository", "Error fetching menu items: $error")
            println(error)
            throw error
        }
    }
}