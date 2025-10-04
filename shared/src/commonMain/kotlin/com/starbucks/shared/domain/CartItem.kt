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
    val shotCountEspresso: Int = 0,
    val shotCountHalfDecaf: Int = 0,
    val shotCountDecaf: Int = 0,
    val milk: List<Pair<String, Double?>> = emptyList(),
    val sweetness: List<Pair<String, Double?>> = emptyList(),
    val topping: List<Pair<String, Double>> = emptyList(),
    val flavor: List<Pair<String, Double>> = emptyList(),
    val condiment: List<Pair<String, Double>> = emptyList(),
    val quantity: Int
)



