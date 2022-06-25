package com.ola.recoverunsold.ui.components

import android.content.Context
import android.location.Geocoder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.ola.recoverunsold.R
import com.ola.recoverunsold.utils.resources.Strings
import java.lang.StringBuilder
import java.util.Locale

@Composable
fun LocationMap(
    modifier: Modifier = Modifier,
    latLng: LatLng? = null,
    onLatLngUpdate: (LatLng) -> Unit
) {
    // TODO : if latlng is null get current position else 0.0
    var latLngData by mutableStateOf(latLng ?: LatLng(0.0, 0.0))
    val addressData = getAddress(latLngData, LocalContext.current)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLngData, 10f)
    }

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        GoogleMap(
            modifier = Modifier.fillMaxWidth(),
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
        Text(addressData, style = MaterialTheme.typography.subtitle1)
    }
}

private fun getAddress(latLng: LatLng, context: Context): String {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
    if (addresses.isEmpty()) {
        return Strings.get(R.string.n_a)
    }
    val address = addresses.first()
    return StringBuilder(address.countryName)
        .append("\n")
        .appendLine(address.adminArea)
        .appendLine(address.subAdminArea)
        .append(address.locality)
        .toString()
}