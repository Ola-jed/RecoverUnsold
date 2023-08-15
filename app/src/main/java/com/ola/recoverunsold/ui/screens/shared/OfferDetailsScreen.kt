package com.ola.recoverunsold.ui.screens.shared

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.MainActivity
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.DateTimePicker
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.app.SubtitleWithIcon
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.location.LocationItem
import com.ola.recoverunsold.ui.components.offer.OfferDetailsComponent
import com.ola.recoverunsold.ui.components.offer.OfferStatusBadge
import com.ola.recoverunsold.ui.components.product.ProductItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.OfferDetailsViewModel
import com.ola.recoverunsold.utils.extensions.addSeconds
import com.ola.recoverunsold.utils.extensions.formatDateTime
import com.ola.recoverunsold.utils.extensions.jsonSerialize
import com.ola.recoverunsold.utils.extensions.openMapWithCoordinates
import com.ola.recoverunsold.utils.extensions.remove
import com.ola.recoverunsold.utils.extensions.show
import com.ola.recoverunsold.utils.resources.Strings
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfferDetailsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    offerId: String,
    offerDetailsViewModel: OfferDetailsViewModel = offerDetailsViewModel(offerId)
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(skipPartiallyExpanded = true),
        snackbarHostState = snackbarHostState
    )
    var showWithdrawalDatePicker by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController) },
        content = {
            BottomSheetScaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                scaffoldState = bottomSheetScaffoldState,
                topBar = {
                    AppBar(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        canGoBack = true,
                        navController = navController,
                        title = stringResource(id = R.string.offer_details),
                        actions = {
                            IconButton(onClick = {
                                navController.navigate(
                                    Routes.OfferProduct.path
                                        .replace("{offerId}", offerId)
                                        .remove("{product}")
                                )
                            }) {
                                Icon(
                                    Icons.Default.PlaylistAdd,
                                    contentDescription = stringResource(id = R.string.add_a_product)
                                )
                            }
                        }
                    )
                },
                sheetContent = {
                    val offer = offerDetailsViewModel.offerApiCallResult.data

                    OrderForm(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(30.dp),
                        withdrawalDate = offerDetailsViewModel.withdrawalDate,
                        minWithdrawalDate = offerDetailsViewModel.maxOf(offer?.startDate, Date()),
                        maxWithdrawalDate = offer?.startDate?.addSeconds(offer.duration),
                        onWithdrawalDateChange = { offerDetailsViewModel.withdrawalDate = it },
                        onSubmit = { offerDetailsViewModel.orderProduct() },
                        loading = offerDetailsViewModel.orderApiCallResult.status == ApiStatus.LOADING,
                        showDatePicker = showWithdrawalDatePicker,
                        onDatePickerHide = { showWithdrawalDatePicker = false },
                        onDatePickerShow = { showWithdrawalDatePicker = true }
                    )

                    if (offerDetailsViewModel.orderApiCallResult.status == ApiStatus.SUCCESS) {
                        LaunchedEffect(coroutineScope) {
                            snackbarHostState.show(Strings.get(R.string.order_successfully_placed))
                            bottomSheetScaffoldState.bottomSheetState.hide()
                            navController.navigate(Routes.Orders.path)
                        }
                    }

                    if (offerDetailsViewModel.orderErrorMessage() != null) {
                        LaunchedEffect(coroutineScope) {
                            snackbarHostState.show(offerDetailsViewModel.orderErrorMessage()!!)
                            bottomSheetScaffoldState.bottomSheetState.hide()
                        }
                    }
                },
                sheetPeekHeight = 0.dp,
                sheetTonalElevation = 25.dp,
                sheetShadowElevation = 25.dp,
                sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
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

                        LazyColumn(
                            modifier = Modifier
                                .padding(paddingValues)
                                .padding(horizontal = 15.dp),
                            state = listState
                        ) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    SubtitleWithIcon(
                                        text = stringResource(id = R.string.offer_details),
                                        imageVector = Icons.Default.Info
                                    )

                                    OfferStatusBadge(offer = offer)
                                }
                            }

                            item {
                                OfferDetailsComponent(offer = offer)
                            }

                            if (!offer.products.isNullOrEmpty()) {
                                item {
                                    SubtitleWithIcon(
                                        modifier = Modifier.padding(top = 20.dp, bottom = 10.dp),
                                        text = stringResource(id = R.string.products_label),
                                        imageVector = Icons.Default.ShoppingBag
                                    )
                                }

                                item {
                                    if (offer.products.count() == 1) {
                                        val product = offer.products.first()
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            ProductItem(
                                                modifier = Modifier
                                                    .width((width * 0.6).dp),
                                                product = product,
                                                isEditable = offer.distributorId == offerDetailsViewModel.currentUserId,
                                                onEdit = {
                                                    navController.navigate(
                                                        Routes.OfferProduct.path
                                                            .replace("{offerId}", offerId)
                                                            .replace(
                                                                "{product}",
                                                                product.jsonSerialize()
                                                            )
                                                    )
                                                },
                                                onDelete = {
                                                    offerDetailsViewModel.deleteProduct(
                                                        product = product,
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
                                    } else {
                                        LazyRow(modifier = Modifier.fillMaxWidth()) {
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
                                                                .replace(
                                                                    "{product}",
                                                                    it.jsonSerialize()
                                                                )
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
                                }
                            }

                            if (offer.location != null) {
                                item {
                                    SubtitleWithIcon(
                                        text = stringResource(id = R.string.pick_up_point),
                                        imageVector = Icons.Default.Place
                                    )
                                }

                                item {
                                    LocationItem(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 6.dp, vertical = 10.dp),
                                        location = offer.location,
                                        isModifiable = false,
                                        onClick = {
                                            context.openMapWithCoordinates(
                                                latitude = offer.location.coordinates.latitude,
                                                longitude = offer.location.coordinates.longitude
                                            )
                                        }
                                    )
                                }
                            }

                            if (offerDetailsViewModel.isCustomer && offer.startDate.addSeconds(offer.duration)
                                    .after(Date())
                            ) {
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Button(
                                            modifier = Modifier
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

                            item {
                                Box(modifier = Modifier.height(75.dp))
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun OrderForm(
    modifier: Modifier = Modifier,
    withdrawalDate: Date,
    minWithdrawalDate: Date,
    maxWithdrawalDate: Date?,
    onWithdrawalDateChange: (Date) -> Unit,
    onSubmit: () -> Unit,
    loading: Boolean,
    showDatePicker: Boolean,
    onDatePickerShow: () -> Unit,
    onDatePickerHide: () -> Unit
) {
    Column(modifier = modifier) {
        CustomTextInput(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDatePickerShow() },
            value = withdrawalDate.formatDateTime(),
            readOnly = true,
            enabled = false,
            onValueChange = {},
            label = { Text(text = stringResource(R.string.withdrawal_date)) },
            trailingIcon = { Icon(Icons.Default.EditCalendar, contentDescription = null) }
        )

        Button(modifier = Modifier.fillMaxWidth(), onClick = onSubmit, enabled = !loading) {
            if (loading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
            } else {
                Text(stringResource(id = R.string.confirm_order))
            }
        }
    }

    if (showDatePicker) {
        DateTimePicker(
            onDateUpdate = {
                onWithdrawalDateChange(it)
                onDatePickerHide()
            },
            date = withdrawalDate,
            minDate = minWithdrawalDate,
            maxDate = maxWithdrawalDate
        )
    }
}

@Composable
fun offerDetailsViewModel(offerId: String): OfferDetailsViewModel {
    val factory = EntryPointAccessors
        .fromActivity<MainActivity.ViewModelFactoryProvider>(LocalContext.current as Activity)
        .offerDetailsViewModelFactory()
    return viewModel(factory = OfferDetailsViewModel.provideFactory(factory, offerId))
}