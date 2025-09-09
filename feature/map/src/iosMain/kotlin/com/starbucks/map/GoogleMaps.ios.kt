package com.starbucks.map

import androidx.compose.runtime.Composable
import com.starbucks.map.model.Coordinates

@Composable
actual fun GoogleMaps(userLocation: Coordinates?, onLocationPicked: (Coordinates, String) -> Unit) {
}