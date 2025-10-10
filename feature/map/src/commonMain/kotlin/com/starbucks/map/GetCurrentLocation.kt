package com.starbucks.map

import com.starbucks.shared.domain.Coordinates

expect suspend fun getCurrentLocation(): Coordinates
