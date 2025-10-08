package com.starbucks.all_products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbucks.data.domain.ProductRepository
import com.starbucks.shared.domain.Product
import com.starbucks.shared.domain.ProductCategory
import com.starbucks.shared.domain.SubCategory
import com.starbucks.shared.util.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AllProductsViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<RequestState<List<Product>>>(RequestState.Idle)
    val products: StateFlow<RequestState<List<Product>>> = _products.asStateFlow()

    fun loadProducts(category: ProductCategory, subCategory: SubCategory?) {
        viewModelScope.launch {
            _products.value = RequestState.Loading

            try {
                repository.readProductsByCategoryFlow(
                    category = category,
                    subCategory = subCategory
                ).collect { result ->
                    _products.value = result
                }
            } catch (e: Exception) {
                _products.value = RequestState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
}