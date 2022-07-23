package com.ola.recoverunsold.ui.screens.shared

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberScaffoldState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.DateTimePicker
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.app.SubtitleWithIcon
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.location.LocationItem
import com.ola.recoverunsold.ui.components.product.ProductItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.OfferDetailsViewModel
import com.ola.recoverunsold.ui.screens.viewmodels.OfferDetailsViewModelFactory
import com.ola.recoverunsold.utils.misc.addSeconds
import com.ola.recoverunsold.utils.misc.formatDateTime
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros
import com.ola.recoverunsold.utils.misc.jsonSerialize
import com.ola.recoverunsold.utils.misc.openMapWithCoordinates
import com.ola.recoverunsold.utils.misc.remove
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OfferDetailsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    offerId: String,
    offerDetailsViewModel: OfferDetailsViewModel = viewModel(
        factory = OfferDetailsViewModelFactory(offerId)
    )
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed),
        snackbarHostState = snackbarHostState
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                canGoBack = true,
                navController = navController,
                title = stringResource(id = R.string.offer_details)
            )
        },
        drawerContent = DrawerContent(navController, snackbarHostState),
        floatingActionButton = {
            if (offerDetailsViewModel.isDistributor) {
                FloatingActionButton(onClick = {
                    navController.navigate(
                        Routes.OfferProduct.path
                            .replace("{offerId}", offerId)
                            .remove("{product}")
                    )
                }) {
                    if (offerDetailsViewModel.isDistributor) {
                        Icon(
                            Icons.Default.PlaylistAdd,
                            contentDescription = stringResource(id = R.string.add)
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.size(0.dp))
            }
        },
        sheetContent = {
            OrderForm(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(30.dp),
                withdrawalDate = offerDetailsViewModel.withdrawalDate,
                onWithdrawalDateChange = { offerDetailsViewModel.withdrawalDate = it },
                onSubmit = {
                    offerDetailsViewModel.orderProduct()
                },
                loading = offerDetailsViewModel.orderApiCallResult.status == ApiStatus.LOADING
            )

            if (offerDetailsViewModel.orderApiCallResult.status == ApiStatus.SUCCESS) {
                coroutineScope.launch {
                    snackbarHostState.show(
                        Strings.get(R.string.order_successfully_placed)
                    )
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }

            if (offerDetailsViewModel.orderErrorMessage() != null) {
                coroutineScope.launch {
                    snackbarHostState.show(
                        offerDetailsViewModel.orderErrorMessage()!!
                    )
                    bottomSheetScaffoldState.bottomSheetState.collapse()
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetElevation = 25.dp,
        sheetGesturesEnabled = true,
        sheetShape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp)
    ) { paddingValues ->
        when (offerDetailsViewModel.offerApiCallResult.status) {
            ApiStatus.LOADING -> LoadingIndicator()
            ApiStatus.ERROR -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = { offerDetailsViewModel.getOffer() },
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(stringResource(id = R.string.retry))
                    }
                }
                LaunchedEffect(snackbarHostState) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = offerDetailsViewModel.errorMessage()
                                ?: Strings.get(R.string.unknown_error_occured)
                        )
                    }
                }
            }
            else -> {
                val offer = offerDetailsViewModel.offerApiCallResult.data!!
                val width = LocalConfiguration.current.screenWidthDp

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 15.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    SubtitleWithIcon(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(id = R.string.offer_details),
                        imageVector = Icons.Default.Info
                    )

                    Text(
                        text = stringResource(
                            R.string.total_amount,
                            offer.price.formatWithoutTrailingZeros()
                        ),
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    if (offer.beneficiaries != null) {
                        Text(
                            text = stringResource(
                                R.string.offer_beneficiaries_data,
                                offer.beneficiaries
                            ),
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }

                    Text(
                        text = stringResource(
                            R.string.start_date_time,
                            offer.startDate.formatDateTime()
                        ),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )

                    Text(
                        text = stringResource(
                            R.string.end_date_time,
                            offer.startDate.addSeconds(offer.duration).formatDateTime()
                        ),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )

                    if (!offer.products.isNullOrEmpty()) {
                        SubtitleWithIcon(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 20.dp, bottom = 10.dp),
                            text = stringResource(id = R.string.products_label),
                            imageVector = Icons.Default.ShoppingBag
                        )

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            items(items = offer.products) {
                                ProductItem(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .width((width * 0.6).dp),
                                    product = it,
                                    isEditable = offer.distributorId == offerDetailsViewModel.currentUserId,
                                    onEdit = {
                                        navController.navigate(
                                            Routes.OfferProduct.path
                                                .replace("{offerId}", offerId)
                                                .replace("{product}", it.jsonSerialize())
                                        )
                                    },
                                    onDelete = {
                                        offerDetailsViewModel.deleteProduct(
                                            product = it,
                                            onSuccess = {
                                                coroutineScope.launch {
                                                    snackbarHostState.show(
                                                        message = Strings.get(R.string.product_deleted_successfully)
                                                    )
                                                }
                                                offerDetailsViewModel.getOffer()
                                            },
                                            onError = {
                                                coroutineScope.launch {
                                                    snackbarHostState.show(
                                                        message = Strings.get(R.string.product_deletion_failed)
                                                    )
                                                }
                                            }
                                        )
                                    }
                                )
                            }
                        }
                    }

                    if (offer.location != null) {
                        SubtitleWithIcon(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 20.dp, bottom = 10.dp),
                            text = stringResource(id = R.string.pick_up_point),
                            imageVector = Icons.Default.Place
                        )

                        LocationItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                            location = offer.location,
                            isModifiable = false
                        )

                        val context = LocalContext.current
                        Button(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85F)
                                .padding(top = 15.dp),
                            onClick = {
                                context.openMapWithCoordinates(
                                    latitude = offer.location.coordinates.latitude,
                                    longitude = offer.location.coordinates.longitude
                                )
                            }
                        ) {
                            Text(stringResource(id = R.string.view_location_on_maps))
                        }
                    }

                    if (offerDetailsViewModel.isCustomer) {
                        Button(modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(0.85F)
                            .padding(bottom = 35.dp),
                            onClick = {
                                coroutineScope.launch {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                }
                            }) {
                            Text(stringResource(id = R.string.order))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderForm(
    modifier: Modifier = Modifier,
    withdrawalDate: Date,
    onWithdrawalDateChange: (Date) -> Unit,
    onSubmit: () -> Unit,
    loading: Boolean
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        CustomTextInput(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            value = withdrawalDate.formatDateTime(),
            readOnly = true,
            enabled = false,
            onValueChange = {},
            label = { Text(text = stringResource(R.string.withdrawal_date)) },
            trailingIcon = { Icon(Icons.Default.EditCalendar, contentDescription = null) }
        )

        Button(modifier = Modifier.fillMaxWidth(), onClick = onSubmit, enabled = !loading) {
            if (loading) {
                CircularProgressIndicator(color = MaterialTheme.colors.background)
            } else {
                Text(stringResource(id = R.string.confirm_order))
            }
        }
    }

    if (showDatePicker) {
        DateTimePicker(
            onDateUpdate = {
                onWithdrawalDateChange(it)
                showDatePicker = false
            },
            date = withdrawalDate
        )
    }
}