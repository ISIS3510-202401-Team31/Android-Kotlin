import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.unifood.R
import com.example.unifood.repository.LocationRepository
import kotlinx.coroutines.launch

class RestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        // Obtener la ubicación del usuario usando coroutines
        lifecycleScope.launch {
            val locationRepository = LocationRepository()
            val userLocation = locationRepository.getUserLocation(this@RestaurantActivity)

            // Aquí puedes manejar la ubicación del usuario
            userLocation?.let {
                // Haz algo con la ubicación obtenida
            }
        }
    }
}
