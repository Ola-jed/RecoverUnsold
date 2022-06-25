package com.ola.recoverunsold.ui.screens.distributor.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.ola.recoverunsold.api.services.wrappers.LocationServiceWrapper
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.ui.components.AppBar
import com.ola.recoverunsold.ui.components.ImagePicker
import org.koin.java.KoinJavaComponent.get

@Composable
fun DistributorLocationFormScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    serializedLocation: String? = null,
    distributorLocationFormViewModel: DistributorLocationFormViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val location = if (serializedLocation == null) {
        null
    } else {
        Gson().fromJson(serializedLocation, Location::class.java)
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                canGoBack = true,
                navController = navController
            )
        },
        drawerGesturesEnabled = false
    ) {

        Column() {
            ImagePicker()
        }
//        val latLng = if (location != null) {
//            LatLng(location.coordinates.latitude, location.coordinates.longitude)
//        } else {
//            LatLng(1.35, 103.87)
//        }
//
//        val cameraPositionState = rememberCameraPositionState {
//            position = CameraPosition.fromLatLngZoom(latLng, 10f)
//        }
//
//        GoogleMap(
//            modifier = Modifier.fillMaxSize(),
//            cameraPositionState = cameraPositionState
//        ) {
//            Marker(
//                title = "Singapore",
//                snippet = "Marker in Singapore",
//                position = LatLng(0.0, 0.0)
//            )
//        }
    }
}

class DistributorLocationFormViewModel(
    private val locationServiceWrapper: LocationServiceWrapper = get(LocationServiceWrapper::class.java),
) : ViewModel() {

    fun create() {
        // TODO
    }

    fun update() {
        // TODO
    }
}