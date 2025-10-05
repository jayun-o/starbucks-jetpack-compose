package com.starbucks.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbucks.data.domain.CustomerRepository
import com.starbucks.data.domain.ProductRepository
import com.starbucks.shared.domain.CartItem
import com.starbucks.shared.domain.Size
import com.starbucks.shared.util.RequestState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val productRepository: ProductRepository,
    private val customerRepository: CustomerRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val product = productRepository.readProductByIdFlow(
        savedStateHandle.get<String>("id") ?: ""
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RequestState.Loading
    )

    // Size & Quantity
    var quantity by mutableStateOf(1)
        private set
    var selectedSize by mutableStateOf<Size?>(null)
        private set

    // Espresso shots
    var shotCountEspresso by mutableStateOf(0)
        private set
    var shotCountHalfDecaf by mutableStateOf(0)
        private set
    var shotCountDecaf by mutableStateOf(0)
        private set

    // Single select options
    var selectedMilk by mutableStateOf<String?>(null)
        private set
    var selectedSweetness by mutableStateOf<String?>(null)
        private set

    // Multi select options
    var selectedToppings by mutableStateOf(setOf<String>())
        private set
    var selectedFlavors by mutableStateOf(setOf<String>())
        private set
    var selectedCondiments by mutableStateOf(setOf<String>())
        private set

    // Cutlery & WarmUp state
    var cutlery by mutableStateOf(false)
        private set

    var warmUp by mutableStateOf(false)
        private set

    // Total price
    var totalPrice by mutableStateOf(0.0)
        private set

    init {
        viewModelScope.launch {
            product.collect { state ->
                if (state is RequestState.Success) {
                    recalculateTotal()
                }
            }
        }
    }

    // -------------------------
    // Update functions
    // -------------------------
    fun updateQuantity(value: Int) {
        quantity = value
        recalculateTotal()
    }

    fun updateSize(value: Size) {
        selectedSize = value
        recalculateTotal()
    }

    fun updateEspressoShot(value: Int) {
        shotCountEspresso = value
        recalculateTotal()
    }

    fun updateHalfDecaf(value: Int) {
        shotCountHalfDecaf = value
        recalculateTotal()
    }

    fun updateDecaf(value: Int) {
        shotCountDecaf = value
        recalculateTotal()
    }

    fun updateMilk(value: String?) {
        selectedMilk = value
        recalculateTotal()
    }

    fun updateSweetness(value: String?) {
        selectedSweetness = value
    }

    fun toggleTopping(name: String) {
        selectedToppings = if (selectedToppings.contains(name)) {
            selectedToppings - name
        } else {
            selectedToppings + name
        }
        recalculateTotal()
    }

    fun toggleFlavor(name: String) {
        selectedFlavors = if (selectedFlavors.contains(name)) {
            selectedFlavors - name
        } else {
            selectedFlavors + name
        }
        recalculateTotal()
    }

    fun toggleCondiment(name: String) {
        selectedCondiments = if (selectedCondiments.contains(name)) {
            selectedCondiments - name
        } else {
            selectedCondiments + name
        }
        recalculateTotal()
    }

    fun updateCutlery(value: Boolean) {
        cutlery = value
    }

    fun updateWarmUp(value: Boolean) {
        warmUp = value
    }

    // -------------------------
    // Price Calculation
    // -------------------------
    private fun recalculateTotal() {
        val productPrice = (product.value as? RequestState.Success)?.data?.price ?: 0.0
        val sizePrice = selectedSize?.price ?: 0.0
        val espressoPrice = shotCountEspresso * 20.0
        val halfDecafPrice = shotCountHalfDecaf * 20.0
        val decafPrice = shotCountDecaf * 20.0

        val milkPrice = when (selectedMilk) {
            "Coconut Milk", "Soy", "Almond Milk", "Oat Milk" -> 15.0
            else -> 0.0
        }

        val toppingPrice = selectedToppings.size * 15.0
        val flavorPrice = selectedFlavors.size * 15.0
        val condimentPrice = selectedCondiments.size * 25.0

        totalPrice = if (productPrice > 0.0) {
            productPrice * quantity
        } else {
            (sizePrice +
                    espressoPrice +
                    halfDecafPrice +
                    decafPrice +
                    milkPrice +
                    toppingPrice +
                    flavorPrice +
                    condimentPrice) * quantity
        }
    }

    // -------------------------
    // Cart Management
    // -------------------------
    fun addItemToCart(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val productId = savedStateHandle.get<String>("id")
            if (productId != null) {
                customerRepository.addItemToCart(
                    cartItem = CartItem(
                        productId = productId,
                        quantity = quantity,
                        size = selectedSize?.name,
                        shotCountEspresso = shotCountEspresso,
                        shotCountHalfDecaf = shotCountHalfDecaf,
                        shotCountDecaf = shotCountDecaf,
                        milk = selectedMilk,
                        sweetness = selectedSweetness,
                        toppings = selectedToppings.toList(),
                        flavors = selectedFlavors.toList(),
                        condiments = selectedCondiments.toList(),
                        totalPrice = totalPrice,
                        cutlery = cutlery,
                        warmUp = warmUp
                    ),
                    onSuccess = onSuccess,
                    onError = onError
                )
            }
        }
    }


    fun clearSelections() {
        selectedSize = null
        shotCountEspresso = 0
        shotCountHalfDecaf = 0
        shotCountDecaf = 0
        selectedMilk = null
        selectedSweetness = null
        selectedToppings = emptySet()
        selectedFlavors = emptySet()
        selectedCondiments = emptySet()
        quantity = 1
        totalPrice = 0.0
        cutlery = false
        warmUp = false
    }
}
