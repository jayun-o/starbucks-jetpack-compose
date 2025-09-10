package com.starbucks.data.domain

import com.starbucks.shared.domain.Product
import dev.gitlive.firebase.storage.File

interface AdminRepository {
    fun getCurrentUserId(): String?
    suspend fun createNewProduct(
        product: Product,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
    suspend fun uploadImageToStorage(file: File): String?
}