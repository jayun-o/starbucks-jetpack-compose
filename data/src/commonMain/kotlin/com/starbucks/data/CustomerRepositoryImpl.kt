package com.starbucks.data

import com.starbucks.data.domain.CustomerRepository
import com.starbucks.shared.domain.CartItem
import com.starbucks.shared.domain.Customer
import com.starbucks.shared.util.RequestState
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseUser
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest

class CustomerRepositoryImpl: CustomerRepository{
    override fun getCurrentUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override suspend fun createCustomer(
        user: FirebaseUser?,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            if ( user != null ){
                val customerCollection = Firebase.firestore.collection(collectionPath = "customer")
                val customer = Customer(
                    id = user.uid,
                    firstName = user.displayName?.split(" ")?.firstOrNull() ?: "Unknown",
                    lastName = user.displayName?.split(" ")?.lastOrNull() ?: "Unknown",
                    email = user.email ?: "Unknown",
                )

                val customerExists = customerCollection.document(user.uid).get().exists

                if (customerExists){
                    onSuccess()
                } else {
                    customerCollection.document(user.uid).set(customer)
                    customerCollection.document(user.uid)
                        .collection("privateData")
                        .document("role")
                        .set(mapOf("isAdmin" to false))
                    onSuccess()
                }
            } else {
                onError("User is not available")
            }
        } catch (e: Exception) {
            onError("Error while creating a new Customer: ${e.message}")
        }
    }

    override suspend fun signUpWithEmail(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Validate inputs
            if (email.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank()) {
                onError("All fields are required")
                return
            }

            if (password.length < 6) {
                onError("Password must be at least 6 characters")
                return
            }

            // Create user with Firebase Auth
            val authResult = Firebase.auth.createUserWithEmailAndPassword(email, password)
            val user = authResult.user

            if (user != null) {
                // Create customer document in Firestore
                val customerCollection = Firebase.firestore.collection(collectionPath = "customer")
                val customer = Customer(
                    id = user.uid,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                )

                customerCollection.document(user.uid).set(customer)
                customerCollection.document(user.uid)
                    .collection("privateData")
                    .document("role")
                    .set(mapOf("isAdmin" to false))

                onSuccess()
            } else {
                onError("Failed to create user")
            }
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("email address is already in use") == true ->
                    "Email address is already registered"
                e.message?.contains("email address is badly formatted") == true ->
                    "Invalid email address"
                e.message?.contains("network error") == true ->
                    "Network error. Please check your connection"
                else -> "Sign up failed: ${e.message}"
            }
            onError(errorMessage)
        }
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Validate inputs
            if (email.isBlank() || password.isBlank()) {
                onError("Email and password are required")
                return
            }

            // Sign in with Firebase Auth
            Firebase.auth.signInWithEmailAndPassword(email, password)
            onSuccess()
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("no user record") == true ||
                        e.message?.contains("invalid-credential") == true ||
                        e.message?.contains("wrong-password") == true ->
                    "Invalid email or password"
                e.message?.contains("user-disabled") == true ->
                    "This account has been disabled"
                e.message?.contains("network error") == true ->
                    "Network error. Please check your connection"
                else -> "Sign in failed: ${e.message}"
            }
            onError(errorMessage)
        }
    }

    override suspend fun sendPasswordResetEmail(
        email: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            // Validate email
            if (email.isBlank()) {
                onError("Email is required")
                return
            }

            // Send password reset email
            Firebase.auth.sendPasswordResetEmail(email)
            onSuccess()
        } catch (e: Exception) {
            val errorMessage = when {
                e.message?.contains("no user record") == true ||
                        e.message?.contains("user-not-found") == true ->
                    "No account found with this email"
                e.message?.contains("invalid-email") == true ->
                    "Invalid email address"
                e.message?.contains("network error") == true ->
                    "Network error. Please check your connection"
                else -> "Failed to send reset email: ${e.message}"
            }
            onError(errorMessage)
        }
    }

    override fun readCustomerFlow(): Flow<RequestState<Customer>> = channelFlow{
        try {
            val userId = getCurrentUserId()
            if(userId != null){
                val database = Firebase.firestore
                database.collection(collectionPath = "customer")
                    .document(userId)
                    .snapshots
                    .collectLatest { document ->
                        if (document.exists){
                            val privateDataDocument = database.collection(collectionPath = "customer")
                                .document(userId)
                                .collection("privateData")
                                .document("role")
                                .get()

                            val customer = Customer(
                                id = document.id,
                                firstName = document.get(field = "firstName"),
                                lastName = document.get(field = "lastName"),
                                email = document.get(field = "email"),
                                address = document.get(field = "address"),
                                location = document.get(field = "location"),
                                postalCode = document.get(field = "postalCode"),
                                phoneNumber = document.get(field = "phoneNumber"),
                                cart = document.get(field = "cart"),
                                isAdmin = privateDataDocument.get(field = "isAdmin")
                            )
                            send(RequestState.Success(data = customer))
                        } else {
                            send(RequestState.Error("Queried customer document does not exist"))
                        }
                    }
            } else {
                send(RequestState.Error("User is not available"))
            }
        } catch (e: Exception){
            send(RequestState.Error("Error while reading customer information: ${e.message}"))
        }

    }

    override suspend fun updateCustomer(
        customer: Customer,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val userId = getCurrentUserId()
            if (userId != null) {
                val firestore = Firebase.firestore
                val customerCollection = firestore.collection(collectionPath = "customer")

                val existingCustomer = customerCollection
                    .document(customer.id)
                    .get()
                if (existingCustomer.exists) {
                    customerCollection
                        .document(customer.id)
                        .update(
                            "firstName" to customer.firstName,
                            "lastName" to customer.lastName,
                            "email" to customer.email,
                            "address" to customer.address,
                            "location" to customer.location,
                            "postalCode" to customer.postalCode,
                            "phoneNumber" to customer.phoneNumber,
                        )
                    onSuccess()
                } else {
                    onError("Customer not found")
                }
            } else {
                onError("User is not available")
            }
        } catch (e : Exception){
            onError("Error while updating customer information: ${e.message}")
        }
    }

    override suspend fun addItemToCart(
        cartItem: CartItem,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null){
                val database = Firebase.firestore
                val customerCollection = database.collection(collectionPath = "customer")

                val existingCustomer = customerCollection
                    .document(currentUserId)
                    .get()
                if (existingCustomer.exists){
                    val existingCart = existingCustomer.get<List<CartItem>>("cart")
                    val updatedCart = existingCart + cartItem
                    customerCollection
                        .document(currentUserId)
                        .set(
                            data = mapOf("cart" to updatedCart),
                            merge = true
                        )
                    onSuccess()
                } else {
                    onError("Select customer does not exist.")
                }
            } else {
                onError("User is not available")
            }

        } catch (e: Exception){
            onError("Error while adding a product yo cart: ${e.message}")
        }
    }

    override suspend fun updateCartItemQuantity(
        id: String,
        quantity: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null){
                val database = Firebase.firestore
                val customerCollection = database.collection(collectionPath = "customer")

                val existingCustomer = customerCollection
                    .document(currentUserId)
                    .get()
                if (existingCustomer.exists){
                    val existingCart = existingCustomer.get<List<CartItem>>("cart")
                    val updatedCart = existingCart.map { cartItem ->
                        if (cartItem.id == id){
                            val unitPrice = if (cartItem.quantity > 0) {
                                cartItem.totalPrice / cartItem.quantity
                            } else {
                                cartItem.totalPrice
                            }
                            cartItem.copy(
                                quantity = quantity,
                                totalPrice = unitPrice * quantity
                            )
                        } else cartItem
                    }
                    customerCollection
                        .document(currentUserId)
                        .update(data = mapOf("cart" to updatedCart))
                    onSuccess()
                } else {
                    onError("Select customer does not exist.")
                }
            } else {
                onError("User is not available")
            }

        } catch (e: Exception){
            onError("Error while updating cart item quantity: ${e.message}")
        }
    }

    override suspend fun deleteCartItem(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null) {
                val database = Firebase.firestore
                val customerCollection = database.collection(collectionPath = "customer")

                val existingCustomer = customerCollection
                    .document(currentUserId)
                    .get()
                if (existingCustomer.exists) {
                    val existingCart = existingCustomer.get<List<CartItem>>("cart")
                    val updatedCart = existingCart.filterNot { it.id == id }
                    customerCollection.document(currentUserId)
                        .update(data = mapOf("cart" to updatedCart))
                    onSuccess()
                } else {
                    onError("Select customer does not exist.")
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while deleting a product from cart: ${e.message}")
        }
    }

    override suspend fun deleteAllCartItems(onSuccess: () -> Unit, onError: (String) -> Unit) {
        try {
            val currentUserId = getCurrentUserId()
            if (currentUserId != null) {
                val database = Firebase.firestore
                val customerCollection = database.collection(collectionPath = "customer")

                val existingCustomer = customerCollection
                    .document(currentUserId)
                    .get()
                if (existingCustomer.exists) {
                    customerCollection.document(currentUserId)
                        .update(data = mapOf("cart" to emptyList<List<CartItem>>()))
                    onSuccess()
                } else {
                    onError("Select customer does not exist.")
                }
            } else {
                onError("User is not available.")
            }
        } catch (e: Exception) {
            onError("Error while deleting a product from cart: ${e.message}")
        }
    }

    override suspend fun signOut(): RequestState<Unit> {
        return try {
            Firebase.auth.signOut()
            RequestState.Success(data = Unit)
        } catch (e: Exception) {
            RequestState.Error("Error while signing out: ${e.message}")
        }
    }
}