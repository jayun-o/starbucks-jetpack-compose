package com.starbucks.data

import com.starbucks.data.domain.AdminRepository
import com.starbucks.shared.domain.Product
import com.starbucks.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.firestore
import dev.gitlive.firebase.storage.File
import dev.gitlive.firebase.storage.storage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withTimeout
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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
                val productCollection = firestore.collection("product")
                productCollection.document(product.id).set(product)
                onSuccess()

            } else {
                onError("User is not available")
            }
        } catch (e: Exception){
            onError("Error while creating a new product: ${e.message}")
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun uploadImageToStorage(file: File): String? {
        return if(getCurrentUserId() != null){
            val storage = Firebase.storage.reference
            val imagePath = storage.child(path = "images/${Uuid.random().toHexString()}")
            try {
                withTimeout(timeMillis = 20000L){
                    imagePath.putFile(file)
                    imagePath.getDownloadUrl()
                }
            } catch (e: Exception){
                null
            }
        } else null
    }

    override suspend fun deleteImageFromStorage(
        downloadUrl: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val storagePath = extractFirebaseStoragePath(downloadUrl)
            if (storagePath != null){
                Firebase.storage.reference.child(storagePath).delete()
                onSuccess()
            } else {
                onError("Storage path is null")
            }
        } catch (e: Exception){
            onError("Error while deleting a thumbnail: ${e.message}")
        }
    }

    override fun readLastTenProducts(): Flow<RequestState<List<Product>>> = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null){
                val database = Firebase.firestore
                database.collection(collectionPath = "product")
                    .orderBy("createdAt", Direction.DESCENDING)
                    .limit(10)
                    .snapshots
                    .collectLatest { query ->
                        val products = query.documents.map { document ->
                            Product(
                                id = document.id,
                                title = document.get(field = "title"),
                                createdAt = document.get(field = "createdAt"),
                                description = document.get(field = "description"),
                                thumbnail = document.get(field = "thumbnail"),
                                category = document.get(field = "category"),
                                subCategory = document.get(field = "subCategory"),
                                price = document.get(field = "price"),
                                sizes = document.get(field = "sizes"),
                                isAvailable = document.get(field = "isAvailable"),
                                isNew = document.get(field = "isNew"),
                                isDiscounted = document.get(field = "isDiscounted"),
                                isPopular = document.get(field = "isPopular")
                            )
                        }
                        send(RequestState.Success(data = products))
                    }
            } else {
                send(RequestState.Error("User is not available."))
            }
        } catch (e:Exception){
            send(RequestState.Error("Error while reading the last 10 times from the database: ${e.message}"))
        }
    }

    override suspend fun readProductById(id: String): RequestState<Product> {
        return try {
            val userId = getCurrentUserId()
            if(userId != null){
                val database = Firebase.firestore
                val productDocument = database.collection(collectionPath = "product")
                    .document(id)
                    .get()
                if(productDocument.exists){
                    val product = Product(
                        id = productDocument.id,
                        title = productDocument.get(field = "title"),
                        createdAt = productDocument.get(field = "createdAt"),
                        description = productDocument.get(field = "description"),
                        thumbnail = productDocument.get(field = "thumbnail"),
                        category = productDocument.get(field = "category"),
                        subCategory = productDocument.get(field = "subCategory"),
                        price = productDocument.get(field = "price"),
                        sizes = productDocument.get(field = "sizes"),
                        isAvailable = productDocument.get(field = "isAvailable"),
                        isNew = productDocument.get(field = "isNew"),
                        isDiscounted = productDocument.get(field = "isDiscounted"),
                        isPopular = productDocument.get(field = "isPopular")
                    )
                    RequestState.Success(product)
                } else {
                    RequestState.Error("Selected product not found.")
                }
            } else {
                RequestState.Error("User is not available.")
            }
        } catch (e: Exception){
            RequestState.Error("Error while reading a selected product: ${e.message}")
        }
    }

    private fun extractFirebaseStoragePath(downloadUrl: String): String? {
        val startIndex = downloadUrl.indexOf("/o/" ) + 3
        if (startIndex < 3) return null

        val endIndex = downloadUrl.indexOf("?", startIndex)
        val encodedPath = if (endIndex != -1 ){
            downloadUrl.substring(startIndex, endIndex)
        } else {
            downloadUrl.substring(startIndex)
        }
        return decodeFirebasePath(encodedPath)
    }

    private fun decodeFirebasePath(encodedPath: String): String {
        return encodedPath
            .replace("%2F", "/")
            .replace("%20", " ")
    }
}