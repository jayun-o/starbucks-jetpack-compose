package com.starbucks.shared.domain

import kotlinx.serialization.Serializable

@Serializable
data class Coordinates(
    val latitude: Double,
    val longitude: Double
)
