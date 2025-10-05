package com.starbucks.shared.domain

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class CartItem(
    val id: String = Uuid.random().toHexString(),
    val productId: String,
    val size: String? = null,
    val productCartItemDetail: List<String>? = emptyList(),
    val shotCountEspresso: Int? = 0,
    val shotCountHalfDecaf: Int? = 0,
    val shotCountDecaf: Int? = 0,
    val milk: String? = null,
    val sweetness: String? = null,
    val toppings: List<String>? = emptyList(),
    val flavors: List<String>? = emptyList(),
    val condiments: List<String>? = emptyList(),
    val quantity: Int,
    val totalPrice: Double,
    val cutlery: Boolean? = false,
    val warmUp: Boolean? = false
)



