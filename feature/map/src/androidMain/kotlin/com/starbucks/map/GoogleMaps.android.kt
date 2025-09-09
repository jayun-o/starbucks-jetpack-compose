package com.starbucks.map

import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.starbucks.map.model.Coordinates
import com.starbucks.shared.ButtonPrimary
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.IconWhite
import com.starbucks.shared.Resources
import com.starbucks.shared.White
import com.starbucks.shared.component.SecondaryButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun GoogleMaps(
    userLocation: Coordinates?,
    onLocationPicked: (Coordinates, String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // fallback location
    val defaultLocation = LatLng(
        userLocation?.latitude ?: 13.7563,
        userLocation?.longitude ?: 100.5017
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 16f)
    }

    var currentAddress by remember { mutableStateOf("กำลังค้นหาที่อยู่...") }
    val selectedMarkerState = remember { MarkerState(position = defaultLocation) }

    // Animate กล้องไปตำแหน่งผู้ใช้เมื่อได้ userLocation
    LaunchedEffect(userLocation) {
        userLocation?.let { coords ->
            val latLng = LatLng(coords.latitude, coords.longitude)
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
            selectedMarkerState.position = latLng
        }
    }

    // Reverse geocode
    LaunchedEffect(selectedMarkerState.position) {
        val geocoder = Geocoder(context)
        currentAddress = kotlinx.coroutines.withContext(Dispatchers.IO) {
            geocoder.getFromLocation(
                selectedMarkerState.position.latitude,
                selectedMarkerState.position.longitude,
                1
            )?.firstOrNull()?.getAddressLine(0) ?: "ไม่พบที่อยู่"
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true), // ปิดจุดฟ้า Google HQ
            uiSettings = MapUiSettings(myLocationButtonEnabled = true),
            onMapClick = { latLng -> selectedMarkerState.position = latLng }
        ) {
            Marker(
                state = selectedMarkerState,
                title = "ตำแหน่งที่เลือก",
                snippet = currentAddress,
                draggable = true
            )
        }

        // ปุ่ม Zoom + Current Location
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 220.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    userLocation?.let {
                        scope.launch {
                            val latLng = LatLng(it.latitude, it.longitude)
                            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                            selectedMarkerState.position = latLng
                        }
                    }
                },
                containerColor = White,
                contentColor = IconPrimary
            ) {
                Icon(painterResource(Resources.Icon.myLocation), contentDescription = "Go to Current Location")
            }

            FloatingActionButton(
                onClick = { scope.launch { cameraPositionState.animate(CameraUpdateFactory.zoomIn()) } },
                containerColor = White,
                contentColor = IconPrimary
            ) {
                Icon(painterResource(Resources.Icon.Plus), contentDescription = "Zoom in")
            }

            FloatingActionButton(
                onClick = { scope.launch { cameraPositionState.animate(CameraUpdateFactory.zoomOut()) } },
                containerColor = White,
                contentColor = IconPrimary
            ) {
                Icon(painterResource(Resources.Icon.Minus), contentDescription = "Zoom out")
            }
        }

        // Panel ล่าง
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(White)
                .fillMaxWidth()
                .height(180.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = currentAddress)
            SecondaryButton(
                onClick = {
                    onLocationPicked(
                        Coordinates(
                            selectedMarkerState.position.latitude,
                            selectedMarkerState.position.longitude
                        ),
                        currentAddress
                    )
                },
                text = "Select Location",
                icon = Resources.Icon.MapPin,
                containerColor = ButtonPrimary,
                borderColor = ButtonPrimary,
                contentColor = IconWhite,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
