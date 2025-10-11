package com.starbucks.navigation

import androidx.compose.runtime.Composable
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
import com.starbucks.profile.ProfileScreen
import com.starbucks.shared.navigation.Screen

@Composable
fun SetupNavGraph(
    startDestination: Screen = Screen.Auth
){
    val navController = rememberNavController()

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
            MapScreen(
                navigateBack = { navController.navigateUp() },
                onLocationSelected = { address ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_location", address)
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
    }
}