package com.example.unifood.repository

import android.util.Log
import com.example.unifood.model.Plate
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

    fun fetchRecommendedRestaurants(userId: String?, categoryFilter: String): List<Map<String, Any>> {
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

    suspend fun getUserFavoriteDishes(userId: String?): List<Plate> {
        val userFavorites = mutableListOf<Plate>()
        try {
            // Verificar si userId es nulo
            if (userId != null) {
                // Obtener los restaurantes visitados por el usuario
                val visitedRestaurants = getUserVisitedRestaurants(userId)

                // Iterar sobre los restaurantes visitados
                for (restaurantId in visitedRestaurants) {
                    // Obtener los platos del restaurante
                    val plates = getRestaurantPlates(restaurantId)

                    // Obtener los likes del restaurante
                    val restaurantLikes = getRestaurantLikes(restaurantId)

                    // Filtrar los platos favoritos del usuario
                    val favoritePlates = plates.filter { plate ->
                        // Verificar si el restaurante tiene suficientes likes
                        restaurantLikes >= 10 // Considerar un plato favorito si el restaurante tiene al menos 3 likes
                    }
                    userFavorites.addAll(favoritePlates)
                }
            } else {
                Log.e("RestaurantRepository", "User ID is null")
                // Manejar la situación donde userId es nulo
                // Por ejemplo, lanzar una excepción o retornar una lista vacía
                // Aquí lanzaremos una excepción IllegalArgumentException
                throw IllegalArgumentException("User ID is null")
            }
        } catch (error: Exception) {
            Log.e("RestaurantRepository", "Error getting user favorite dishes: $error")
            throw error
        }
        return userFavorites
    }



    private suspend fun getRestaurantPlates(restaurantId: String): List<Plate> {
        return try {
            // Obtener los platos del restaurante utilizando su ID
            val docSnapshot = databaseInstance.collection("restaurants").document(restaurantId).collection("plates").get().await()
            val plates = mutableListOf<Plate>()
            for (plateDoc in docSnapshot.documents) {
                val plate = plateDoc.toObject(Plate::class.java)
                plate?.let {
                    plates.add(it)
                }
            }
            plates
        } catch (error: Exception) {
            // Manejar cualquier error
            error.printStackTrace()
            throw error
        }
    }


    private suspend fun getUserVisitedRestaurants(userId: String): List<String> {
        val visitedRestaurants = mutableListOf<String>()
        try {
            val reviewsSnapshot = databaseInstance.collection("reviews")
                .whereEqualTo("userId", userId)
                .get().await()

            for (reviewDoc in reviewsSnapshot.documents) {
                val restaurantId = reviewDoc.getString("restaurantId")
                if (restaurantId != null && !visitedRestaurants.contains(restaurantId)) {
                    visitedRestaurants.add(restaurantId)
                }
            }
        } catch (error: Exception) {
            Log.e("RestaurantRepository", "Error fetching user visited restaurants: $error")
            throw error
        }
        return visitedRestaurants
    }

    private suspend fun getRestaurantDetails(restaurantId: String): Map<String, Any> {
        return try {
            // Obtener los detalles del restaurante utilizando su ID
            val docSnapshot = databaseInstance.collection("restaurants").document(restaurantId).get().await()
            docSnapshot.data ?: throw Exception("Restaurant details not found")
        } catch (error: Exception) {
            // Manejar cualquier error
            error.printStackTrace()
            throw error
        }
    }

    private suspend fun getRestaurantLikes(restaurantId: String): Int {
        return try {
            // Obtener los detalles del restaurante utilizando su ID
            val docSnapshot = databaseInstance.collection("restaurants").document(restaurantId).get().await()
            val restaurantData = docSnapshot.data ?: throw Exception("Restaurant details not found")

            // Obtener los likes del restaurante
            val likes = restaurantData["likes"] as? Int ?: 0
            likes
        } catch (error: Exception) {
            // Manejar cualquier error
            error.printStackTrace()
            throw error
        }
    }


}
