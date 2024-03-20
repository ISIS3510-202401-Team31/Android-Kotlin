package com.example.unifood

import android.location.Location
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.unifood.repository.LocationRepository
import com.example.unifood.repository.RestaurantRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RestaurantActivity : AppCompatActivity() {
    private lateinit var restaurantRepository: RestaurantRepository
    private lateinit var mostVisitedTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        // Inicializar la vista de texto para mostrar los restaurantes más visitados
        mostVisitedTextView = findViewById(R.id.mostVisitedTextView)

        // Inicializar el repositorio del restaurante
        restaurantRepository = RestaurantRepository()

        // Obtener la ubicación del usuario usando coroutines
        lifecycleScope.launch {
            val locationRepository = LocationRepository()
            val userLocation = locationRepository.getUserLocation(this@RestaurantActivity)

            // Mostrar los restaurantes más visitados
            showMostVisitedRestaurants(userLocation)
        }
    }

    private suspend fun showMostVisitedRestaurants(userLocation: Location?) {
        try {
            // Obtener los restaurantes más visitados desde el repositorio
            val mostVisitedRestaurants = withContext(Dispatchers.IO) {
                restaurantRepository.getMostVisitedRestaurants(5) // Obtener los 5 restaurantes más visitados
            }

            // Construir el texto para mostrar los restaurantes más visitados
            val stringBuilder = StringBuilder()
            mostVisitedRestaurants.forEachIndexed { index, restaurant ->
                stringBuilder.append("${index + 1}. ${restaurant["name"]} (${restaurant["likes"]} likes)\n")
            }

            // Mostrar el texto en el TextView
            mostVisitedTextView.text = stringBuilder.toString()
        } catch (e: Exception) {
            // Manejar cualquier error
            e.printStackTrace()
            // Mostrar un mensaje de error en caso de falla
            mostVisitedTextView.text = "Error al cargar los restaurantes más visitados"
        }
    }
}
