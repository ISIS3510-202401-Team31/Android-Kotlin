package com.example.unifood

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.unifood.model.ProfileModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var locationTextView: TextView
    private lateinit var profileModel: ProfileModel


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        locationTextView = findViewById(R.id.location)

        profileModel = ProfileModel(this)

        val preferencesTextView: TextView = findViewById(R.id.textPreferences)

        preferencesTextView.setOnClickListener{
            val intent = Intent(this, PreferencesActivity::class.java)
            startActivity(intent)
        }

        // Solicitar permisos de ubicación si no están otorgados
        if (!hasLocationPermission()) {
            requestLocationPermission()
        } else {
            getLocation()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE &&
            grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        ) {
            getLocation()
        } else {
            locationTextView.text = "Location permission denied"
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val location = profileModel.getLastKnownLocation()

        if (location != null) {
            val latitude = location.latitude
            val longitude = location.longitude
            locationTextView.text = "Location: Latitud: $latitude, Longitud: $longitude"
        } else {
            locationTextView.text = "Location not available"
        }
    }
}
