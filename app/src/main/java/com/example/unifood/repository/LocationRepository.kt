package com.example.unifood.repository

import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import android.content.Context
import androidx.core.content.ContextCompat

class LocationRepository {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    suspend fun getUserLocation(context: Context): Location? {
        return withContext(Dispatchers.IO) {
            // Verificar si tenemos permiso de ubicación
            if (hasLocationPermission(context)) {
                try {
                    // Obtener la ubicación del usuario
                    val userLocation = fusedLocationClient.lastLocation.await()
                    userLocation
                } catch (error: SecurityException) {
                    // Manejar excepción de permiso denegado
                    error.printStackTrace()
                    null
                }
            } else {
                // Si no tenemos permiso, devolver null o solicitar permiso al usuario
                null
            }
        }
    }

    private fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
