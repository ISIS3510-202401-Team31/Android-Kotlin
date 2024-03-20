package com.example.unifood.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.Query
import java.lang.Exception
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RestaurantRepository {
    private val databaseInstance = FirebaseFirestore.getInstance()

    suspend fun getRestaurants(): List<Map<String, Any>> {
        return try {
            val querySnapshot = databaseInstance.collection("restaurants").get().await()
            querySnapshot.documents.map { doc ->
                val restaurantData = doc.data!!
                restaurantData["docId"] = doc.id
                restaurantData
            }
        } catch (error: Exception) {
            Log.e("RestaurantRepository", "Error fetching menu items: $error")
            throw error
        }
    }

    suspend fun getRestaurantById(restaurantId: String): Map<String, Any>? {
        return try {
            val docSnapshot = databaseInstance.collection("restaurants").document(restaurantId).get().await()
            docSnapshot.data
        } catch (error: Exception) {
            Log.e("RestaurantRepository", "Error fetching restaurant by id: $error")
            throw error
        }
    }

    fun fetchRecommendedRestaurants(userId: String, categoryFilter: String): List<Map<String, Any>> {
        return try {
            val url = URL("http://192.168.0.21:5000/recommend/$userId/$categoryFilter")
            val connection = url.openConnection() as HttpsURLConnection
            connection.connect()

            if (connection.responseCode == HttpsURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                val responseData = JSONObject(response).getJSONArray("data")
                List(responseData.length()) { index ->
                    responseData.getJSONObject(index).toMap()
                }
            } else {
                throw Exception("Failed to load recommended restaurants")
            }
        } catch (error: Exception) {
            Log.e("RestaurantRepository", "Error fetching recommended restaurants: $error")
            throw error
        }
    }

    suspend fun getMostVisitedRestaurants(limit: Int): List<Map<String, Any>> {
        return try {
            val querySnapshot = databaseInstance.collection("restaurants")
                .orderBy("likes", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get().await()

            querySnapshot.documents.map { doc ->
                val restaurantData = doc.data!!
                restaurantData["docId"] = doc.id
                restaurantData
            }
        } catch (error: Exception) {
            Log.e("RestaurantRepository", "Error fetching most visited restaurants: $error")
            throw error
        }
    }

    private fun JSONObject.toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        val keys = this.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = this.get(key)
            map[key] = value
        }
        return map
    }
}