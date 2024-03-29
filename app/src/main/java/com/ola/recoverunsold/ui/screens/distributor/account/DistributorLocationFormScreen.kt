package com.ola.recoverunsold.ui.screens.distributor.account

import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.LatLng
import com.ola.recoverunsold.MainActivity
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.ImagePicker
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.location.LocationMap
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorLocationFormViewModel
import com.ola.recoverunsold.utils.extensions.jsonDeserialize
import com.ola.recoverunsold.utils.extensions.show
import com.ola.recoverunsold.utils.extensions.toCoordinates
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DistributorLocationFormScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    serializedLocation: String? = null,
    distributorLocationFormViewModel: DistributorLocationFormViewModel = distributorLocationFormViewModel(
        serializedLocation.jsonDeserialize<Location>()
    )
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val location = serializedLocation.jsonDeserialize<Location>()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController) },
        gesturesEnabled = false,
        content = {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    AppBar(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        canGoBack = true,
                        navController = navController,
                        title = stringResource(
                            id = if (serializedLocation.isNullOrBlank()) {
                                R.string.create_a_location
                            } else {
                                R.string.update_location
                            }
                        )
                    )
                }
            ) { paddingValues ->
                DistributorLocationFormScreenContent(
                    modifier = Modifier.padding(paddingValues),
                    name = distributorLocationFormViewModel.name,
                    indication = distributorLocationFormViewModel.indication,
                    location = location,
                    imageUri = distributorLocationFormViewModel.imageUri,
                    loading = distributorLocationFormViewModel.apiCallResult.status == ApiStatus.LOADING,
                    errorMessage = distributorLocationFormViewModel.errorMessage(),
                    isSuccessful = distributorLocationFormViewModel.apiCallResult.status == ApiStatus.SUCCESS,
                    onImagePicked = { distributorLocationFormViewModel.imageUri = it },
                    onNameChange = { distributorLocationFormViewModel.name = it },
                    onIndicationChange = { distributorLocationFormViewModel.indication = it },
                    onLatLngUpdate = {
                        distributorLocationFormViewModel.latLong = it.toCoordinates()
                    },
                    onSubmit = {
                        if (!distributorLocationFormViewModel.formState.isValid) {
                            coroutineScope.launch {
                                snackbarHostState.show(
                                    message = distributorLocationFormViewModel.formState.errorMessage
                                        ?: Strings.get(R.string.invalid_data)
                                )
                            }
                        } else {
                            if (location == null) {
                                distributorLocationFormViewModel.create(context)
                            } else {
                                distributorLocationFormViewModel.update(context)
                            }
                        }
                    },
                    snackbarHostState = snackbarHostState,
                    coroutineScope = coroutineScope,
                    navController = navController,
                    onValidationSuccess = {
                        distributorLocationFormViewModel.formState =
                            distributorLocationFormViewModel.formState.copy(
                                isValid = true,
                                errorMessage = null
                            )
                    },
                    onValidationError = {
                        distributorLocationFormViewModel.formState =
                            distributorLocationFormViewModel.formState.copy(
                                isValid = false,
                                errorMessage = it
                            )
                    }
                )
            }
        }
    )
}

@Composable
fun DistributorLocationFormScreenContent(
    modifier: Modifier = Modifier,
    name: String,
    indication: String,
    location: Location? = null,
    imageUri: Uri? = null,
    loading: Boolean,
    isSuccessful: Boolean,
    errorMessage: String?,
    onImagePicked: (Uri) -> Unit,
    onNameChange: (String) -> Unit,
    onIndicationChange: (String) -> Unit,
    onLatLngUpdate: (LatLng) -> Unit,
    onSubmit: () -> Unit,
    snackbarHostState: SnackbarHostState,
    coroutineScope: CoroutineScope,
    navController: NavController,
    onValidationError: (String) -> Unit,
    onValidationSuccess: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    val maxIndex = 2

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .padding(top = 25.dp, bottom = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val focusManager = LocalFocusManager.current
        val fieldsModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)

        when (currentIndex) {
            0 -> Column(modifier = Modifier.padding(top = 25.dp)) {
                CustomTextInput(
                    modifier = fieldsModifier,
                    value = name,
                    leadingIcon = { Icon(Icons.Filled.TextFields, contentDescription = null) },
                    placeholder = { Text(text = stringResource(R.string.name)) },
                    label = { Text(text = stringResource(R.string.name)) },
                    onValueChange = onNameChange,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Ascii
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    validator = IsRequiredValidator(),
                    onValidationSuccess = onValidationSuccess,
                    onValidationError = onValidationError
                )

                CustomTextInput(
                    modifier = fieldsModifier,
                    value = indication,
                    leadingIcon = { Icon(Icons.Filled.TextFields, contentDescription = null) },
                    placeholder = { Text(text = stringResource(R.string.indication)) },
                    label = { Text(text = stringResource(R.string.indication)) },
                    onValueChange = onIndicationChange,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    singleLine = false,
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    canBeEmpty = true
                )
            }

            1 -> ImagePicker(
                modifier = fieldsModifier
                    .height((LocalConfiguration.current.screenHeightDp * 0.75).dp),
                imageUri = imageUri,
                onImagePicked = onImagePicked
            )

            2 -> Column(modifier = Modifier.padding(top = 3.dp)) {
                Text(
                    stringResource(id = R.string.choose_location_on_map),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 15.dp, start = 10.dp, end = 10.dp),
                    style = MaterialTheme.typography.bodyMedium
                )

                LocationMap(
                    modifier = Modifier.size(
                        height = (LocalConfiguration.current.screenHeightDp * 0.7).dp,
                        width = (LocalConfiguration.current.screenHeightDp * 0.7).dp
                    ),
                    latLng = if (location != null) {
                        LatLng(location.coordinates.latitude, location.coordinates.longitude)
                    } else {
                        null
                    },
                    onLatLngUpdate = onLatLngUpdate,
                    onLocationFetchFailed = {
                        coroutineScope.launch {
                            snackbarHostState.show(
                                message = Strings.get(R.string.location_fetch_failed)
                            )
                        }
                    }
                )
            }
        }

        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 15.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (currentIndex > 0) {
                Button(onClick = {
                    currentIndex--
                }) {
                    Text(text = stringResource(id = R.string.previous))
                }
            }

            if (currentIndex < maxIndex) {
                Button(onClick = {
                    if (name.isBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.show(Strings.get(R.string.name_field_required_message))
                        }
                    } else {
                        currentIndex++
                    }
                }) {
                    Text(text = stringResource(id = R.string.next))
                }
            }

            if (currentIndex == maxIndex) {
                Button(onClick = onSubmit, enabled = !loading) {
                    if (loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.background
                        )
                    } else {
                        Text(text = stringResource(R.string.submit))
                    }
                }
            }
        }

        if (errorMessage != null) {
            LaunchedEffect(snackbarHostState) {
                coroutineScope.launch { snackbarHostState.show(message = errorMessage) }
            }
        }

        if (isSuccessful) {
            LaunchedEffect(snackbarHostState) {
                navController.navigate(Routes.DistributorAccount.path)
                coroutineScope.launch {
                    snackbarHostState.show(message = Strings.get(R.string.location_published_successfully))
                }
            }
        }
    }
}

@Composable
fun distributorLocationFormViewModel(location: Location?): DistributorLocationFormViewModel {
    val factory = EntryPointAccessors
        .fromActivity<MainActivity.ViewModelFactoryProvider>(LocalContext.current as Activity)
        .distributorLocationFormViewModelFactory()
    return viewModel(factory = DistributorLocationFormViewModel.provideFactory(factory, location))
}