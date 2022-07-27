package com.ola.recoverunsold.ui.screens.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.query.OfferFilterQuery
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.distributor.DistributorInformationComponent
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.offer.OfferFilterComponent
import com.ola.recoverunsold.ui.components.offer.OfferItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorDetailsViewModel
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorDetailsViewModelFactory
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

@Composable
fun DistributorDetailsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    distributorId: String,
    distributorDetailsViewModel: DistributorDetailsViewModel = viewModel(
        factory = DistributorDetailsViewModelFactory(distributorId)
    )
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                title = stringResource(id = R.string.distributor_information)
            )
        },
        drawerContent = DrawerContent(navController, snackbarHostState)
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (distributorDetailsViewModel.distributorApiCallResult.status) {
                ApiStatus.LOADING -> LoadingIndicator()
                ApiStatus.ERROR -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Button(
                            onClick = {
                                distributorDetailsViewModel.offerFilterQuery = OfferFilterQuery()
                                distributorDetailsViewModel.getDistributorInformation()
                            },
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(stringResource(id = R.string.retry))
                        }
                    }

                    LaunchedEffect(snackbarHostState) {
                        coroutineScope.launch {
                            snackbarHostState.show(
                                message = distributorDetailsViewModel.errorMessage()
                                    ?: Strings.get(R.string.unknown_error_occured)
                            )
                        }
                    }
                }
                else -> {
                    val distributor = distributorDetailsViewModel.distributorApiCallResult.data!!

                    DistributorInformationComponent(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 20.dp),
                        distributorInformation = distributor
                    )
                }
            }

            when (distributorDetailsViewModel.offersApiCallResult.status) {
                ApiStatus.LOADING -> {}
                ApiStatus.ERROR -> {
                    LaunchedEffect(snackbarHostState) {
                        coroutineScope.launch {
                            snackbarHostState.show(
                                message = Strings.get(R.string.error_occured_while_fetching_offers)
                            )
                        }
                    }
                }
                else -> {
                    val offers = distributorDetailsViewModel.offersApiCallResult.data!!

                    LazyColumn {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start
                            ) {
                                OfferFilterComponent(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    minPrice = distributorDetailsViewModel.offerFilterQuery.minPrice,
                                    maxPrice = distributorDetailsViewModel.offerFilterQuery.maxPrice,
                                    minDate = distributorDetailsViewModel.offerFilterQuery.minDate,
                                    maxDate = distributorDetailsViewModel.offerFilterQuery.maxDate,
                                    active = distributorDetailsViewModel.offerFilterQuery.active,
                                    onMinPriceChange = {
                                        distributorDetailsViewModel.offerFilterQuery =
                                            distributorDetailsViewModel.offerFilterQuery.copy(
                                                minPrice = it
                                            )
                                    },
                                    onMaxPriceChange = {
                                        distributorDetailsViewModel.offerFilterQuery =
                                            distributorDetailsViewModel.offerFilterQuery.copy(
                                                maxPrice = it
                                            )
                                    },
                                    onMinDateChange = {
                                        distributorDetailsViewModel.offerFilterQuery =
                                            distributorDetailsViewModel.offerFilterQuery.copy(
                                                minDate = it
                                            )
                                    },
                                    onMaxDateChange = {
                                        distributorDetailsViewModel.offerFilterQuery =
                                            distributorDetailsViewModel.offerFilterQuery.copy(
                                                maxDate = it
                                            )
                                    },
                                    onActiveChange = {
                                        distributorDetailsViewModel.offerFilterQuery =
                                            distributorDetailsViewModel.offerFilterQuery.copy(
                                                active = it
                                            )
                                    },
                                    onApply = {
                                        distributorDetailsViewModel.getOffers()
                                    },
                                    onReset = {
                                        distributorDetailsViewModel.offerFilterQuery =
                                            OfferFilterQuery()
                                        distributorDetailsViewModel.getOffers()
                                    }
                                )
                            }
                        }

                        if (offers.items.isEmpty()) {
                            item {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Text(
                                        stringResource(R.string.no_offers_found),
                                        style = MaterialTheme.typography.h6,
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                            }
                        } else {
                            items(items = offers.items) { item ->
                                OfferItem(
                                    modifier = Modifier
                                        .fillParentMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 10.dp),
                                    offer = item,
                                    onTap = {
                                        navController.navigate(
                                            Routes.OfferDetails.path.replace(
                                                "{offerId}",
                                                item.id
                                            )
                                        )
                                    }
                                )
                            }

                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 20.dp, end = 20.dp, top = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    if (offers.pageNumber > 1) {
                                        Button(onClick = { distributorDetailsViewModel.getPrevious() }) {
                                            Text(stringResource(id = R.string.previous))
                                        }
                                    }
                                    if (offers.hasNext) {
                                        Button(onClick = { distributorDetailsViewModel.getNext() }) {
                                            Text(stringResource(id = R.string.next))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}