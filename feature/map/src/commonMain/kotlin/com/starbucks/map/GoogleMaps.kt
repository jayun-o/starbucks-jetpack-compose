// commonMain
package com.starbucks.map

import androidx.compose.runtime.Composable

@Composable
expect fun GoogleMaps(userLocation: Coordinates? = null)
