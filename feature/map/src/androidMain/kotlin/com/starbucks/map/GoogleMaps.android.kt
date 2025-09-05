package com.starbucks.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
actual fun GoogleMaps(userLocation: Coordinates?) {
    val defaultLocation = LatLng(13.7563309, 100.5017651) // Bangkok

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLocation, 12f)
    }

    LaunchedEffect(userLocation) {
        userLocation?.let {
            cameraPositionState.animate(
                com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                    LatLng(it.latitude, it.longitude), 16f
                )
            )
        }
    }

    if (userLocation == null) {
        // แสดง Loading ระหว่างรอ GPS
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
            Text(text = "กำลังหาตำแหน่งของคุณ...", modifier = Modifier.align(Alignment.BottomCenter))
        }
    } else {
        // แสดงแผนที่เมื่อได้พิกัดแล้ว
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = true // ต้องเช็ค permission ก่อนเปิดจริง
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = true
            )
        )
    }
}
