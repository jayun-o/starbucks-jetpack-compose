package com.starbucks.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbucks.data.domain.CustomerRepository
import com.starbucks.shared.domain.Coordinates
import com.starbucks.shared.util.RequestState
import kotlinx.coroutines.launch

data class MapScreenState(
    val userCoordinates: Coordinates? = null,
    val loading: Boolean = true,
    val permissionDenied: Boolean = false,
    val selectedCoordinates: Coordinates? = null,
    val selectedAddress: String? = null
)

class MapViewModel(
    private val customerRepository: CustomerRepository,
) : ViewModel() {
    var screenReady: RequestState<Unit> by mutableStateOf(RequestState.Loading)
    var screenState: MapScreenState by mutableStateOf(MapScreenState())
        private set

    init {
        fetchCurrentLocation()
    }


    private fun fetchCurrentLocation() {
        viewModelScope.launch {
            try {
                val coords = getCurrentLocation() // expect function
                screenState = screenState.copy(
                    userCoordinates = coords,
                    loading = false
                )
            } catch (e: LocationPermissionException) {
                screenState = screenState.copy(
                    permissionDenied = true,
                    loading = false
                )
            } catch (e: Exception) {
                screenState = screenState.copy(
                    userCoordinates = Coordinates(13.7563, 100.5017),
                    loading = false
                )
            }
        }
    }

    fun selectLocation(coords: Coordinates, address: String) {
        println("Selected Location -> lat: ${coords.latitude}, lng: ${coords.longitude}, address: $address")
        screenState = screenState.copy(
            selectedCoordinates = coords,
            selectedAddress = address
        )
    }
}


// Exceptions
class LocationPermissionException : Exception("Permission denied")


