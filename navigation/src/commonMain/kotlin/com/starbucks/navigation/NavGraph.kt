package com.starbucks.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.starbucks.auth.AuthScreen
import com.starbucks.home.HomeGraphScreen
import com.starbucks.map.Coordinates
import com.starbucks.map.MapScreen
import com.starbucks.profile.ProfileScreen
import com.starbucks.shared.navigation.Screen
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile

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
            // สมมติว่าคุณมี Geolocator หรือ LocationProvider
            val geoLocation = remember { Geolocator.mobile() }
            var userCoordinates by remember { mutableStateOf<Coordinates?>(null) }

            LaunchedEffect(Unit) {
                when (val result = geoLocation.current()) {
                    is GeolocatorResult.Success -> {
                        val coords = result.data.coordinates
                        userCoordinates = Coordinates(coords.latitude, coords.longitude)
                    }
                    else -> {
                        // error หรือ permission denied → userCoordinates = null
                        userCoordinates = null
                    }
                }
            }
            MapScreen(
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}