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
import rememberMessageBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageProductScreen(
    id: String?,
    navigateBack: () -> Unit,
) {

    val currentLanguage by LanguageManager.language.collectAsState()
    val messageBarState = rememberMessageBarState()
    // เก็บ state ของ Category / SubCategory ที่เลือก
    var selectedCategory by remember { mutableStateOf(ProductCategory.BEVERAGE) }
    var selectedSubCategory by remember { mutableStateOf<SubCategory?>(null) }

    // 🔥 Dynamic size options
    var sizes by remember { mutableStateOf(listOf<Size>()) }

    Scaffold (
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if( id == null) LocalizedStrings.get(
                            "new_product", currentLanguage
                        ) else LocalizedStrings.get(
                            "edit_product", currentLanguage
                        ),
                        fontFamily = RaleWayFontFamily(),
                        fontSize = FontSize.EXTRA_MEDIUM,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack){
                        Icon(
                            painter = painterResource(Resources.Icon.BackArrow),
                            contentDescription = "Back Arrow icon",
                            tint = IconPrimary
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Surface,
                        scrolledContainerColor = Surface,
                        navigationIconContentColor = IconPrimary,
                        titleContentColor = TextPrimary,
                        actionIconContentColor = IconPrimary
                    ),
            )
        }
    ){ padding ->
        ContentWithMessageBar(
            modifier = Modifier.padding(
                top = padding.calculateTopPadding(),
                bottom = padding.calculateBottomPadding()
            ),
            contentBackgroundColor = Surface,
            messageBarState = messageBarState,
            errorMaxLines = 2
        ){
            Column (
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(
                        bottom = 24.dp,
                        top = 12.dp
                    )
            ){
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(state = rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(size = 12.dp))
                            .border(
                                width = 1.dp,
                                color = BorderIdle,
                                shape = RoundedCornerShape(size = 12.dp)
                            )
                            .background(SurfaceLighter)
                            .clickable {  },
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            painter = painterResource(Resources.Icon.Plus),
                            contentDescription = "Plus icon",
                            tint = IconPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    CustomTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = "Title"
                    )
                    CustomTextField(
                        modifier = Modifier.height(168.dp),
                        value = "",
                        onValueChange = {},
                        placeholder = "Description",
                        expanded = true
                    )
                    CategoriesDropdown(
                        category = selectedCategory,
                        onCategorySelected = {
                            selectedCategory = it
                            selectedSubCategory = null
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    SubCategoriesDropdown(
                        category = selectedCategory,
                        selectedSubCategory = selectedSubCategory,
                        onSubCategorySelected = { selectedSubCategory = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (selectedCategory.title == "Beverage"){
                        Text("Size Options", color = TextPrimary, fontSize = FontSize.REGULAR)
                        // Add new option button
                        SecondaryButton(
                            onClick = {
                                sizes = sizes + Size(name = "", price = 0.0)
                            },
                            text = "Add size option",
                            icon = Resources.Icon.Plus,
                            borderColor = ButtonPrimary,
                            contentColor = IconPrimary,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                        sizes.forEachIndexed { index, size ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                CustomTextField(
                                    value = size.name,
                                    onValueChange = { newValue ->
                                        sizes = sizes.toMutableList().also {
                                            it[index] = it[index].copy(name = newValue)
                                        }
                                    },
                                    placeholder = "Size Name"
                                )
                                // Price
                                CustomTextField(
                                    value = size.price.toString(),
                                    onValueChange = { newValue ->
                                        val parsed = newValue.toDoubleOrNull() ?: 0.0
                                        sizes = sizes.toMutableList().also {
                                            it[index] = it[index].copy(price = parsed)
                                        }
                                    },
                                    placeholder = "Price",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                // ลบ option
                                SecondaryButton(
                                    onClick = {
                                        sizes = sizes.toMutableList().also { it.removeAt(index) }
                                    },
                                    text = "Delete size",
                                    icon = Resources.Icon.Delete,
                                    borderColor = Red,
                                    contentColor = IconPrimary,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 12.dp),
                                )
                            }
                        }
                    } else {
                        CustomTextField(
                            value = "",
                            onValueChange = {},
                            placeholder = "Size"
                        )
                        CustomTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = "Price",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                            )
                        )
                    }



                }

                Spacer(modifier = Modifier.height(24.dp))

                //Test Button
                Button(
                    onClick = {
                        println("Selected Category: ${selectedCategory.title}")
                        println("Selected SubCategory: ${selectedSubCategory?.title ?: "None"}")
                        println("Sizes: $sizes")
                    },
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Text("Save Product")
                }

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if (id == null) "Add new product"
                    else "Update",
                    icon = if (id == null) Resources.Icon.Plus
                    else Resources.Icon.Checkmark,
                    onClick = {}
                )
            }
        }
    }
}