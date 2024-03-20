package com.example.unifood.utils

import kotlin.math.*

object DistanceCalculator {

    fun calculateDistanceInKm(userLat: Double, userLong: Double,
                              restaurantLat: Double, restaurantLong: Double): Double {
        val radiusOfEarth = 6371000
        val latDistance = degreesToRadians(restaurantLat - userLat)
        val longDistance = degreesToRadians(restaurantLong - userLong)
        val a = (sin(latDistance / 2) * sin(latDistance / 2) +
                cos(degreesToRadians(userLat)) *
                cos(degreesToRadians(restaurantLat)) *
                sin(longDistance / 2) * sin(longDistance / 2))
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return radiusOfEarth * c / 1000
    }

    private fun degreesToRadians(degrees: Double): Double {
        return degrees * (PI / 180)
    }
}
