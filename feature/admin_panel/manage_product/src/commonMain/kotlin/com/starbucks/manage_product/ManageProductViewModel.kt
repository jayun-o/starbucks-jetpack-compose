@file:OptIn(ExperimentalTime::class)

package com.starbucks.manage_product

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
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
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalTime::class)
data class ManageProductScreenState(
    val id: String = Uuid.random().toHexString(),
    val createdAt: Long = Clock.System.now().toEpochMilliseconds(),
    val title: String = "",
    val description: String = "",
    val thumbnail: String = "",
    val category: ProductCategory = ProductCategory.BEVERAGE,
    val subCategory: SubCategory = getSubCategoriesFor(ProductCategory.BEVERAGE).first(),
    val price: Double = 0.0,
    val sizes: List<Size>? = null,
    val isNew: Boolean = false,
    val isPopular: Boolean = false,
    val isDiscounted: Boolean = false,
    val discounted: Int? = 0,
    val isAvailable: Boolean = false,

    val isCoffeeShot: Boolean = false,
    val isMilk: Boolean = false,
    val isSweetness: Boolean = false,
    val isFlavorAndSyrup: Boolean = false,
    val isCondiment: Boolean = false,
    val isToppings: Boolean = false,
    val isCutlery: Boolean = false,
    val isWarmUp: Boolean = false


)

class ManageProductViewModel(
    private val adminRepository: AdminRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel()  {

    private val productId = savedStateHandle.get<String>("id") ?: ""

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
            screenState.price > 0
        }

        screenState.title.isNotBlank() &&
                screenState.description.isNotBlank() &&
                screenState.thumbnail.isNotBlank() &&
                sizesValid
    }

    init {
        productId.takeIf { it.isNotEmpty() }?.let { id ->
            viewModelScope.launch {
                val selectedProduct = adminRepository.readProductById(id)

                if (selectedProduct.isSuccess()) {
                    val product = selectedProduct.getSuccessData()

                    updateId(product.id)
                    updateCreatedAt(product.createdAt)
                    updateTitle(product.title)
                    updateDescription(product.description)
                    updateThumbnail(product.thumbnail)
                    updateThumbnailUploaderState(RequestState.Success(Unit))
                    updateCategory(product.category)
                    updateSubCategory(product.subCategory)
                    updatePrice(product.price)
                    updateSizes(product.sizes)
                    updateNew(product.isNew)
                    updatePopular(product.isPopular)
                    updateDiscounted(product.isDiscounted)
                    updateDiscountedPercent(product.discounted)
                    updateAvailable(product.isAvailable)

                    updateCoffeeShot(product.isCoffeeShot)
                    updateMilk(product.isMilk)
                    updateSweetness(product.isSweetness)
                    updateFlavorAndSyrup(product.isFlavorAndSyrup)
                    updateCondiment(product.isCondiment)
                    updateToppings(product.isToppings)
                    updateCutlery(product.isCutlery)
                    updateWarmUp(product.isWarmUp)
                }
            }
        }
    }


    fun updateId(value: String) {
        screenState = screenState.copy(id = value)
    }

    fun updateCreatedAt(value: Long) {
        screenState = screenState.copy(createdAt = value)
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

    fun updateCategory(value: ProductCategory, resetFields: Boolean = true) {
        val defaultSubCategory = getSubCategoriesFor(value).first()
        screenState = screenState.copy(
            category = value,
            subCategory = defaultSubCategory,
            sizes = if (resetFields) null else screenState.sizes,
            price = if (resetFields) 0.0 else screenState.price
        )
    }

    fun updateSubCategory(value: SubCategory) {
        screenState = screenState.copy(subCategory = value)
    }

    fun updatePrice(value: Double?) {
        screenState = screenState.copy(price = value ?: 0.0)
    }

    fun updateNew(value: Boolean) {
        screenState = screenState.copy(isNew = value)
    }

    fun updatePopular(value: Boolean) {
        screenState = screenState.copy(isPopular = value)
    }

    fun updateDiscounted(value: Boolean) {
        screenState = screenState.copy(
            isDiscounted = value,
            discounted = if (!value) 0 else screenState.discounted
        )
    }

    fun updateDiscountedPercent(value: Int?) {
        screenState = screenState.copy(discounted = value ?: 0)
    }

    fun updateAvailable(value: Boolean) {
        screenState = screenState.copy(isAvailable = value)
    }

    fun updateSizes(value: List<Size>?) {
        screenState = screenState.copy(sizes = value)
    }

    //Customization
    fun updateCoffeeShot(value: Boolean) {
        screenState = screenState.copy(isCoffeeShot = value)
    }
    fun updateMilk(value: Boolean) {
        screenState = screenState.copy(isMilk = value)
    }

    fun updateSweetness(value: Boolean) {
        screenState = screenState.copy(isSweetness = value)
    }
    fun updateFlavorAndSyrup(value: Boolean) {
        screenState = screenState.copy(isFlavorAndSyrup = value)
    }
    fun updateCondiment(value: Boolean) {
        screenState = screenState.copy(isCondiment = value)
    }
    fun updateToppings(value: Boolean) {
        screenState = screenState.copy(isToppings = value)
    }
    fun updateCutlery(value: Boolean) {
        screenState = screenState.copy(isCutlery = value)
    }
    fun updateWarmUp(value: Boolean) {
        screenState = screenState.copy(isWarmUp = value)
    }

    private fun resetState() {
        screenState = ManageProductScreenState()
        thumbnailUploaderState = RequestState.Idle
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
                    sizes = screenState.sizes,
                    isNew = screenState.isNew,
                    isPopular = screenState.isPopular,
                    isDiscounted = screenState.isDiscounted,
                    discounted = screenState.discounted,
                    isAvailable = screenState.isAvailable,

                    isCoffeeShot = screenState.isCoffeeShot,
                    isMilk = screenState.isMilk,
                    isSweetness = screenState.isSweetness,
                    isFlavorAndSyrup = screenState.isFlavorAndSyrup,
                    isCondiment = screenState.isCondiment,
                    isToppings = screenState.isToppings,
                    isCutlery = screenState.isCutlery,
                    isWarmUp = screenState.isWarmUp
                ),
                onSuccess = onSuccess,
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

                productId.takeIf { it.isNotEmpty() }?.let { id ->
                    adminRepository.updateProductThumbnail(
                        productId = id,
                        downloadUrl = downloadUrl,
                        onSuccess = {
                            onSuccess()
                            updateThumbnailUploaderState(RequestState.Success(Unit))
                            updateThumbnail(downloadUrl)
                        },
                        onError = { message ->
                            updateThumbnailUploaderState(RequestState.Error(message))
                        }
                    )
                } ?: run {
                    onSuccess()
                    updateThumbnailUploaderState(RequestState.Success(Unit))
                    updateThumbnail(downloadUrl)
                }

            } catch (e: Exception){
                updateThumbnailUploaderState(RequestState.Error("Error while uploading: $e"))
            }
        }
    }

    fun updateProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ){
        if (isFormValid){
            viewModelScope.launch {
                adminRepository.updateProduct(
                    product = Product(
                        id = screenState.id,
                        createdAt = screenState.createdAt,
                        title = screenState.title,
                        description = screenState.description,
                        thumbnail = screenState.thumbnail,
                        category = screenState.category,
                        subCategory = screenState.subCategory,
                        price = screenState.price,
                        sizes = screenState.sizes,
                        isNew = screenState.isNew,
                        isPopular = screenState.isPopular,
                        isDiscounted = screenState.isDiscounted,
                        discounted = screenState.discounted,
                        isAvailable = screenState.isAvailable,

                        isCoffeeShot = screenState.isCoffeeShot,
                        isMilk = screenState.isMilk,
                        isSweetness = screenState.isSweetness,
                        isFlavorAndSyrup = screenState.isFlavorAndSyrup,
                        isCondiment = screenState.isCondiment,
                        isToppings = screenState.isToppings,
                        isCutlery = screenState.isCutlery,
                        isWarmUp = screenState.isWarmUp
                    ),
                    onSuccess = onSuccess,
                    onError = onError
                )
            }
        } else {
            onError("Please fill in the information.")
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
                    productId.takeIf { it.isNotEmpty() }?.let { id ->
                        viewModelScope.launch {
                            adminRepository.updateProductThumbnail(
                                productId = id,
                                downloadUrl = "",
                                onSuccess = {
                                    updateThumbnail("")
                                    updateThumbnailUploaderState(RequestState.Idle)
                                    onSuccess()
                                },
                                onError = { message -> onError(message) }
                            )
                        }
                    } ?: run {
                        updateThumbnail("")
                        updateThumbnailUploaderState(RequestState.Idle)
                        onSuccess()
                    }

                },
                onError = onError
            )
        }
    }

    fun deleteProduct(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        productId.takeIf { it.isNotEmpty() }?.let { id ->
            viewModelScope.launch {
                adminRepository.deleteProduct(
                    productId = id,
                    onSuccess = {
                        deleteThumbnailFromStorage(
                            onSuccess = {},
                            onError = {}
                        )
                        onSuccess()
                    },
                    onError = { message -> onError(message) }
                )
            }
        }
    }
}