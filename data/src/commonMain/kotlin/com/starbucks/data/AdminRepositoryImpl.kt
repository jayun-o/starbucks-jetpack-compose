package com.starbucks.data

import com.starbucks.data.domain.AdminRepository
import com.starbucks.shared.domain.Product
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore

class AdminRepositoryImpl: AdminRepository {
    override fun getCurrentUserId() = Firebase.auth.currentUser?.uid

    override suspend fun createNewProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null){
                val firestore = Firebase.firestore

            } else {
                onError("User is not available")
            }
        } catch (e: Exception){
            onError("Error while creating a new product: ${e.message}")
        }
    }
}