package com.ola.recoverunsold.ui.screens.distributor.offers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.core.StatusCode
import com.ola.recoverunsold.api.query.OfferFilterQuery
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.Page
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.offer.OfferFilterComponent
import com.ola.recoverunsold.ui.components.offer.OfferItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.utils.misc.jsonSerialize
import com.ola.recoverunsold.utils.misc.remove
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import java.util.Date

@Composable
fun DistributorOffersScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    distributorOffersViewModel: DistributorOffersViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState
            )
        },
        drawerContent = DrawerContent(navController, snackbarHostState),
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(
                    Routes.OfferCreateOrUpdate.path.remove("{offer}")
                )
            }) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        when (distributorOffersViewModel.offersApiResult.status) {
            ApiStatus.LOADING -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
            ApiStatus.ERROR -> {
                LaunchedEffect(snackbarHostState) {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(
                            message = distributorOffersViewModel.errorMessage()
                                ?: Strings.get(R.string.unknown_error_occured),
                            actionLabel = Strings.get(R.string.ok),
                            duration = SnackbarDuration.Long
                        ).also {
                            distributorOffersViewModel.getOffers()
                        }
                    }
                }
            }
            else -> {
//                val offers = distributorOffersViewModel.offersGetResponse.data!! TODO :

                val offers = Page(
                    items = listOf(
                        Offer("48562", Date(), 4000UL, 4, 150.0, Date(), null, emptyList())
                    ), hasNext = false, pageNumber = 1, pageSize = 1, total = 1
                )

                if (offers.items.isEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            stringResource(R.string.no_offer_published),
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(paddingValues)
                            .fillMaxSize()
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OfferFilterComponent(
                                    modifier = Modifier.padding(20.dp),
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

                        items(items = offers.items) { item ->
                            OfferItem(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
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
                                                snackbarHostState.showSnackbar(
                                                    message = Strings.get(R.string.offer_deleted_successfully),
                                                    actionLabel = Strings.get(R.string.ok),
                                                    duration = SnackbarDuration.Long
                                                )
                                            }
                                            distributorOffersViewModel.getOffers()
                                        },
                                        onFailure = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar(
                                                    message = Strings.get(R.string.offer_deletion_failed),
                                                    actionLabel = Strings.get(R.string.ok),
                                                    duration = SnackbarDuration.Long
                                                )
                                            }
                                        }
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
                                    Button(onClick = {
                                        distributorOffersViewModel.getPrevious()
                                    }) {
                                        Text(stringResource(id = R.string.previous))
                                    }
                                }
                                if (offers.hasNext) {
                                    Button(onClick = {
                                        distributorOffersViewModel.getNext()
                                    }) {
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

class DistributorOffersViewModel(
    private val offerServiceWrapper: OfferServiceWrapper = get(OfferServiceWrapper::class.java)
) : ViewModel() {
    var offerFilterQuery by mutableStateOf(OfferFilterQuery())
    var offersApiResult: ApiCallResult<Page<Offer>> by mutableStateOf(ApiCallResult.Inactive())

    init {
        getOffers()
    }

    fun getOffers() {
        offersApiResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val userId = UserObserver.user.value!!.id
            val response = offerServiceWrapper.getDistributorOffers(userId, offerFilterQuery)
            offersApiResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = response.body())
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun getNext() {
        offerFilterQuery = offerFilterQuery.inc()
        getOffers()
    }

    fun getPrevious() {
        offerFilterQuery = offerFilterQuery.dec()
        getOffers()
    }

    fun resetFilter() {
        offerFilterQuery = OfferFilterQuery()
    }

    fun deleteOffer(offer: Offer, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val token = TokenStore.get()!!
        viewModelScope.launch {
            val response = offerServiceWrapper.deleteOffer(token.bearerToken, offer.id)
            if (response.isSuccessful) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    fun errorMessage(): String? {
        if (offersApiResult.status == ApiStatus.ERROR) {
            return when (offersApiResult.statusCode) {
                StatusCode.Unauthorized.code -> Strings.get(R.string.not_authenticated_full_message)
                else -> Strings.get(R.string.unknown_error_occured)
            }
        }
        return null
    }
}

