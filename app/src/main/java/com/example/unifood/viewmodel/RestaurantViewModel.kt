package com.example.unifood.viewmodel

import android.content.Context
import android.location.Location
import android.util.Log
import com.example.unifood.model.Plate
import com.example.unifood.model.Restaurant
import com.example.unifood.repository.LocationRepository
import com.example.unifood.repository.RestaurantRepository
import com.example.unifood.utils.DistanceCalculator

class RestaurantViewModel(private val context: Context) {
    private val restaurantRepository = RestaurantRepository()
    private val locationRepository = LocationRepository()


    suspend fun getRestaurants(): List<Restaurant> {
        return getRestaurantData(getUserLocation(context))
    }

    suspend fun getRestaurantById(restaurantName: String): Restaurant? {
        val userLocation = getUserLocation(context)
        val data = restaurantRepository.getRestaurantById(restaurantName)
        return data?.let { mapSingleRestaurantData(it, userLocation) }
    }

    suspend fun getRestaurantsNearby(): List<Restaurant> {
        val userLocation = getUserLocation(context)
        val data = restaurantRepository.getRestaurants()
        return filterNearbyRestaurants(data, userLocation)
    }

    suspend fun getRecommendedRestaurants(userId: String?, categoryFilter: String): List<Restaurant> {
        val data = restaurantRepository.fetchRecommendedRestaurants(userId, categoryFilter)
        val userLocation = getUserLocation(context)
        return mapRestaurantData(data, userLocation)
    }

    suspend fun getMostVisitedRestaurants(limit: Int): List<Restaurant> {
        val data = restaurantRepository.getMostVisitedRestaurants(limit)
        val userLocation = getUserLocation(context)
        return mapRestaurantData(data, userLocation)
    }

    suspend fun getUserFavoriteDishes(userId: String?): List<Plate> {
        return restaurantRepository.getUserFavoriteDishes(userId)
    }


    private suspend fun getUserLocation(context: Context): Location {
        try {
            return locationRepository.getUserLocation(context) ?: throw IllegalStateException("User location not found")
        } catch (error: Exception) {
            Log.e("RestaurantViewModel", "Error getting user location: $error")
            throw error
        }
    }

    private suspend fun getRestaurantData(userLocation: Location): List<Restaurant> {
        try {
            val data = restaurantRepository.getRestaurants()
            return mapRestaurantData(data, userLocation)
        } catch (error: Exception) {
            Log.e("RestaurantViewModel", "Error fetching menu items: $error")
            throw error
        }
    }

    private fun mapRestaurantData(data: List<Map<String, Any>>, userLocation: Location): List<Restaurant> {
        return data.map { mapSingleRestaurantData(it, userLocation) }
    }

    private fun mapSingleRestaurantData(item: Map<String, Any>, userLocation: Location): Restaurant {
        val restaurantLat = item["latitud"].toString().toDouble()
        val restaurantLong = item["longitud"].toString().toDouble()
        val distance = DistanceCalculator.calculateDistanceInKm(userLocation.latitude, userLocation.longitude, restaurantLat, restaurantLong)

        return Restaurant(
            id = item["docId"].toString(),
            imageUrl = item["imageURL"].toString(),
            logoUrl = item["logoURL"].toString(),
            name = item["name"].toString(),
            isOpen = item["isOpen"] as? Boolean ?: false,
            distance = distance,
            rating = item["rating"]?.toString()?.toDouble() ?: 0.0,
            avgPrice = item["avgPrice"]?.toString()?.toDouble() ?: 0.0,
            foodType = item["foodType"].toString(),
            phoneNumber = item["phoneNumber"].toString(),
            workingHours = item["workingHours"].toString(),
            likes = item["likes"]?.toString()?.toInt() ?: 0,
            address = item["address"].toString(),
            addressDetail = item["addressDetail"].toString(),
            latitude = item["latitud"].toString(),
            longitude = item["longitud"].toString()
        )
    }

    private fun filterNearbyRestaurants(data: List<Map<String, Any>>, userLocation: Location): List<Restaurant> {
        return data.filter { isNearbyRestaurant(it, userLocation) }.map { mapSingleRestaurantData(it, userLocation) }
    }

    private fun isNearbyRestaurant(item: Map<String, Any>, userLocation: Location): Boolean {
        val restaurantLat = item["latitud"].toString().toDouble()
        val restaurantLong = item["longitud"].toString().toDouble()
        val distance = DistanceCalculator.calculateDistanceInKm(userLocation.latitude, userLocation.longitude, restaurantLat, restaurantLong)
        return distance <= 30.5
    }
}
