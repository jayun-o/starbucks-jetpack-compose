package com.starbucks.data

import com.starbucks.data.domain.ProductRepository
import com.starbucks.shared.domain.Product
import com.starbucks.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class ProductRepositoryImpl: ProductRepository {
    override fun getCurrentUserId() = Firebase.auth.currentUser?.uid

    override fun readDiscountedProducts(): Flow<RequestState<List<Product>>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                database.collection(collectionPath = "product")
                    .where { "isDiscounted" equalTo true }
                    .snapshots
                    .collectLatest { query ->
                        val products = query.documents.map { document ->
                            Product(
                                id = document.id,
                                createdAt = document.get(field = "createdAt"),
                                title = document.get(field = "title"),
                                description = document.get(field = "description"),
                                thumbnail = document.get(field = "thumbnail"),
                                category = document.get(field = "category"),
                                subCategory = document.get(field = "subCategory"),
                                price = document.get(field = "price"),
                                sizes = document.get(field = "sizes"),
                                isAvailable = document.get(field = "isAvailable"),
                                isNew = document.get(field = "isNew"),
                                isDiscounted = document.get(field = "isDiscounted"),
                                discounted = document.get(field = "discounted"),
                                isPopular = document.get(field = "isPopular")
                            )
                        }
                        send(RequestState.Success(data = products.map { it.copy(title = it.title.uppercase()) }))
                    }
            } else {
                send(RequestState.Error("User is not available."))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error while reading the last 10 times from the database: ${e.message}"))
        }
    }

    override fun readNewProducts(): Flow<RequestState<List<Product>>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                database.collection(collectionPath = "product")
                    .where { "isNew" equalTo true }
                    .snapshots
                    .collectLatest { query ->
                        val products = query.documents.map { document ->
                            Product(
                                id = document.id,
                                createdAt = document.get(field = "createdAt"),
                                title = document.get(field = "title"),
                                description = document.get(field = "description"),
                                thumbnail = document.get(field = "thumbnail"),
                                category = document.get(field = "category"),
                                subCategory = document.get(field = "subCategory"),
                                price = document.get(field = "price"),
                                sizes = document.get(field = "sizes"),
                                isAvailable = document.get(field = "isAvailable"),
                                isNew = document.get(field = "isNew"),
                                isDiscounted = document.get(field = "isDiscounted"),
                                discounted = document.get(field = "discounted"),
                                isPopular = document.get(field = "isPopular")
                            )
                        }
                        send(RequestState.Success(data = products.map { it.copy(title = it.title.uppercase()) }))
                    }
            } else {
                send(RequestState.Error("User is not available."))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error while reading the last 10 times from the database: ${e.message}"))
        }
    }
}