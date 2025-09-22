package com.starbucks.admin_panel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import com.starbucks.shared.ButtonPrimary
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.IconWhite
import com.starbucks.shared.LanguageManager
import com.starbucks.shared.LocalizedStrings
import com.starbucks.shared.RaleWayFontFamily
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.component.InfoCard
import com.starbucks.shared.component.LoadingCard
import com.starbucks.shared.component.ProductCard
import com.starbucks.shared.domain.displayName
import com.starbucks.shared.util.DisplayResult
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun AdminPanelScreen(
    navigateBack: () -> Unit,
    navigateToManageProduct: (String?) -> Unit,
){
    val currentLanguage by LanguageManager.language.collectAsState()
    val viewModel = koinViewModel<AdminPanelViewModel>()
    val products = viewModel.products.collectAsState()

    LaunchedEffect(products.value){
        println(products.value)
    }

    Scaffold (
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LocalizedStrings.get("admin_panel",
                            currentLanguage
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
                actions = {
                    IconButton(onClick = {}){
                        Icon(
                            painter = painterResource(Resources.Icon.Search),
                            contentDescription = "Search icon",
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
                 },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navigateToManageProduct(null) },
                    containerColor = ButtonPrimary,
                    contentColor = IconWhite,
                    content = {
                        Icon(
                            painter = painterResource(Resources.Icon.Plus),
                            contentDescription = "Add icon"
                        )
                    }
                )
            }
    ){ padding ->
        products.value.DisplayResult(
            modifier = Modifier
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                ),
            onLoading = { LoadingCard( modifier = Modifier.fillMaxSize()) },
            onSuccess = { lastProducts ->

                // --- Group by Category first ---
                val categoryGroups = lastProducts.groupBy { it.category }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(all = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    categoryGroups.forEach { (category, productsInCategory) ->

                        // --- Category Header ---
                        item {
                            Text(
                                text = category.displayName(),
                                fontFamily = RaleWayFontFamily(),
                                fontSize = FontSize.LARGE,
                                color = TextPrimary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // --- Group inside category by subCategory ---
                        val subCategoryGroups = productsInCategory.groupBy { it.subCategory?.title ?: "Others" }

                        subCategoryGroups.forEach { (subCategoryTitle, productsInSubCategory) ->

                            // SubCategory Title
                            item {
                                Text(
                                    text = subCategoryTitle,
                                    fontFamily = RaleWayFontFamily(),
                                    fontSize = FontSize.MEDIUM,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }

                            // Horizontal Scroll Row of products
                            item {
                                LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                ) {
                                    items(
                                        items = productsInSubCategory,
                                        key = { it.id }
                                    ) { product ->
                                        ProductCard(
                                            product = product,
                                            onClick = { navigateToManageProduct(product.id)},
                                            modifier = Modifier
                                                .padding(vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            },
            onError = { message ->
                InfoCard(
                    image = Resources.Image.Cat,
                    title = "Oops!",
                    subtitle = message
                )
            }

        )
    }
}
