package com.starbucks.order

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.starbucks.data.domain.OrderRepository
import com.starbucks.shared.domain.Order
import kotlinx.coroutines.*

class OrderViewModel(
    private val orderRepository: OrderRepository
): ViewModel() {
    var orders by mutableStateOf<List<Order>>(emptyList())
        private set

    var loading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun loadOrderHistory() {
        val userId = orderRepository.getCurrentUserId() ?: return
        loading = true

        CoroutineScope(Dispatchers.Main).launch {
            orderRepository.getOrdersByCustomerId(
                customerId = userId,
                onResult = {
                    orders = it
                    loading = false
                },
                onError = { msg ->
                    error = msg
                    loading = false
                }
            )
        }
    }
}

