package com.starbucks.map

import android.annotation.SuppressLint
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.starbucks.shared.domain.Coordinates
import com.starbucks.shared.*
import com.starbucks.shared.component.SecondaryButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.painterResource

@SuppressLint("MissingPermission")
@Composable
actual fun GoogleMaps(
    userLocation: Coordinates?,
    onLocationPicked: (Coordinates, String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val defaultLatLng = LatLng(13.7563, 100.5017) // Bangkok fallback
    var currentLatLng by remember { mutableStateOf(userLocation?.toLatLng() ?: defaultLatLng) }
    val markerState = remember { MarkerState(position = currentLatLng) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(currentLatLng, 16f)
    }

    var currentAddress by remember { mutableStateOf("กำลังค้นหาที่อยู่...") }

    // ดึงตำแหน่งจริงจาก device/emulator
    LaunchedEffect(Unit) {
        try {
            val location = fusedLocationClient.lastLocation.await()
            location?.let {
                val latLng = LatLng(it.latitude, it.longitude)
                currentLatLng = latLng
                markerState.position = latLng
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
            }
        } catch (_: SecurityException) { }
    }

    // Reverse geocode ทุกครั้งที่ marker ขยับ
    LaunchedEffect(markerState.position) {
        val geocoder = Geocoder(context)
        currentAddress = withContext(Dispatchers.IO) {
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
            properties = MapProperties(
                isMyLocationEnabled = true,
                isBuildingEnabled = true,
                isTrafficEnabled = false
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false,
                zoomControlsEnabled = false
            ),
            onMapClick = { latLng -> markerState.position = latLng }
        ) {
            Marker(
                state = markerState,
                title = "ตำแหน่งที่เลือก",
                snippet = currentAddress,
                draggable = true
            )
        }

        // ปุ่มข้างขวา
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 220.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        try {
                            val location = fusedLocationClient.lastLocation.await()
                            location?.let {
                                val latLng = LatLng(it.latitude, it.longitude)
                                markerState.position = latLng
                                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 16f))
                            }
                        } catch (_: SecurityException) { }
                    }
                },
                containerColor = White,
                contentColor = IconPrimary
            ) {
                Icon(painter = painterResource(Resources.Icon.myLocation), contentDescription = null)
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

        // แผงล่าง
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(White)
                .fillMaxWidth()
                .height(180.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = currentAddress)
            SecondaryButton(
                onClick = {
                    onLocationPicked(
                        Coordinates(markerState.position.latitude, markerState.position.longitude),
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

// Extension
fun Coordinates.toLatLng() = LatLng(this.latitude, this.longitude)
