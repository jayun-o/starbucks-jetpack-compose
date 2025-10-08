package com.starbucks.data

import com.starbucks.data.domain.ProductRepository
import com.starbucks.shared.domain.Product
import com.starbucks.shared.domain.ProductCategory
import com.starbucks.shared.domain.SubCategory
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
                                isPopular = document.get(field = "isPopular"),

                                isCoffeeShot = document.get(field = "isCoffeeShot"),
                                isMilk = document.get(field = "isMilk"),
                                isSweetness = document.get(field = "isSweetness"),
                                isFlavorAndSyrup = document.get(field = "isFlavorAndSyrup"),
                                isCondiment = document.get(field = "isCondiment"),
                                isToppings = document.get(field = "isToppings"),
                                isCutlery = document.get(field = "isCutlery"),
                                isWarmUp = document.get(field = "isWarmUp")
                            )
                        }
                        send(RequestState.Success(data = products.map { it.copy(title = it.title.split(" ")
                            .joinToString(" ") { word ->
                                word.replaceFirstChar { it.uppercase() }
                            }) }))
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
                                isPopular = document.get(field = "isPopular"),

                                isCoffeeShot = document.get(field = "isCoffeeShot"),
                                isMilk = document.get(field = "isMilk"),
                                isSweetness = document.get(field = "isSweetness"),
                                isFlavorAndSyrup = document.get(field = "isFlavorAndSyrup"),
                                isCondiment = document.get(field = "isCondiment"),
                                isToppings = document.get(field = "isToppings"),
                                isCutlery = document.get(field = "isCutlery"),
                                isWarmUp = document.get(field = "isWarmUp")
                            )
                        }
                        send(RequestState.Success(data = products.map { it.copy(title = it.title
                            .split(" ")
                            .joinToString(" ") { word ->
                                word.replaceFirstChar { it.uppercase() }
                            }) }))
                    }
            } else {
                send(RequestState.Error("User is not available."))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error while reading the last 10 times from the database: ${e.message}"))
        }
    }

    override fun readProductByIdFlow(id: String): Flow<RequestState<Product>> = channelFlow{
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                database.collection(collectionPath = "product")
                    .document(id)
                    .snapshots
                    .collectLatest { document ->
                        if(document.exists){
                            val product = Product(
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
                                isPopular = document.get(field = "isPopular"),

                                isCoffeeShot = document.get(field = "isCoffeeShot"),
                                isMilk = document.get(field = "isMilk"),
                                isSweetness = document.get(field = "isSweetness"),
                                isFlavorAndSyrup = document.get(field = "isFlavorAndSyrup"),
                                isCondiment = document.get(field = "isCondiment"),
                                isToppings = document.get(field = "isToppings"),
                                isCutlery = document.get(field = "isCutlery"),
                                isWarmUp = document.get(field = "isWarmUp")
                            )
                            send(RequestState.Success(product.copy(
                                title = product.title
                                    .split(" ")
                                    .joinToString(" ") { word ->
                                        word.replaceFirstChar { it.uppercase() }
                                    })
                            ))
                        } else {
                            send(RequestState.Error("Selected product does not exist."))
                        }
                    }
            } else {
                send(RequestState.Error("User is not available."))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error while reading the last 10 times from the database: ${e.message}"))
        }
    }

    override fun readProductByIdsFlow(ids: List<String>): Flow<RequestState<List<Product>>> =
        channelFlow {
            try {
                val userId = getCurrentUserId()
                if (userId != null) {
                    val database = Firebase.firestore
                    val productCollection = database.collection(collectionPath = "product")
                    val allProducts = mutableListOf<Product>()
                    val chunks = ids.chunked(10)

                    chunks.forEachIndexed { index, chunk ->
                        productCollection
                            .where { "id" inArray chunk }
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
                                        isPopular = document.get(field = "isPopular"),

                                        isCoffeeShot = document.get(field = "isCoffeeShot"),
                                        isMilk = document.get(field = "isMilk"),
                                        isSweetness = document.get(field = "isSweetness"),
                                        isFlavorAndSyrup = document.get(field = "isFlavorAndSyrup"),
                                        isCondiment = document.get(field = "isCondiment"),
                                        isToppings = document.get(field = "isToppings"),
                                        isCutlery = document.get(field = "isCutlery"),
                                        isWarmUp = document.get(field = "isWarmUp")
                                    )
                                }
                                allProducts.addAll(products.map { it.copy(title = it.title.split(" ")
                                    .joinToString(" ") { word ->
                                        word.replaceFirstChar { it.uppercase() }
                                    })
                                })
                                if (index == chunks.lastIndex) {
                                    send(RequestState.Success(allProducts))
                                }
                            }
                    }
                } else {
                    send(RequestState.Error("User is not available."))
                }
            } catch (e: Exception) {
                send(RequestState.Error("Error while reading the last 10 times from the database: ${e.message}"))
            }
        }

    override fun readProductsByCategoryFlow(
        category: ProductCategory,
        subCategory: SubCategory?
    ): Flow<RequestState<List<Product>>>
            = channelFlow {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val database = Firebase.firestore
                var query = database.collection(collectionPath = "product")
                    .where { "category" equalTo category.id }

                // Only add subCategory filter if it's provided
                if (subCategory != null) {
                    query = query.where { "subCategory.id" equalTo subCategory.id }
                }

                query.snapshots.collectLatest { querySnapshot ->
                    val products = querySnapshot.documents.map { document ->
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
                            isPopular = document.get(field = "isPopular"),

                            isCoffeeShot = document.get(field = "isCoffeeShot"),
                            isMilk = document.get(field = "isMilk"),
                            isSweetness = document.get(field = "isSweetness"),
                            isFlavorAndSyrup = document.get(field = "isFlavorAndSyrup"),
                            isCondiment = document.get(field = "isCondiment"),
                            isToppings = document.get(field = "isToppings"),
                            isCutlery = document.get(field = "isCutlery"),
                            isWarmUp = document.get(field = "isWarmUp")
                        )
                    }
                    send(RequestState.Success(products.map {
                        it.copy(
                            title = it.title
                                .split(" ")
                                .joinToString(" ") { word ->
                                    word.replaceFirstChar { it.uppercase() }
                                })
                    }))
                }
            } else {
                send(RequestState.Error("User is not available."))
            }
        } catch (e: Exception) {
            send(RequestState.Error("Error while reading products from the database: ${e.message}"))
        }
    }
}