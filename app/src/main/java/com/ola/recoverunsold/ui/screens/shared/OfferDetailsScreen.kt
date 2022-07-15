package com.ola.recoverunsold.ui.screens.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlaylistAdd
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import com.ola.recoverunsold.api.responses.TokenRoles
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.app.SubtitleWithIcon
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.location.LocationItem
import com.ola.recoverunsold.ui.components.product.ProductItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.OfferDetailsViewModel
import com.ola.recoverunsold.ui.screens.viewmodels.OfferDetailsViewModelFactory
import com.ola.recoverunsold.utils.misc.formatDateTime
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros
import com.ola.recoverunsold.utils.misc.jsonSerialize
import com.ola.recoverunsold.utils.misc.openMapWithCoordinates
import com.ola.recoverunsold.utils.misc.remove
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import kotlinx.coroutines.launch
import java.util.Date

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
    val connectedAsDistributor = TokenStore.get()?.role == TokenRoles.DISTRIBUTOR

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
        drawerContent = DrawerContent(navController, snackbarHostState),
        floatingActionButton = {
            if (connectedAsDistributor) {
                FloatingActionButton(onClick = {
                    navController.navigate(
                        Routes.OfferProduct.path
                            .replace("{offerId}", offerId)
                            .remove("{product}")
                    )
                }) {
                    Icon(
                        Icons.Default.PlaylistAdd,
                        contentDescription = stringResource(id = R.string.add)
                    )
                }
            }
        }
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
                            Date.from(
                                offer.startDate.toInstant().plusSeconds(offer.duration.toLong())
                            ).formatDateTime()
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

                        if (offerDetailsViewModel.isCustomer) {
                            Button(modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth(0.85F),
                                onClick = { /*TODO*/ }) {
                                Text(stringResource(id = R.string.order))
                            }
                        }
                    }
                }
            }
        }
    }
}