package com.ola.recoverunsold.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun LocationMap(
    modifier: Modifier = Modifier,
    latLng: LatLng? = null,
    onLatLngUpdate: (LatLng) -> Unit
) {
    // TODO : if latlng is null get current position else 0.0
    var latLngData by mutableStateOf(latLng ?: LatLng(0.0, 0.0))

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLngData, 10f)
    }

    GoogleMap(
        modifier = modifier.fillMaxWidth(),
        cameraPositionState = cameraPositionState,
        onMapLongClick = {
            latLngData = it
            onLatLngUpdate(it)
        }
    ) {
        Marker(
            position = latLngData
        )
    }
}