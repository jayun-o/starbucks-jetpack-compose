package com.starbucks.data.domain

import com.starbucks.shared.domain.CartItem
import com.starbucks.shared.domain.Customer
import com.starbucks.shared.util.RequestState
import dev.gitlive.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow


interface CustomerRepository {
    fun getCurrentUserId(): String?

    suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    // New email/password authentication methods
    suspend fun signUpWithEmail(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun signInWithEmail(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    fun readCustomerFlow(): Flow<RequestState<Customer>>

    suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun addItemToCart(
        cartItem: CartItem,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun updateCartItemQuantity(
        id: String,
        quantity: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun deleteCartItem(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun deleteAllCartItems(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    suspend fun signOut(): RequestState<Unit>
}