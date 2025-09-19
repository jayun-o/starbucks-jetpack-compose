package com.starbucks.manage_product

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.starbucks.data.domain.AdminRepository
import com.starbucks.shared.domain.Product
import com.starbucks.shared.domain.ProductCategory
import com.starbucks.shared.domain.Size
import com.starbucks.shared.domain.SubCategory
import com.starbucks.shared.domain.getSubCategoriesFor
import com.starbucks.shared.util.RequestState
import dev.gitlive.firebase.storage.File
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class ManageProductScreenState(
    val id: String = Uuid.random().toHexString(),
    val title: String = "",
    val description: String = "",
    val thumbnail: String = "",
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

    var thumbnailUploaderState: RequestState<Unit> by mutableStateOf(RequestState.Idle)
        private set

    // Reactive form validation
    val isFormValid by derivedStateOf {
        val sizes = screenState.sizes
        val sizesValid = if (screenState.category == ProductCategory.BEVERAGE) {
            !sizes.isNullOrEmpty() && sizes.all { it.name.isNotBlank() && it.price > 0 }
        } else {
            screenState.price > 0 // รองรับราคา 0
        }

        screenState.title.isNotBlank() &&
                screenState.description.isNotBlank() &&
                screenState.thumbnail.isNotBlank() &&
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

    fun updateThumbnailUploaderState(value: RequestState<Unit>) {
        thumbnailUploaderState = value
    }

    fun updateCategory(value: ProductCategory) {
        val defaultSubCategory = getSubCategoriesFor(value).firstOrNull()
        screenState = screenState.copy(
            category = value,
            subCategory = defaultSubCategory,
            sizes = null,   // reset size
            price = 0.0     // reset price
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

    @OptIn(ExperimentalUuidApi::class)
    private fun resetState() {
        screenState = screenState.copy(
            id = Uuid.random().toHexString(),
            title = "",
            description = "",
            thumbnail = "",
            subCategory = null,
            price = 0.0,
            sizes = null
        )
        thumbnailUploaderState = RequestState.Idle // reset thumbnail
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
                    resetState()
                    onSuccess()
                },
                onError = onError
            )
        }
    }

    fun uploadThumbnailToStorage(
        file: File?,
        onSuccess: () -> Unit
        ){
        if (file == null){
            updateThumbnailUploaderState(RequestState.Error("File is null. Error while selecting an image."))
            return
        }

        updateThumbnailUploaderState(RequestState.Loading)

        viewModelScope.launch {
            try {
                val downloadUrl = adminRepository.uploadImageToStorage(file)
                if (downloadUrl.isNullOrEmpty()){
                    throw Exception("Failed to retrieve a download URL after the upload.")
                }
                onSuccess()
                updateThumbnailUploaderState(RequestState.Success(Unit))
                updateThumbnail(downloadUrl)

            } catch (e: Exception){
                updateThumbnailUploaderState(RequestState.Error("Error while uploading: $e"))
            }
        }
    }

    fun deleteThumbnailFromStorage(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        viewModelScope.launch {
            adminRepository.deleteImageFromStorage(
                downloadUrl = screenState.thumbnail,
                onSuccess = {
                    updateThumbnail("")
                    updateThumbnailUploaderState(RequestState.Idle)
                    onSuccess()
                },
                onError = onError
            )
        }
    }
}