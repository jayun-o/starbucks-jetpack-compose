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

    private val _allProducts = mutableListOf<Product>()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun loadProducts(category: ProductCategory, subCategory: SubCategory?) {
        viewModelScope.launch {
            _products.value = RequestState.Loading

            try {
                repository.readProductsByCategoryFlow(
                    category = category,
                    subCategory = subCategory
                ).collect { result ->
                    when (result) {
                        is RequestState.Success -> {
                            _allProducts.clear()
                            _allProducts.addAll(result.data)
                            applySearch()
                        }
                        else -> _products.value = result
                    }
                }
            } catch (e: Exception) {
                _products.value = RequestState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        applySearch()
    }

    fun clearSearch() {
        _searchQuery.value = ""
        applySearch()
    }

    private fun applySearch() {
        val query = _searchQuery.value.trim()
        val filtered = if (query.isEmpty()) {
            _allProducts
        } else {
            _allProducts.filter { product ->
                product.title.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true)
            }
        }
        _products.value = RequestState.Success(filtered)
    }
}