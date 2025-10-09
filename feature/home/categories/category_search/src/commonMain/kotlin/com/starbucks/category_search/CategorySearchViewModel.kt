package com.starbucks.category_search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbucks.data.domain.ProductRepository
import com.starbucks.shared.domain.BeverageSubCategory
import com.starbucks.shared.domain.FoodSubCategory
import com.starbucks.shared.domain.Product
import com.starbucks.shared.domain.ProductCategory
import com.starbucks.shared.domain.SubCategory
import com.starbucks.shared.domain.toSubCategory
import com.starbucks.shared.util.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategorySearchViewModel(
    private val repository: ProductRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _products = MutableStateFlow<RequestState<List<Product>>>(RequestState.Idle)
    val products: StateFlow<RequestState<List<Product>>> = _products.asStateFlow()

    private val _allProducts = mutableListOf<Product>()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Get navigation arguments from SavedStateHandle
    private val categoryName: String = savedStateHandle.get<String>("category") ?: ""
    private val subCategoryName: String = savedStateHandle.get<String>("subCategory") ?: ""

    init {
        if (categoryName.isNotEmpty() && subCategoryName.isNotEmpty()) {
            val category = try {
                ProductCategory.valueOf(categoryName)
            } catch (e: Exception) {
                null
            }

            if (category != null) {
                filterProducts(category, subCategoryName)
            }
        }
    }

    fun filterProducts(category: ProductCategory, subCategoryName: String) {
        viewModelScope.launch {
            _products.value = RequestState.Loading

            try {
                val subCategory = getSubCategory(category, subCategoryName)

                if (subCategory != null) {
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
                } else {
                    _products.value = RequestState.Error("Invalid subcategory")
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

    private fun getSubCategory(category: ProductCategory, subCategoryName: String): SubCategory? {
        return when (category) {
            ProductCategory.BEVERAGE -> {
                BeverageSubCategory.entries
                    .find { it.name == subCategoryName }
                    ?.toSubCategory()
            }
            ProductCategory.FOOD -> {
                FoodSubCategory.entries
                    .find { it.name == subCategoryName }
                    ?.toSubCategory()
            }
        }
    }
}