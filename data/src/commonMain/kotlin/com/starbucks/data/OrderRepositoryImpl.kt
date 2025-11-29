package com.starbucks.data

import com.starbucks.data.domain.CustomerRepository
import com.starbucks.data.domain.OrderRepository
import com.starbucks.shared.domain.Order
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.flow.first
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class OrderRepositoryImpl(
    private val customerRepository: CustomerRepository
): OrderRepository {
    override fun getCurrentUserId() = Firebase.auth.currentUser?.uid

    @OptIn(ExperimentalTime::class)
    override suspend fun createTheOrder(
        order: Order,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null) {
                val database = Firebase.firestore
                val orderCollection = database.collection(collectionPath = "order")

                // Create order with timestamp
                val orderWithTimestamp = order.copy(
                    createdAt = Clock.System.now().toEpochMilliseconds()
                )

                orderCollection.document(order.id).set(orderWithTimestamp)

                customerRepository.deleteAllCartItems(
                    onSuccess = { },
                    onError = { }
                )
                onSuccess()
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while creating order: ${e.message}")
        }
    }

    override suspend fun getOrdersByCustomerId(
        customerId: String,
        onResult: (List<Order>) -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val database = Firebase.firestore
            val collection = database.collection("order")

            // Query orders by customerId
            val query = collection.where {
                "customerId" equalTo customerId
            }

            // Get the documents
            val snapshot = query.get()

            // Map documents to Order objects
            val orders = snapshot.documents.mapNotNull { document ->
                try {
                    document.data<Order>()
                } catch (e: Exception) {
                    println("Error parsing order document ${document.id}: ${e.message}")
                    null
                }
            }.sortedByDescending { it.createdAt } // Sort by newest first

            onResult(orders)
        } catch (e: Exception) {
            onError("Error getting order history: ${e.message}")
        }
    }
}