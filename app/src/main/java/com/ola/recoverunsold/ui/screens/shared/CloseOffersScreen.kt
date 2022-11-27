package com.ola.recoverunsold.ui.screens.shared

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.models.LatLong
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.app.NoContentComponent
import com.ola.recoverunsold.ui.components.app.PaginationComponent
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.offer.OfferRelativeDistanceItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.CloseOffersViewModel
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros
import com.ola.recoverunsold.utils.misc.getDeviceLocation
import com.ola.recoverunsold.utils.misc.openMapWithCoordinates
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.misc.toSecureInt
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.IntegerValidator
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterialApi::class)
@Composable
fun CloseOffersScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    closeOffersViewModel: CloseOffersViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed),
        snackbarHostState = snackbarHostState
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = bottomSheetScaffoldState,
                title = stringResource(id = R.string.close_offers),
                canGoBack = true,
                navController = navController
            )
        },
        drawerContent = DrawerContent(navController),
        sheetContent = {
            CloseOffersFormContent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                distance = closeOffersViewModel.offerDistanceFilterQuery.distance,
                onDistanceUpdate = {
                    closeOffersViewModel.offerDistanceFilterQuery =
                        closeOffersViewModel
                            .offerDistanceFilterQuery
                            .copy(distance = it)
                },
                onDistanceValidationError = {
                    closeOffersViewModel.formState =
                        closeOffersViewModel.formState.copy(
                            isValid = false,
                            errorMessage = it
                        )
                },
                onDistanceValidationSuccess = {
                    closeOffersViewModel.formState =
                        closeOffersViewModel.formState.copy(
                            isValid = true,
                            errorMessage = null
                        )
                },
                onSubmit = {
                    if (!closeOffersViewModel.formState.isValid) {
                        coroutineScope.launch {
                            snackbarHostState.show(
                                message = closeOffersViewModel.formState.errorMessage
                                    ?: Strings.get(R.string.invalid_data)
                            )
                        }
                    } else {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                        closeOffersViewModel.getCloseOffers()
                    }
                }
            )
        },
        sheetPeekHeight = 0.dp,
        sheetElevation = 25.dp,
        sheetGesturesEnabled = true,
        sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                coroutineScope.launch {
                    if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    } else {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            }) {
                Icon(imageVector = Icons.Default.NearMe, contentDescription = null)
            }
        }
    ) { paddingValues ->
        when (closeOffersViewModel.closeOffersApiResult.status) {
            ApiStatus.INACTIVE -> {
                Text(
                    text = stringResource(id = R.string.enter_search_department),
                    modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp),
                    fontSize = 19.sp
                )

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
                                closeOffersViewModel.offerDistanceFilterQuery = closeOffersViewModel
                                    .offerDistanceFilterQuery
                                    .copy(latLong = LatLong(it.latitude, it.longitude))
                            },
                            onLocationFetchFailed = {
                                coroutineScope.launch {
                                    snackbarHostState.show(
                                        message = Strings.get(R.string.location_fetch_failed)
                                    )
                                }
                            }
                        )
                    }
                    else -> {
                        coroutineScope.launch {
                            snackbarHostState.show(
                                message = Strings.get(R.string.feature_requiring_permission)
                            )
                        }
                    }
                }
            }
            ApiStatus.LOADING -> LoadingIndicator()
            ApiStatus.ERROR -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            closeOffersViewModel.errorMessage()
                                ?: stringResource(id = R.string.unknown_error_occured),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        Button(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            onClick = { closeOffersViewModel.getCloseOffers() }) {
                            Text(text = stringResource(id = R.string.retry))
                        }
                    }
                }
            }
            ApiStatus.SUCCESS -> {
                val offers = closeOffersViewModel.closeOffersApiResult.data!!

                if (offers.items.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        NoContentComponent(
                            modifier = Modifier.fillMaxWidth(),
                            message = stringResource(R.string.no_offers_found)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    ) {
                        items(items = offers.items) { item ->
                            OfferRelativeDistanceItem(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                offerWithRelativeDistance = item,
                                onMoreInformationRequest = {
                                    navController.navigate(
                                        Routes.OfferDetails.path.replace(
                                            "{offerId}",
                                            item.offer.id
                                        )
                                    )
                                },
                                onMapShowRequest = {
                                    context.openMapWithCoordinates(
                                        latitude = item.offer.location!!.coordinates.latitude,
                                        longitude = item.offer.location.coordinates.longitude
                                    )
                                }
                            )
                        }

                        item {
                            PaginationComponent(
                                modifier = Modifier.fillMaxWidth(),
                                page = offers,
                                onPrevious = { closeOffersViewModel.getPrevious() },
                                onNext = { closeOffersViewModel.getNext() }
                            )
                        }

                        item {
                            Box(modifier = Modifier.height(75.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CloseOffersFormContent(
    modifier: Modifier = Modifier,
    distance: Double,
    onDistanceUpdate: (Double) -> Unit,
    onDistanceValidationError: (String) -> Unit,
    onDistanceValidationSuccess: () -> Unit,
    onSubmit: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val modifierToApply = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)

        CustomTextInput(
            modifier = modifierToApply,
            value = distance.formatWithoutTrailingZeros(),
            onValueChange = { onDistanceUpdate(it.toSecureInt().toDouble()) },
            label = { Text(text = stringResource(R.string.distance)) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
            validator = IntegerValidator(),
            onValidationError = onDistanceValidationError,
            onValidationSuccess = onDistanceValidationSuccess
        )

        Button(
            onClick = onSubmit,
            modifier = modifierToApply.padding(bottom = 10.dp)
        ) {
            Text(text = stringResource(id = R.string.search))
        }
    }
}