package com.starbucks.manage_product

import ContentWithMessageBar
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.starbucks.manage_product.component.CategoriesDropdown
import com.starbucks.manage_product.component.SubCategoriesDropdown
import com.starbucks.manage_product.util.PhotoPicker
import com.starbucks.shared.BorderIdle
import com.starbucks.shared.ButtonPrimary
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.IconWhite
import com.starbucks.shared.LanguageManager
import com.starbucks.shared.LocalizedStrings
import com.starbucks.shared.RaleWayFontFamily
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.TextSecondary
import com.starbucks.shared.component.CustomTextField
import com.starbucks.shared.component.ErrorCard
import com.starbucks.shared.component.LoadingCard
import com.starbucks.shared.component.PrimaryButton
import com.starbucks.shared.component.SizeOptionsSection
import com.starbucks.shared.domain.ProductCategory
import com.starbucks.shared.util.DisplayResult
import com.starbucks.shared.util.RequestState
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreen(
    id: String?,
    navigateBack: () -> Unit,
) {
    val currentLanguage by LanguageManager.language.collectAsState()
    val messageBarState = rememberMessageBarState()
    val viewModel = koinViewModel<ManageProductViewModel>()
    val thumbnailUploaderState = viewModel.thumbnailUploaderState
    val screenState = viewModel.screenState
    val isFormValid = viewModel.isFormValid
    var dropdownMenuOpened by remember { mutableStateOf(false) }

    val photoPicker = koinInject<PhotoPicker>()

    photoPicker.InitializePhotoPicker(
        onImageSelect = { file ->
            viewModel.uploadThumbnailToStorage(
                file = file,
                onSuccess = {
                    messageBarState.addSuccess("Thumbnail uploaded successfully!")
                }
            )
        }
    )

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (id == null) LocalizedStrings.get("new_product", currentLanguage)
                        else LocalizedStrings.get("edit_product", currentLanguage),
                        fontFamily = RaleWayFontFamily(),
                        fontSize = FontSize.EXTRA_MEDIUM,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back Arrow icon",
                            tint = IconPrimary
                        )
                    }
                },
                actions = {
                    id.takeIf { it != null }?.let{
                        Box {
                            IconButton(onClick = { dropdownMenuOpened = true }) {
                                Icon(
                                    painter = painterResource(Resources.Icon.VerticalMenu),
                                    contentDescription = "Vertical menu icon",
                                    tint = IconPrimary
                                )
                            }
                        }
                        DropdownMenu(
                            containerColor = Surface,
                            expanded = dropdownMenuOpened,
                            onDismissRequest = { dropdownMenuOpened = false }
                        ){
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        modifier = Modifier.size(14.dp),
                                        painter = painterResource(Resources.Icon.Delete),
                                        contentDescription = "Delete icon",
                                        tint = IconPrimary
                                    )
                                },
                                text = { Text(text = "Delete", color = TextPrimary) },
                                onClick = {
                                    dropdownMenuOpened = false
                                    viewModel.deleteProduct(
                                        onSuccess = navigateBack,
                                        onError = { message -> messageBarState.addError(message) }
                                    )
                                }
                            )
                        }
                    }

                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                )
            )
        }
    ) { padding ->
        ContentWithMessageBar(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            ),
            contentBackgroundColor = Surface,
            messageBarState = messageBarState,
            errorMaxLines = 2
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp, top = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Thumbnail Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, BorderIdle, RoundedCornerShape(12.dp))
                            .background(SurfaceLighter)
                            .clickable (
                                enabled = thumbnailUploaderState.isIdle(),
                            ){
                                photoPicker.open()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        thumbnailUploaderState.DisplayResult(
                            onIdle = {
                                Icon(
                                    modifier = Modifier.size(24.dp),
                                    painter = painterResource(Resources.Icon.Plus),
                                    contentDescription = "Camera icon",
                                    tint = IconPrimary
                                )
                            },
                            onLoading = {
                                LoadingCard(modifier = Modifier.fillMaxSize())
                            },
                            onSuccess = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.TopEnd
                                ){
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize(),
                                        model = ImageRequest.Builder(
                                            LocalPlatformContext.current
                                        ).data(screenState.thumbnail)
                                            .crossfade(enable = true)
                                            .build(),
                                        contentDescription = "Product thumbnail image",
                                        contentScale = ContentScale.Crop
                                    )
                                    Box(
                                        modifier = Modifier
                                            .padding(
                                                top = 12.dp,
                                                end = 12.dp
                                            )
                                            .clip(RoundedCornerShape(size = 6.dp))
                                            .background(ButtonPrimary)
                                            .clickable { viewModel.deleteThumbnailFromStorage(
                                                onSuccess = { messageBarState.addSuccess("Thumbnail deleted successfully!") },
                                                onError = { message -> messageBarState.addError(message) }
                                            ) }
                                            .padding(all = 12.dp),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Icon(
                                            modifier = Modifier.size(14.dp),
                                            painter = painterResource(Resources.Icon.Delete),
                                            contentDescription = "Delete icon",
                                            tint = IconWhite
                                        )
                                    }
                                }
                            },
                            onError = { message ->
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ){
                                    ErrorCard(message = message)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    TextButton(
                                        onClick = {
                                            viewModel.updateThumbnailUploaderState(RequestState.Idle)
                                        },
                                        colors = ButtonDefaults.textButtonColors(
                                            containerColor = Color.Transparent,
                                        )
                                    ){
                                        Text(
                                            text = "Try again",
                                            fontSize = FontSize.SMALL,
                                            color = TextSecondary
                                        )
                                    }
                                }
                            }
                        )
                    }

                    // Title & Description
                    CustomTextField(
                        value = screenState.title,
                        onValueChange = viewModel::updateTitle,
                        placeholder = "Title"
                    )

                    CustomTextField(
                        modifier = Modifier.height(168.dp),
                        value = screenState.description,
                        onValueChange = viewModel::updateDescription,
                        placeholder = "Description",
                        expanded = true
                    )

                    // Category & SubCategory
                    CategoriesDropdown(
                        category = screenState.category,
                        onCategorySelected = viewModel::updateCategory,
                        modifier = Modifier.fillMaxWidth()
                    )

                    SubCategoriesDropdown(
                        category = screenState.category,
                        selectedSubCategory = screenState.subCategory,
                        onSubCategorySelected = viewModel::updateSubCategory,
                        modifier = Modifier.fillMaxWidth()
                    )

                    AnimatedVisibility(
                        visible = screenState.category == ProductCategory.BEVERAGE
                    ){
                        Column {
                            SizeOptionsSection(
                                sizes = screenState.sizes,
                                onSizesChanged = viewModel::updateSizes
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    AnimatedVisibility(
                        visible = screenState.category == ProductCategory.FOOD
                    ) {
                        Column {
                            CustomTextField(
                                value = "${screenState.price}",
                                onValueChange = { value ->
                                    if (value.isEmpty() || value.toDoubleOrNull() != null) {
                                        viewModel.updatePrice(value.toDoubleOrNull() ?: 0.0)
                                    }
                                },
                                placeholder = "Price",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (id == null) "Add new product" else "Update",
                    icon = if (id == null) Resources.Icon.Plus else Resources.Icon.Checkmark,
                    enabled = isFormValid,
                    onClick = {
                        if (id != null){
                            viewModel.updateProduct(
                                onSuccess = { messageBarState.addSuccess("Product updated successfully!") },
                                onError = { message -> messageBarState.addError(message) }
                            )
                        } else {
                            viewModel.createNewProduct(
                                onSuccess = { messageBarState.addSuccess("Product added successfully!") },
                                onError = { message -> messageBarState.addError(message) }
                            )
                        }
                    }
                )
            }
        }
    }
}
