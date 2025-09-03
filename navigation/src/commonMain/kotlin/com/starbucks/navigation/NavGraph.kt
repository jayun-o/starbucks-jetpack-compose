package com.starbucks.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.starbucks.auth.AuthScreen
import com.starbucks.home.HomeGraphScreen
import com.starbucks.profile.ProfileScreen
import com.starbucks.shared.component.maps.MapScreen
import com.starbucks.shared.navigation.Screen

@Composable
fun SetupNavGraph(startDestination: Screen = Screen.Auth){
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
                }
            )
        }
        composable<Screen.Profile> {
            ProfileScreen(
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToMap = {
                    navController.navigate(Screen.Maps)
                }
            )
        }
        composable<Screen.Maps> {
            MapScreen(
                navigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}