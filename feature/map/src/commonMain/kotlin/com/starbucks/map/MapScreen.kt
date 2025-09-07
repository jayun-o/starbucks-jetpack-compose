package com.starbucks.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.jordond.compass.geolocation.Geolocator
import dev.jordond.compass.geolocation.GeolocatorResult
import dev.jordond.compass.geolocation.mobile
import org.jetbrains.compose.resources.painterResource
import com.starbucks.shared.*
import kotlinx.coroutines.withTimeoutOrNull

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navigateBack: () -> Unit,
    onLocationPicked: (Coordinates, String) -> Unit
) {
    val currentLanguage by LanguageManager.language.collectAsState()
    var userCoordinates by remember { mutableStateOf<Coordinates?>(null) }
    var loading by remember { mutableStateOf(true) }
    var permissionDenied by remember { mutableStateOf(false) }

    // ตรวจสอบ location
    LaunchedEffect(Unit) {
        val geoLocation = Geolocator.mobile()
        try {
            val result = withTimeoutOrNull(5000L) { geoLocation.current() } // 5 วินาที timeout
            when(result) {
                is GeolocatorResult.Success -> {
                    val coords = result.data.coordinates
                    userCoordinates = Coordinates(coords.latitude, coords.longitude)
                }
                is GeolocatorResult.Error -> {
                    if(result is GeolocatorResult.PermissionError) permissionDenied = true
                    else userCoordinates = Coordinates(13.7563, 100.5017) // fallback default
                }
                null -> userCoordinates = Coordinates(13.7563, 100.5017) // timeout fallback
            }
        } catch(e: Exception){
            userCoordinates = Coordinates(13.7563, 100.5017) // fallback
        }
        loading = false
    }


    if(permissionDenied) {
        LaunchedEffect(Unit){
            navigateBack()
        }
        return
    }

    Scaffold(
        containerColor = Surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = LocalizedStrings.get("location", currentLanguage),
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Surface,
                    scrolledContainerColor = Surface,
                    navigationIconContentColor = IconPrimary,
                    titleContentColor = TextPrimary,
                    actionIconContentColor = IconPrimary
                )
            )
        }
    ){
        if(loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        } else {
            GoogleMaps(
                userLocation = userCoordinates,
                onLocationPicked = { coords, address ->
                    val locationString = "$address"
                    println("เลือกพิกัด: ${coords.latitude}, ${coords.longitude}")
                    println("ที่อยู่: $address")
                    navigateBack()
                }
            )
        }
    }
}