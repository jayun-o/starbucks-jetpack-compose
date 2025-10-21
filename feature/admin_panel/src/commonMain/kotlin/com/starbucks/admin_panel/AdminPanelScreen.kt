package com.starbucks.admin_panel

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import com.starbucks.shared.component.InfoCard
import com.starbucks.shared.component.LoadingCard
import com.starbucks.shared.component.ProductCard
import com.starbucks.shared.component.sendAdminLocalNotification
import com.starbucks.shared.domain.displayName
import com.starbucks.shared.util.DisplayResult
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import starbucks.feature.admin_panel.generated.resources.Res

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    navigateBack: () -> Unit,
    navigateToManageProduct: (String?) -> Unit,
){
    val currentLanguage by LanguageManager.language.collectAsState()
    val viewModel = koinViewModel<AdminPanelViewModel>()
    val products = viewModel.filteredProducts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var searchBarVisible by mutableStateOf(false)

    var showNotificationDialog by remember { mutableStateOf(false) }
    var notificationTitle by remember { mutableStateOf("") }
    var notificationBody by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    if (showNotificationDialog) {
        AlertDialog(
            containerColor = SurfaceLighter,
            onDismissRequest = { showNotificationDialog = false },
            title = { Text("Send Local Notification") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = notificationTitle,
                        onValueChange = { notificationTitle = it },
                        label = { Text("Notification Title") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = notificationBody,
                        onValueChange = { notificationBody = it },
                        label = { Text("Notification Body") },
                        singleLine = false
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (notificationTitle.isNotBlank() && notificationBody.isNotBlank()) {
                        scope.launch {
                            sendAdminLocalNotification(
                                title = notificationTitle,
                                body = notificationBody
                            )
                        }
                        showNotificationDialog = false
                    }
                }) {
                    Text("Send")
                }
            },
            dismissButton = {
                TextButton(onClick = { showNotificationDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold (
        containerColor = Surface,
        topBar = {
            AnimatedContent(targetState = searchBarVisible) { visible ->
                if(visible){
                    SearchBar(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth(),
                        inputField = {
                            SearchBarDefaults.InputField(
                                modifier = Modifier.fillMaxWidth(),
                                query = searchQuery,
                                onQueryChange = viewModel::updateSearchQuery, // ใช้ตรงนี้
                                expanded = false,
                                onExpandedChange = {},
                                onSearch = {},
                                placeholder = {
                                    Text(
                                        text = "Search here",
                                        fontSize = FontSize.REGULAR,
                                        color = TextPrimary
                                    )
                                },
                                trailingIcon = {
                                    IconButton(
                                        modifier = Modifier.size(14.dp),
                                        onClick = {
                                            if(searchQuery.isNotEmpty()) viewModel.updateSearchQuery("")
                                            else searchBarVisible = false
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(Resources.Icon.Close),
                                            contentDescription = "Close icon",
                                        )
                                    }
                                }
                            )
                        },
                        colors = SearchBarColors(
                            containerColor = SurfaceLighter,
                            dividerColor = BorderIdle,
                        ),
                        expanded = false,
                        onExpandedChange = { },
                        content = { }
                    )
                } else {
                    TopAppBar(
                        title = {
                            Text(
                                text = LocalizedStrings.get("admin_panel", currentLanguage),
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
                            IconButton(onClick = { searchBarVisible = true } ){
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
            }
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                FloatingActionButton(
                    onClick = { showNotificationDialog = true },
                    containerColor = ButtonPrimary,
                    contentColor = IconWhite
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(Resources.Icon.Notify),
                        contentDescription = "Notification icon"
                    )
                }

                FloatingActionButton(
                    onClick = { navigateToManageProduct(null) },
                    containerColor = ButtonPrimary,
                    contentColor = IconWhite,
                ) {
                    Icon(
                        painter = painterResource(Resources.Icon.Plus),
                        contentDescription = "Add icon"
                    )
                }
            }
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
                AnimatedContent(targetState = lastProducts) { products ->
                    if(products.isNotEmpty()){
                        val categoryGroups = lastProducts.groupBy { it.category }

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(all = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            categoryGroups.forEach { (category, productsInCategory) ->
                                item {
                                    Text(
                                        text = category.displayName(),
                                        fontFamily = RaleWayFontFamily(),
                                        fontSize = FontSize.LARGE,
                                        color = TextPrimary,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )
                                }

                                val subCategoryGroups = productsInCategory.groupBy { it.subCategory?.title ?: "Others" }

                                subCategoryGroups.forEach { (subCategoryTitle, productsInSubCategory) ->
                                    item {
                                        Text(
                                            text = subCategoryTitle,
                                            fontFamily = RaleWayFontFamily(),
                                            fontSize = FontSize.MEDIUM,
                                            color = TextPrimary,
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        )
                                    }

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
                                                    onClick = { navigateToManageProduct(product.id) },
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        InfoCard(
                            image = Resources.Image.Cat,
                            title = "Oops!",
                            subtitle = "No products found."
                        )
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
