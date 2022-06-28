package com.ola.recoverunsold.ui.components

import android.Manifest
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

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
            getDeviceLocation(context, {
                latLngData = it.also { Log.i("Location", "Updated with  $it") }
                onLatLngUpdate(it)
            }, { onLocationFetchFailed() })
        }
        else -> {}
    }

    GoogleMap(
        modifier = modifier.fillMaxWidth(),
        cameraPositionState = rememberCameraPositionState {
            (CameraPosition.fromLatLngZoom(
                latLngData,
                25f
            ))
        },
        onMapLongClick = {
            latLngData = it
            onLatLngUpdate(it)
        }
    ) {
        Marker(
            state = MarkerState(position = latLngData)
        )
    }
}

private fun getDeviceLocation(
    context: Context,
    onLatLngValueUpdate: (LatLng) -> Unit,
    onLocationFetchFailed: () -> Unit
) {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    val locationChangeListener = fun(location: Location?) {
        if (location != null) {
            onLatLngValueUpdate(
                LatLng(
                    location.latitude,
                    location.longitude
                )
            )
        } else {
            onLocationFetchFailed()
        }
    }
    fusedLocationProviderClient.lastLocation.addOnSuccessListener(locationChangeListener)
    fusedLocationProviderClient.getCurrentLocation(
        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
        null
    ).addOnSuccessListener(locationChangeListener)
}