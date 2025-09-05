package com.starbucks.map

@kotlinx.serialization.Serializable
data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

data class Place(
    val name: String?,
    val thoroughfare: String?,
    val subThoroughfare: String?,
    val locality: String?,
    val subLocality: String?,
    val postalCode: String?,
    val countryCode: String?,
    val countryName: String?
)
