package com.starbucks.shared.domain

import kotlinx.serialization.Serializable


@Serializable
data class Customer(
    val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val address: String? = null,
    val location: String? = null,
    val postalCode: String? = null,
    val phoneNumber: String? = null,
    val cart: List<CartItem> = emptyList()
)