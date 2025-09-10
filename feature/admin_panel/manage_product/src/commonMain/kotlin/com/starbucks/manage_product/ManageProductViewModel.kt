package com.starbucks.manage_product

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbucks.data.domain.AdminRepository
import com.starbucks.shared.domain.Product
import com.starbucks.shared.domain.ProductCategory
import com.starbucks.shared.domain.Size
import com.starbucks.shared.domain.SubCategory
import com.starbucks.shared.domain.getSubCategoriesFor
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ManageProductScreenState(
    val id: String = Uuid.random().toHexString(),
    val title: String = "",
    val description: String = "",
    val thumbnail: String = "thumbnail",
    val category: ProductCategory = ProductCategory.BEVERAGE,
    val subCategory: SubCategory?  = null,
    val price: Double = 0.0,
    val sizes: List<Size>? = null,
)

class ManageProductViewModel(
    private val adminRepository: AdminRepository
): ViewModel()  {
    var screenState by mutableStateOf(ManageProductScreenState())
        private set

    val isFormValid: Boolean
        get() {
            val sizes = screenState.sizes
            val sizesValid = if (screenState.category == ProductCategory.BEVERAGE) {

                // ต้องมี size อย่างน้อย 1 ตัว และแต่ละตัวต้องถูกกรอก
                !sizes.isNullOrEmpty() && sizes.all { it.name.isNotBlank() && it.price > 0 }
            } else {
                screenState.price != 0.0
            }

            return screenState.title.isNotBlank() &&
                    screenState.description.isNotBlank() &&
                    screenState.thumbnail.isNotBlank() &&
                    screenState.category != null &&
                    screenState.subCategory != null &&
                    sizesValid
        }



    fun updateTitle(value: String) {
        screenState = screenState.copy(title = value)
    }

    fun updateDescription(value: String) {
        screenState = screenState.copy(description = value)
    }

    fun updateThumbnail(value: String) {
        screenState = screenState.copy(thumbnail = value)
    }

    fun updateCategory(value: ProductCategory) {
        val defaultSubCategory = getSubCategoriesFor(value).firstOrNull()
        screenState = screenState.copy(
            category = value,
            subCategory = defaultSubCategory
        )
    }

    fun updateSubCategory(value: SubCategory) {
        screenState = screenState.copy(subCategory = value)
    }

    fun updatePrice(value: Double) {
        screenState = screenState.copy(price = value)
    }

    fun updateSizes(value: List<Size>) {
        screenState = screenState.copy(sizes = value)
    }

    fun createNewProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            adminRepository.createNewProduct(
                product = Product(
                    id = screenState.id,
                    title = screenState.title,
                    description = screenState.description,
                    thumbnail = screenState.thumbnail,
                    category = screenState.category,
                    subCategory = screenState.subCategory,
                    price = screenState.price,
                    sizes = screenState.sizes
                ),
                onSuccess = {
                    // Reset sizes หลังสร้าง product สำเร็จ
                    if (screenState.category == ProductCategory.BEVERAGE) {
                        updateSizes(emptyList())
                    }
                    onSuccess()
                },
                onError = onError
            )
        }

    }
}