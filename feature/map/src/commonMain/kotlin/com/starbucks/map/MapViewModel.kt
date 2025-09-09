package com.starbucks.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbucks.map.model.Coordinates
import kotlinx.coroutines.launch

data class MapScreenState(
    val userCoordinates: Coordinates? = null,
    val loading: Boolean = true,
    val permissionDenied: Boolean = false,
    val selectedCoordinates: Coordinates? = null,
    val selectedAddress: String? = null
)

class MapViewModel : ViewModel() {
    var screenState by mutableStateOf(MapScreenState())
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
        screenState = screenState.copy(
            selectedCoordinates = coords,
            selectedAddress = address
        )
    }
}


// Exceptions
class LocationPermissionException : Exception("Permission denied")