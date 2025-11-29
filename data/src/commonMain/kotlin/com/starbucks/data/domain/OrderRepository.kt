package com.starbucks.data.domain

import com.starbucks.shared.domain.Order

interface OrderRepository {
    fun getCurrentUserId(): String?
    suspend fun createTheOrder(
        order: Order,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun getOrdersByCustomerId(
        customerId: String,
        onResult: (List<Order>) -> Unit,
        onError: (String) -> Unit
    )
    
}