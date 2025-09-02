package com.starbucks.data

import com.starbucks.data.domain.CustomerRepository
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
                    onSuccess()
                }
            } else {
                onError("User is not available")
            }
        } catch (e: Exception) {
            onError("Error while creating a new Customer: ${e.message}")
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
                            val customer = Customer(
                                id = document.id,
                                firstName = document.get(field = "firstName"),
                                lastName = document.get(field = "lastName"),
                                email = document.get(field = "email"),
                                address = document.get(field = "address"),
                                province = document.get(field = "province"),
                                district = document.get(field = "district"),
                                subDistrict = document.get(field = "subDistrict"),
                                postalCode = document.get(field = "postalCode"),
                                phoneNumber = document.get(field = "phoneNumber"),
                                cart = document.get(field = "cart"),
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
                            "province" to customer.province,
                            "district" to customer.district,
                            "subDistrict" to customer.subDistrict,
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

    override suspend fun signOut(): RequestState<Unit> {
        return try {
            Firebase.auth.signOut()
            RequestState.Success(data = Unit)
        } catch (e: Exception) {
            RequestState.Error("Error while signing out: ${e.message}")
        }
    }
}