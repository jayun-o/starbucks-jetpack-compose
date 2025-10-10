package com.starbucks.map

import androidx.compose.runtime.Composable
import com.starbucks.shared.domain.Coordinates

@Composable
actual fun GoogleMaps(userLocation: Coordinates?, onLocationPicked: (Coordinates, String) -> Unit) {
}