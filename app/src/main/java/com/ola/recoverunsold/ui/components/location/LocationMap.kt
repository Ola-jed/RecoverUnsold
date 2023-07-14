package com.ola.recoverunsold.ui.components.location

import android.Manifest
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.ola.recoverunsold.utils.extensions.getDeviceLocation

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationMap(
    modifier: Modifier = Modifier,
    latLng: LatLng? = null,
    onLatLngUpdate: (LatLng) -> Unit,
    onLocationFetchFailed: () -> Unit
) {
    var latLngData by rememberSaveable { mutableStateOf(latLng ?: LatLng(0.0, 0.0)) }
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPositionState = rememberCameraPositionState {
        position = (CameraPosition.fromLatLngZoom(
            latLngData,
            25f
        ))
    }

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                locationPermissionState.launchPermissionRequest()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    })

    when {
        locationPermissionState.hasPermission -> {
            context.getDeviceLocation(
                onLatLngValueUpdate = {
                    latLngData = it
                    onLatLngUpdate(it)
                },
                onLocationFetchFailed = onLocationFetchFailed
            )
        }
        else -> {}
    }

    GoogleMap(
        modifier = modifier.fillMaxWidth(),
        cameraPositionState = cameraPositionState,
        onMapLongClick = {
            latLngData = it
            onLatLngUpdate(it)
        },
        properties = MapProperties(mapType = MapType.HYBRID)
    ) {
        Marker(
            state = MarkerState(position = latLngData)
        )
    }
}