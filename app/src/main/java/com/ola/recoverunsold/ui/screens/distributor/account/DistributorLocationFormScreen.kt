package com.ola.recoverunsold.ui.screens.distributor.account

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.ola.recoverunsold.api.services.wrappers.LocationServiceWrapper
import com.ola.recoverunsold.models.LatLong
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.ui.components.AppBar
import com.ola.recoverunsold.ui.components.ImagePicker
import com.ola.recoverunsold.ui.components.LocationMap
import com.ola.recoverunsold.utils.misc.toCoordinates
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ImagePicker(
                modifier = Modifier.fillMaxWidth(),
                onImagePicked = { distributorLocationFormViewModel.imageUrl = it }
            )

            LocationMap(
                modifier = Modifier
                    .size(
                        height = (LocalConfiguration.current.screenHeightDp * 0.5).dp,
                        width = (LocalConfiguration.current.screenHeightDp * 0.7).dp
                    )
                    .clip(RoundedCornerShape(10.dp)),
                latLng = if (location != null) {
                    LatLng(location.coordinates.latitude, location.coordinates.longitude)
                } else {
                    null
                },
                onLatLngUpdate = { distributorLocationFormViewModel.latLong = it.toCoordinates() }
            )
        }
    }
}

class DistributorLocationFormViewModel(
    private val locationServiceWrapper: LocationServiceWrapper = get(LocationServiceWrapper::class.java),
) : ViewModel() {
    var imageUrl by mutableStateOf<Uri?>(null)
    var latLong by mutableStateOf(LatLong.zero())

    fun create() {
        // TODO
    }

    fun update() {
        // TODO
    }
}