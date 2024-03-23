package com.example.unifood.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat

class ProfileRepository(private val context: Context) {

    fun getLastKnownLocation(): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Verificar permisos
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Si los permisos no están otorgados, puedes manejar este caso de diferentes maneras.
            // En este ejemplo, simplemente devolvemos null, pero puedes pedir permisos aquí,
            // o mostrar un mensaje al usuario solicitando permisos, o manejar de otra manera.
            return null
        }

        // Obtener la última ubicación conocida
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
    }


}
