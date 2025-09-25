package com.starbucks.admin_panel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbucks.data.domain.AdminRepository
import com.starbucks.shared.domain.Product
import com.starbucks.shared.domain.displayName
import com.starbucks.shared.util.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AdminPanelViewModel(
    private val adminRepository: AdminRepository,
): ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val products = adminRepository.readLastTenProducts()

    val filteredProducts: StateFlow<RequestState<List<Product>>> =
        searchQuery
            .debounce(300)
            .flatMapLatest { query ->
                products.map { state ->
                    when (state) {
                        is RequestState.Success -> {
                            val filtered = if (query.isBlank()) {
                                state.data
                            } else {
                                state.data.filter { product ->
                                    val categoryText = product.category.displayName()
                                    val subCategoryText = product.subCategory?.title ?: ""

                                    product.title.contains(query, ignoreCase = true) ||
                                            product.description.contains(query, ignoreCase = true) ||
                                            categoryText.contains(query, ignoreCase = true) ||
                                            subCategoryText.contains(query, ignoreCase = true)
                                }
                            }
                            RequestState.Success(filtered)
                        }
                        is RequestState.Loading -> RequestState.Loading
                        is RequestState.Error -> state
                        is RequestState.Idle -> RequestState.Idle
                    }
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = RequestState.Idle
            )



    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
