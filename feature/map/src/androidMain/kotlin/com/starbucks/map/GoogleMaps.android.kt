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
import com.starbucks.shared.Alpha
import com.starbucks.shared.ButtonPrimary
import com.starbucks.shared.IconPrimary
import com.starbucks.shared.IconWhite
import com.starbucks.shared.Resources
import com.starbucks.shared.White
import com.starbucks.shared.component.SecondaryButton
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

@Composable
actual fun GoogleMaps(
    userLocation: Coordinates?,
    onLocationPicked: (Coordinates, String) -> Unit
) {
    val context = LocalContext.current
    val defaultLocation = LatLng(13.7563309, 100.5017651)
    val scope = rememberCoroutineScope() // สำหรับ animate camera

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            userLocation?.let { LatLng(it.latitude, it.longitude) } ?: defaultLocation,
            16f // zoom ระดับเริ่มต้น
        )
    }


    var currentAddress by remember { mutableStateOf("กำลังค้นหาที่อยู่...") }
    val markerState = remember { MarkerState(position = defaultLocation) }

    // Zoom ไปตำแหน่งผู้ใช้ครั้งแรก
    LaunchedEffect(userLocation) {
        userLocation?.let {
            val latLng = LatLng(it.latitude, it.longitude)
            // Animate camera เพิ่ม smooth effect หลังจาก load
            cameraPositionState.animate(CameraUpdateFactory.newLatLng(latLng))
            markerState.position = latLng
        }
    }


    // Reverse geocode จาก markerState
    LaunchedEffect(markerState.position) {
        val geocoder = Geocoder(context)
        currentAddress = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            geocoder.getFromLocation(
                markerState.position.latitude,
                markerState.position.longitude,
                1
            )?.firstOrNull()?.getAddressLine(0) ?: "ไม่พบที่อยู่"
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(myLocationButtonEnabled = true),
            onMapClick = { latLng ->
                markerState.position = latLng
            }
        ) {
            Marker(
                state = markerState,
                title = "ตำแหน่งที่เลือก",
                snippet = currentAddress,
                draggable = true
            )
        }

        if (userLocation != null) {
            val scope = rememberCoroutineScope()

            // จัดปุ่มเป็น Column อยู่ขอบขวากลาง
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 220.dp), // กัน panel ล่าง
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // FAB current location
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            val latLng = LatLng(userLocation.latitude, userLocation.longitude)
                            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                            markerState.position = latLng
                        }
                    },
                    containerColor = White,
                    contentColor = IconPrimary
                ) {
                    Icon(
                        painter = painterResource(Resources.Icon.MapPin),
                        contentDescription = "Go to Current Location"
                    )
                }

                // FAB Zoom +
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            cameraPositionState.animate(CameraUpdateFactory.zoomIn())
                        }
                    },
                    containerColor = White,
                    contentColor = IconPrimary
                ) {
                    Icon(
                        painter = painterResource(Resources.Icon.Plus),
                        contentDescription = "Zoom in"
                    )
                }

                // FAB Zoom -
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                        }
                    },
                    containerColor = White,
                    contentColor = IconPrimary
                ) {
                    Icon(
                    painter = painterResource(Resources.Icon.Minus),
                    contentDescription = "Zoom out"
                    )
                }
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
                            markerState.position.latitude,
                            markerState.position.longitude
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
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}
