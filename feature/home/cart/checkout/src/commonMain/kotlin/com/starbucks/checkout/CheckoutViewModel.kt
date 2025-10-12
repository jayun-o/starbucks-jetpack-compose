package com.starbucks.checkout

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbucks.data.domain.CustomerRepository
import com.starbucks.data.domain.OrderRepository
import com.starbucks.shared.domain.CartItem
import com.starbucks.shared.domain.Customer
import com.starbucks.shared.domain.Order
import com.starbucks.shared.util.RequestState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class CheckoutScreenState(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val address: String? = null,
    val location: String? = null,
    val postalCode: String? = null,
    val phoneNumber: String? = null,
    val cart: List<CartItem> = emptyList()
)

class CheckoutViewModel(
    private val customerRepository: CustomerRepository,
    private val orderRepository: OrderRepository,
    private val savedStateHandle: SavedStateHandle
):ViewModel() {
    var screenReady: RequestState<Unit> by mutableStateOf(RequestState.Loading)
    var screenState: CheckoutScreenState by mutableStateOf(CheckoutScreenState())
        private set


    val isFormValid: Boolean
        get() = with(screenState) {
            firstName.trim().length in 3..50 &&
                    lastName.trim().length in 3..50 &&
                    (postalCode.isNullOrBlank() || postalCode.trim().length in 3..8) &&
                    (phoneNumber.isNullOrBlank() || phoneNumber.trim().length == 10)
        }

    init {
        viewModelScope.launch {
            customerRepository.readCustomerFlow().collectLatest { data ->
                if (data.isSuccess()){
                    val fetchedCustomer = data.getSuccessData()
                    screenState = CheckoutScreenState(
                        id = fetchedCustomer.id,
                        firstName = fetchedCustomer.firstName,
                        lastName = fetchedCustomer.lastName,
                        email = fetchedCustomer.email,
                        address = fetchedCustomer.address,
                        location = fetchedCustomer.location,
                        postalCode = fetchedCustomer.postalCode,
                        phoneNumber = fetchedCustomer.phoneNumber,
                        cart = fetchedCustomer.cart
                    )
                    screenReady = RequestState.Success(Unit)
                } else if (data.isError()){
                    screenReady = RequestState.Error(data.getErrorMessage())
                }
            }
        }
    }

    fun updateFirstName(value: String){
        screenState = screenState.copy(firstName = value)
    }

    fun updateLastName(value: String){
        screenState = screenState.copy(lastName = value)
    }

    fun updateAddress(value: String) {
        screenState = screenState.copy(address = value)
    }

    fun updateLocation(value: String){
        screenState = screenState.copy(location = value)
    }

    fun updatePostalCode(value: String){
        screenState = screenState.copy(postalCode = value)
    }

    fun updatePhoneNumber(value: String){
        screenState = screenState.copy(phoneNumber = value)
    }

    fun payOnDelivery(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        updateCustomer(
            onSuccess = {
                createTheOrder(
                    onSuccess = onSuccess,
                    onError = onError
                )
            },
            onError = onError
        )
    }

    private fun updateCustomer(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            customerRepository.updateCustomer(
                customer = Customer(
                    id = screenState.id,
                    firstName = screenState.firstName,
                    lastName = screenState.lastName,
                    email = screenState.email,
                    address = screenState.address,
                    location = screenState.location,
                    postalCode = screenState.postalCode,
                    phoneNumber = screenState.phoneNumber
                ),
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

    private fun createTheOrder(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            orderRepository.createTheOrder(
                order = Order(
                    customerId = screenState.id,
                    items = screenState.cart,
                    totalAmount = savedStateHandle.get<Double>("totalAmount") ?: 0.0,
                ),
                onSuccess = onSuccess,
                onError = onError
            )
        }
    }

}