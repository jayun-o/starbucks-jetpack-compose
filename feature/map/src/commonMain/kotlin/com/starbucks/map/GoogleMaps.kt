// commonMain
package com.starbucks.map

import androidx.compose.runtime.Composable
import com.starbucks.shared.domain.Coordinates

@Composable
expect fun GoogleMaps(
    userLocation: Coordinates? = null,
    onLocationPicked: (Coordinates, String) -> Unit
)
