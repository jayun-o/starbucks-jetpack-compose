package com.starbucks.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.starbucks.admin_panel.AdminPanelScreen
import com.starbucks.auth.AuthScreen
import com.starbucks.details.DetailsScreen
import com.starbucks.home.HomeGraphScreen
import com.starbucks.manage_product.ManageProductScreen
import com.starbucks.map.MapScreen
import com.starbucks.payment_completed.PaymentCompletedScreen
import com.starbucks.profile.ProfileScreen
import com.starbucks.shared.navigation.Screen
import androidx.compose.runtime.getValue
import com.starbucks.shared.util.IntentHandler
import com.starbucks.shared.util.PreferencesRepository
import org.koin.compose.koinInject

@Composable
fun SetupNavGraph(
    startDestination: Screen = Screen.Auth
){
    val navController = rememberNavController()
    val intentHandler = koinInject<IntentHandler>()
    val navigateTo by intentHandler.navigateTo.collectAsState()

    LaunchedEffect(navigateTo){
        println("NAVIGATING TO PAYMENT COMPLETED!")
        navigateTo?.let { paymentCompleted ->
            navController.navigate(paymentCompleted)
            intentHandler.resetNavigation()
        }
    }

    val preferencesData by PreferencesRepository.readPayPalDataFlow()
        .collectAsState(initial = null)

    LaunchedEffect(preferencesData) {
        preferencesData?.let { paymentCompleted ->
            if(paymentCompleted.token != null) {
                navController.navigate(paymentCompleted)
                PreferencesRepository.reset()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ){
        composable<Screen.Auth>{
            AuthScreen(
                navigateToHome = {
                    navController.navigate(Screen.HomeGraph){
                        popUpTo(Screen.Auth){ inclusive = true }
                    }
                }
            )
        }

        composable<Screen.HomeGraph> {
            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            val checkoutSelectedLocation = savedStateHandle?.getStateFlow<String?>("checkout_location", null)
                ?.collectAsState()

            HomeGraphScreen(
                navigateToAuth = {
                    navController.navigate(Screen.Auth){
                        popUpTo(Screen.HomeGraph){ inclusive = true }
                    }
                },
                navigateToProfile = {
                    navController.navigate(Screen.Profile)
                },
                navigateToAdminPanel = {
                    navController.navigate(Screen.AdminPanel)
                },
                navigateToDetails = { productId ->
                    navController.navigate(Screen.Details(productId))
                },
                navigateToMap = {
                    navController.navigate(Screen.Maps(location = "checkout"))
                },
                checkoutSelectedLocation = checkoutSelectedLocation?.value,
                navigateToPaymentCompleted = { isSuccess, error ->
                    navController.navigate(Screen.PaymentCompleted(isSuccess, error))
                }
            )
        }

        composable<Screen.Profile> {
            val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
            val selectedLocation = savedStateHandle?.getStateFlow<String?>("selected_location", null)
                ?.collectAsState()

            ProfileScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToMap = {
                    navController.navigate(Screen.Maps())
                },
                selectedLocation = selectedLocation?.value
            )
        }

        composable<Screen.Maps> {
            val route = it.toRoute<Screen.Maps>()
            val locationKey = if (route.location == "checkout") "checkout_location" else "selected_location"

            MapScreen(
                navigateBack = { navController.navigateUp() },
                onLocationSelected = { address ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(locationKey, address)
                    navController.navigateUp()
                }
            )
        }

        composable<Screen.AdminPanel> {
            AdminPanelScreen (
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToManageProduct = { id ->
                    navController.navigate(Screen.ManageProduct(id))
                }
            )
        }

        composable<Screen.ManageProduct> {
            val id = it.toRoute<Screen.ManageProduct>().id
            ManageProductScreen(
                id = id,
                navigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.Details> {
            DetailsScreen(
                navigateBack = { navController.navigateUp() }
            )
        }

        composable<Screen.PaymentCompleted> {
            val route = it.toRoute<Screen.PaymentCompleted>()
            PaymentCompletedScreen(
                navigateBack = {
                    navController.navigate(Screen.HomeGraph) {
                        popUpTo(Screen.HomeGraph) { inclusive = true }
                    }
                }
            )
        }
    }
}