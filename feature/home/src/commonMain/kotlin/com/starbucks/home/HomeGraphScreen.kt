package com.starbucks.home

import ContentWithMessageBar
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.starbucks.all_products.AllProductsScreen
import com.starbucks.cart.CartScreen
import com.starbucks.categories.CategoryScreen
import com.starbucks.category_search.CategorySearchScreen
import com.starbucks.checkout.CheckoutScreen
import com.starbucks.home.component.BottomBar
import com.starbucks.home.component.CustomDrawer
import com.starbucks.home.domain.BottomBarDestination
import com.starbucks.home.domain.CustomDrawerState
import com.starbucks.home.domain.isOpened
import com.starbucks.home.domain.opposite
import com.starbucks.products_overview.ProductsOverviewScreen
import com.starbucks.shared.Alpha
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.LanguageManager
import com.starbucks.shared.LocalizedStrings
import com.starbucks.shared.RaleWayFontFamily
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.SurfaceLighter
import com.starbucks.shared.TextPrimary
import com.starbucks.shared.domain.ProductCategory
import com.starbucks.shared.navigation.Screen
import com.starbucks.shared.util.getScreenWidth
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import rememberMessageBarState
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeGraphScreen(
    navigateToAuth : () -> Unit,
    navigateToProfile: () -> Unit,
    navigateToOrder: () -> Unit,
    navigateToAdminPanel: () -> Unit,
    navigateToDetails: (String) -> Unit,
    navigateToMap: () -> Unit,
    checkoutSelectedLocation: String? = null,
    navigateToPaymentCompleted: (Boolean?, String?) -> Unit
){
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState()

    val selectedDestination by remember {
        derivedStateOf {
            when {
                currentRoute.value?.destination?.route == Screen.Cart::class.qualifiedName -> BottomBarDestination.Cart
                currentRoute.value?.destination?.route == Screen.Categories::class.qualifiedName -> BottomBarDestination.Categories
                currentRoute.value?.destination?.route?.contains("Checkout") == true -> BottomBarDestination.Cart
                else -> BottomBarDestination.ProductsOverview
            }
        }
    }

    // Check if current screen is Checkout
    val isCheckoutScreen by remember {
        derivedStateOf {
            currentRoute.value?.destination?.route?.contains("Checkout") == true
        }
    }

    val screenWidth = remember { getScreenWidth() }
    var drawerState by remember { mutableStateOf(CustomDrawerState.Closed) }
    val offsetValue by remember{ derivedStateOf { (screenWidth / 1.5).dp }}
    val animatedOffset by animateDpAsState(
        targetValue = if(drawerState.isOpened()) offsetValue else 0.dp,
    )
    val animatedBackground by animateColorAsState(
        targetValue = if(drawerState.isOpened()) SurfaceLighter else Surface,
    )
    val animatedScale by animateFloatAsState(
        targetValue = if (drawerState.isOpened()) 0.9f else 1f
    )
    val animatedRadius by animateDpAsState(
        targetValue = if (drawerState.isOpened()) 20.dp else 0.dp
    )

    val viewModel = koinViewModel<HomeGraphViewModel>()
    val customer by viewModel.customer.collectAsState()

    val messageBarState = rememberMessageBarState()

    val currentLanguage by LanguageManager.language.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(animatedBackground)
            .systemBarsPadding()
    ){
        CustomDrawer(
            customer = customer,
            onProfileClick = navigateToProfile,
            onOrderClick = navigateToOrder,
            onContactUsClick = {},
            onSignOutClick = {
                viewModel.signOut(
                    onSuccess = navigateToAuth,
                    onError = { message -> messageBarState.addError(message) }
                )
            },
            onAdminPanelClick = navigateToAdminPanel
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(size = animatedRadius))
                .offset(x = animatedOffset)
                .scale(scale = animatedScale)
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(size = animatedRadius),
                    ambientColor = Color.Black.copy(alpha = Alpha.DISABLED),
                    spotColor = Color.Black.copy(alpha = Alpha.DISABLED)
                )
        ){
            Scaffold (
                containerColor = Surface,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            val currentLang by LanguageManager.language.collectAsState()
                            AnimatedContent(
                                targetState = Triple(selectedDestination, currentLang, isCheckoutScreen),
                            ) { (destination, _, isCheckout) ->
                                Text(
                                    text = if (isCheckout) {
                                        "Checkout"
                                    } else {
                                        LocalizedStrings.get(
                                            destination.titleKey,
                                            currentLanguage
                                        )
                                    },
                                    fontFamily = RaleWayFontFamily(),
                                    fontSize = FontSize.EXTRA_MEDIUM,
                                    color = TextPrimary
                                )
                            }
                        },
                        navigationIcon = {
                            AnimatedContent(
                                targetState = Pair(drawerState, isCheckoutScreen)
                            ){ (drawer, isCheckout) ->
                                if (isCheckout) {
                                    IconButton(onClick = { navController.navigateUp() }){
                                        Icon(
                                            painter = painterResource(Resources.Icon.BackArrow),
                                            contentDescription = "Back icon",
                                            tint = IconPrimary
                                        )
                                    }
                                } else if (drawer.isOpened()){
                                    IconButton(onClick = { drawerState = drawerState.opposite() }){
                                        Icon(
                                            painter = painterResource(Resources.Icon.Close),
                                            contentDescription = "Close icon",
                                            tint = IconPrimary
                                        )
                                    }
                                } else {
                                    IconButton(onClick = { drawerState = drawerState.opposite() }){
                                        Icon(
                                            painter = painterResource(Resources.Icon.Menu),
                                            contentDescription = "Menu icon",
                                            tint = IconPrimary
                                        )
                                    }
                                }
                            }
                        },
                        actions = {
                            val currentLang by LanguageManager.language.collectAsState()
                            IconButton(onClick = {
                                val newLang = if (currentLang == "en") "th" else "en"
                                LanguageManager.setLanguage(newLang)
                            }) {
                                Text(
                                    text = currentLang.uppercase(),
                                    fontFamily = RaleWayFontFamily(),
                                    fontSize = FontSize.MEDIUM,
                                    color = IconPrimary
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
                bottomBar = {
                    BottomBar(
                        customer = customer,
                        selected = selectedDestination,
                        onSelect = { destination ->
                            navController.navigate(destination.screen){
                                launchSingleTop = true
                                popUpTo(Screen.ProductsOverview){ saveState = true }
                                restoreState = true
                            }
                        }
                    )
                }
            ){ padding ->
                ContentWithMessageBar(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    messageBarState = messageBarState,
                    errorMaxLines = 2,
                    contentBackgroundColor = Surface
                ){
                    NavHost(
                        navController = navController,
                        startDestination = Screen.ProductsOverview,
                        modifier = Modifier.fillMaxSize()
                    ){
                        composable<Screen.ProductsOverview> {
                            ProductsOverviewScreen(
                                navigateToDetails = navigateToDetails
                            )
                        }
                        composable<Screen.Cart> {
                            CartScreen(
                                navigateToCheckout = { totalAmount ->
                                    navController.navigate(Screen.Checkout(totalAmount.toString()))
                                }
                            )
                        }
                        composable<Screen.Categories> {
                            CategoryScreen(
                                onNavigateToAllProducts = { category ->
                                    navController.navigate(Screen.AllProducts(category.name))
                                },
                                navigateToCategorySearch = { category, subCategory ->
                                    navController.navigate(Screen.CategorySearch(category, subCategory))
                                }
                            )
                        }

                        composable<Screen.CategorySearch> {
                            val route = it.toRoute<Screen.CategorySearch>()
                            val category = ProductCategory.valueOf(route.category)
                            val subCategoryName = route.subCategory

                            CategorySearchScreen(
                                category = category,
                                subCategory = subCategoryName,
                                navigateBack = { navController.navigateUp() },
                                navigateToDetails = navigateToDetails
                            )
                        }

                        composable<Screen.AllProducts> {
                            val route = it.toRoute<Screen.AllProducts>()
                            val category = ProductCategory.valueOf(route.category)

                            AllProductsScreen(
                                category = category,
                                navigateBack = { navController.navigateUp() },
                                navigateToDetails = navigateToDetails
                            )
                        }

                        composable<Screen.Checkout> {
                            val route = it.toRoute<Screen.Checkout>()
                            val totalAmount = route.totalAmount.toDoubleOrNull() ?: 0.0

                            CheckoutScreen(
                                totalAmount = totalAmount,
                                navigateToMap = navigateToMap,
                                selectedLocation = checkoutSelectedLocation,
                                navigateToPaymentCompleted = navigateToPaymentCompleted
                            )
                        }
                    }
                }
            }
        }
    }
}