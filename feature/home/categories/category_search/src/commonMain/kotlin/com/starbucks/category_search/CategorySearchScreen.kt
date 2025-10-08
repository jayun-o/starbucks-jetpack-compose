package com.starbucks.category_search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.RaleWayFontFamily
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.component.InfoCard
import com.starbucks.shared.component.LoadingCard
import com.starbucks.shared.component.ProductCardItem
import com.starbucks.shared.domain.ProductCategory
import com.starbucks.shared.domain.BeverageSubCategory
import com.starbucks.shared.domain.FoodSubCategory
import com.starbucks.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySearchScreen(
    navigateBack: () -> Unit,
    navigateToDetails: (String) -> Unit,
    category: ProductCategory,
    subCategory: String
) {
    val viewModel = koinViewModel<CategorySearchViewModel>()
    val products by viewModel.products.collectAsState()

    // Filter products when screen loads or when category/subCategory changes
    LaunchedEffect(category, subCategory) {
        viewModel.filterProducts(category, subCategory)
    }

    // Get display title for subcategory
    val subCategoryTitle = when (category) {
        ProductCategory.BEVERAGE -> {
            BeverageSubCategory.entries
                .find { it.name == subCategory }?.title ?: subCategory
        }
        ProductCategory.FOOD -> {
            FoodSubCategory.entries
                .find { it.name == subCategory }?.title ?: subCategory
        }
    }

    Scaffold (
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = subCategoryTitle,
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
                actions = {
                    IconButton(onClick = {
                        // TODO: Implement search functionality
                    } ){
                        Icon(
                            painter = painterResource(Resources.Icon.Search),
                            contentDescription = "Search icon",
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
                ),
            )
        }
    ){ padding ->
        products.DisplayResult(
            onLoading = {
                LoadingCard(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                )
            },
            onSuccess = { categoryProducts ->
                AnimatedContent(
                    targetState = categoryProducts,
                    modifier = Modifier.padding(padding)
                ) { productsList ->
                    if(productsList.isNotEmpty()){
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ){
                            items(
                                items = productsList,
                                key = { it.id }
                            ){ product ->
                                ProductCardItem(
                                    product = product,
                                    onClick = { navigateToDetails(it) }
                                )
                            }
                        }
                    } else {
                        InfoCard(
                            image = Resources.Image.Cat,
                            title = "Nothing here",
                            subtitle = "We couldn't find any products in this category."
                        )
                    }
                }
            },
            onError = { message ->
                InfoCard(
                    modifier = Modifier.padding(padding),
                    image = Resources.Image.Cat,
                    title = "Oops!",
                    subtitle = message,
                )
            }
        )
    }
}