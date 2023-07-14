package com.ola.recoverunsold.ui.screens.distributor.offers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.ExtendableFab
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.app.NoContentComponent
import com.ola.recoverunsold.ui.components.app.PaginationComponent
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.offer.OfferFilterComponent
import com.ola.recoverunsold.ui.components.offer.OfferItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorOffersViewModel
import com.ola.recoverunsold.utils.extensions.isScrollingUp
import com.ola.recoverunsold.utils.extensions.jsonSerialize
import com.ola.recoverunsold.utils.extensions.remove
import com.ola.recoverunsold.utils.extensions.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

@Composable
fun DistributorOffersScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    distributorOffersViewModel: DistributorOffersViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController) },
        content = {
            Scaffold(
                topBar = {
                    AppBar(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        title = stringResource(id = R.string.offers)
                    )
                },
                floatingActionButton = {
                    ExtendableFab(
                        extended = listState.isScrollingUp(),
                        text = { Text(stringResource(id = R.string.add)) },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        onClick = {
                            navController.navigate(Routes.OfferCreateOrUpdate.path.remove("{offer}"))
                        }
                    )
                }
            ) { paddingValues ->
                when (distributorOffersViewModel.offersApiResult.status) {
                    ApiStatus.LOADING -> LoadingIndicator()
                    ApiStatus.ERROR -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Button(
                                onClick = {
                                    distributorOffersViewModel.resetFilter()
                                    distributorOffersViewModel.getOffers()
                                },
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(stringResource(id = R.string.retry))
                            }
                        }

                        LaunchedEffect(snackbarHostState) {
                            coroutineScope.launch {
                                snackbarHostState.show(
                                    message = distributorOffersViewModel.errorMessage()
                                        ?: Strings.get(R.string.unknown_error_occured)
                                )
                            }
                        }
                    }

                    else -> {
                        val offers = distributorOffersViewModel.offersApiResult.data!!

                        LazyColumn(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize(),
                            state = listState
                        ) {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    OfferFilterComponent(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 20.dp, horizontal = 10.dp),
                                        minPrice = distributorOffersViewModel.offerFilterQuery.minPrice,
                                        maxPrice = distributorOffersViewModel.offerFilterQuery.maxPrice,
                                        minDate = distributorOffersViewModel.offerFilterQuery.minDate,
                                        maxDate = distributorOffersViewModel.offerFilterQuery.maxDate,
                                        active = distributorOffersViewModel.offerFilterQuery.active,
                                        onMinPriceChange = {
                                            distributorOffersViewModel.offerFilterQuery =
                                                distributorOffersViewModel.offerFilterQuery.copy(
                                                    minPrice = it
                                                )
                                        },
                                        onMaxPriceChange = {
                                            distributorOffersViewModel.offerFilterQuery =
                                                distributorOffersViewModel.offerFilterQuery.copy(
                                                    maxPrice = it
                                                )
                                        },
                                        onMinDateChange = {
                                            distributorOffersViewModel.offerFilterQuery =
                                                distributorOffersViewModel.offerFilterQuery.copy(
                                                    minDate = it
                                                )
                                        },
                                        onMaxDateChange = {
                                            distributorOffersViewModel.offerFilterQuery =
                                                distributorOffersViewModel.offerFilterQuery.copy(
                                                    maxDate = it
                                                )
                                        },
                                        onActiveChange = {
                                            distributorOffersViewModel.offerFilterQuery =
                                                distributorOffersViewModel.offerFilterQuery.copy(
                                                    active = it
                                                )
                                        },
                                        onApply = {
                                            distributorOffersViewModel.getOffers()
                                        },
                                        onReset = {
                                            distributorOffersViewModel.resetFilter()
                                            distributorOffersViewModel.getOffers()
                                        }
                                    )
                                }
                            }

                            if (offers.items.isEmpty()) {
                                item {
                                    NoContentComponent(
                                        modifier = Modifier.fillMaxWidth(),
                                        message = stringResource(R.string.no_offers_found)
                                    )
                                }
                            } else {
                                items(items = offers.items) { item ->
                                    OfferItem(
                                        modifier = Modifier
                                            .fillParentMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 10.dp),
                                        offer = item,
                                        isEditable = true,
                                        onTap = {
                                            navController.navigate(
                                                Routes.OfferDetails.path.replace(
                                                    "{offerId}",
                                                    item.id
                                                )
                                            )
                                        },
                                        onEdit = {
                                            navController.navigate(
                                                Routes.OfferCreateOrUpdate.path.replace(
                                                    "{offer}",
                                                    item.jsonSerialize()
                                                )
                                            )
                                        },
                                        onDelete = {
                                            distributorOffersViewModel.deleteOffer(
                                                offer = item,
                                                onSuccess = {
                                                    coroutineScope.launch {
                                                        snackbarHostState.show(
                                                            message = Strings.get(R.string.offer_deleted_successfully)
                                                        )
                                                    }
                                                    distributorOffersViewModel.getOffers()
                                                },
                                                onFailure = {
                                                    coroutineScope.launch {
                                                        snackbarHostState.show(
                                                            message = Strings.get(R.string.offer_deletion_failed)
                                                        )
                                                    }
                                                }
                                            )
                                        }
                                    )
                                }

                                item {
                                    PaginationComponent(
                                        modifier = Modifier.fillMaxWidth(),
                                        page = offers,
                                        onPrevious = { distributorOffersViewModel.getPrevious() },
                                        onNext = { distributorOffersViewModel.getNext() }
                                    )
                                }

                                item {
                                    Box(modifier = Modifier.height(70.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}