package com.starbucks.manage_product

import ContentWithMessageBar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.starbucks.manage_product.component.CategoriesDropdown
import com.starbucks.manage_product.component.SubCategoriesDropdown
import com.starbucks.shared.BorderIdle
import com.starbucks.shared.ButtonPrimary
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.LanguageManager
import com.starbucks.shared.LocalizedStrings
import com.starbucks.shared.RaleWayFontFamily
import com.starbucks.shared.Red
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.component.CustomTextField
import com.starbucks.shared.component.PrimaryButton
import com.starbucks.shared.component.SecondaryButton
import com.starbucks.shared.domain.ProductCategory
import com.starbucks.shared.domain.Size
import com.starbucks.shared.domain.SubCategory
import org.jetbrains.compose.resources.painterResource
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
    val screenState = viewModel.screenState
    val isFormValid = viewModel.isFormValid

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
                            .clickable {
                                // TODO: Open image picker
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Resources.Icon.Plus),
                            contentDescription = "Plus icon",
                            tint = IconPrimary,
                            modifier = Modifier.size(24.dp)
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

                    // Size options (เฉพาะ BEVERAGE)
                    if (screenState.category == ProductCategory.BEVERAGE) {
                        Text("Size Options", color = TextPrimary, fontSize = FontSize.REGULAR)

                        SecondaryButton(
                            onClick = {
                                val newSizes = (screenState.sizes ?: emptyList()) + Size(name = "", price = 0.0)
                                viewModel.updateSizes(newSizes)
                            },
                            text = "Add size option",
                            icon = Resources.Icon.Plus,
                            borderColor = ButtonPrimary,
                            contentColor = IconPrimary,
                            modifier = Modifier.fillMaxWidth()
                        )

                        (screenState.sizes ?: emptyList()).forEachIndexed { index, size ->
                            Column(modifier = Modifier.fillMaxWidth()) {
                                CustomTextField(
                                    value = size.name,
                                    onValueChange = { newValue ->
                                        val newSizes = screenState.sizes!!.toMutableList()
                                        newSizes[index] = newSizes[index].copy(name = newValue)
                                        viewModel.updateSizes(newSizes)
                                    },
                                    placeholder = "Size Name"
                                )

                                CustomTextField(
                                    value = size.price.toString(),
                                    onValueChange = { newValue ->
                                        val parsed = newValue.toDoubleOrNull() ?: 0.0
                                        val newSizes = screenState.sizes!!.toMutableList()
                                        newSizes[index] = newSizes[index].copy(price = parsed)
                                        viewModel.updateSizes(newSizes)
                                    },
                                    placeholder = "Price",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )

                                SecondaryButton(
                                    onClick = {
                                        val newSizes = screenState.sizes!!.toMutableList().also { it.removeAt(index) }
                                        viewModel.updateSizes(newSizes)
                                    },
                                    text = "Delete size",
                                    icon = Resources.Icon.Delete,
                                    borderColor = Red,
                                    contentColor = IconPrimary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp)
                                )
                            }
                        }
                    } else {
                        CustomTextField(
                            value = "${screenState.price}",
                            onValueChange = { value ->
                                if (value.isEmpty() || value.toDoubleOrNull() != null) {
                                    viewModel.updatePrice(value.toDoubleOrNull() ?: 0.0)
                                }
                            },
                            placeholder = "Price",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Test button
                Button(
                    onClick = {
                        println("ScreenState: $screenState")
                    },
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Text("Save Product")
                }

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (id == null) "Add new product" else "Update",
                    icon = if (id == null) Resources.Icon.Plus else Resources.Icon.Checkmark,
                    enabled = isFormValid,
                    onClick = {
                        viewModel.createNewProduct(
                            onSuccess = {
                                messageBarState.addSuccess("Product added successfully!")
                            },
                            onError = { message ->
                                messageBarState.addError(message)
                            }
                        )
                    }
                )
            }
        }
    }
}
