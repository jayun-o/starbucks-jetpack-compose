package com.starbucks.map

import com.starbucks.map.model.Coordinates
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import kotlinx.coroutines.withTimeoutOrNull

actual suspend fun getCurrentLocation(): Coordinates {
    val geoLocation = Geolocator.mobile()
    val result = withTimeoutOrNull(15000L) { geoLocation.current() } // เพิ่ม timeout

    return when(result) {
        is GeolocatorResult.Success -> {
            val c = result.data.coordinates
            Coordinates(c.latitude, c.longitude)
        }
        is GeolocatorResult.Error -> {
            if(result is GeolocatorResult.PermissionError) throw LocationPermissionException()
            else Coordinates(13.7563, 100.5017)
        }
        null -> Coordinates(13.7563, 100.5017) // timeout fallback
    }
}
