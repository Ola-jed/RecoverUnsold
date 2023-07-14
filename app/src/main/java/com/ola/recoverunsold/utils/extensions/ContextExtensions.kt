package com.ola.recoverunsold.utils.extensions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.ola.recoverunsold.api.core.ApiClient
import com.ola.recoverunsold.api.services.FcmTokenService
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import okhttp3.Cache

private const val cacheSize = 5242880L

/**
 * Use the Android context to get the device location
 */
fun Context.getDeviceLocation(
    onLatLngValueUpdate: (LatLng) -> Unit,
    onLocationFetchFailed: () -> Unit
) {
    val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    val locationChangeListener = fun(location: Location?) {
        if (location != null) {
            onLatLngValueUpdate(LatLng(location.latitude, location.longitude))
        } else {
            onLocationFetchFailed()
        }
    }

    if (ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    fusedLocationProviderClient.getCurrentLocation(
        Priority.PRIORITY_BALANCED_POWER_ACCURACY,
        null
    ).addOnSuccessListener(locationChangeListener)
}

/**
 * Open a map with the given lat long
 */
fun Context.openMapWithCoordinates(latitude: Double, longitude: Double) {
    val mapsIntent = Intent(Intent.ACTION_VIEW)
    mapsIntent.data = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude")
    ContextCompat.startActivity(this, mapsIntent, null)
}

/**
 * Returns if the current Context has network access
 */
fun Context.hasNetwork(): Boolean {
    val connectivityManager = this.getSystemService<ConnectivityManager>()
    val capabilities = connectivityManager
        ?.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
        }
    }
    return false
}

/**
 * Create an http cache for okhttp from the context
 */
fun Context.httpCache(): Cache = Cache(this.cacheDir, cacheSize)

/**
 * Logout the authenticated user from the app
 */
suspend fun Context.logout() {
    val token = TokenStore.get()
    if (token != null) {
        try {
            val fcmTokenService: FcmTokenService = ApiClient.buildService()
            fcmTokenService.deleteAllFcmTokens()
        } catch (e: Exception) {
            // Nothing because we should not hinder the user experience
        }
        TokenStore(this).removeToken()
    }
    UserObserver.remove()
}