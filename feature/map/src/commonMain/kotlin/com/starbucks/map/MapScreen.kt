package com.starbucks.map

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.starbucks.shared.FontSize
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.LanguageManager
import com.starbucks.shared.LocalizedStrings
import com.starbucks.shared.RaleWayFontFamily
import com.starbucks.shared.Resources
import com.starbucks.shared.Surface
import com.starbucks.shared.TextPrimary
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navigateBack: () -> Unit
){
    val currentLanguage by LanguageManager.language.collectAsState()
    val geoLocation = remember { Geolocator.mobile() }

    // เก็บพิกัดผู้ใช้
    var userCoordinates by remember { mutableStateOf<Coordinates?>(null) }

    // เรียก Geolocator เพียงครั้งเดียว
    LaunchedEffect(Unit) {
        when (val result = geoLocation.current()) {
            is GeolocatorResult.Success -> {
                val coords = result.data.coordinates
//                println("LOCATION: $coords")
//                println("LOCATION NAME: ${MobileGeocoder().placeOrNull(coords)?.locality}")

                // อัปเดต userCoordinates เพื่อส่งเข้า GoogleMaps
                userCoordinates = Coordinates(coords.latitude, coords.longitude)
            }
            is GeolocatorResult.Error -> when (result) {
                is GeolocatorResult.NotSupported -> println("LOCATION ERROR: ${result.message}")
                is GeolocatorResult.NotFound -> println("LOCATION ERROR: ${result.message}")
                is GeolocatorResult.PermissionError -> println("LOCATION ERROR: ${result.message}")
                is GeolocatorResult.GeolocationFailed -> println("LOCATION ERROR: ${result.message}")
                else -> {
                    userCoordinates = null
                }
            }
        }
    }

    Scaffold (
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LocalizedStrings.get("location",currentLanguage),
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
    ){
        GoogleMaps(userCoordinates)
    }
}