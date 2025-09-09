package com.starbucks.map

import com.starbucks.map.model.Coordinates

expect suspend fun getCurrentLocation(): Coordinates
