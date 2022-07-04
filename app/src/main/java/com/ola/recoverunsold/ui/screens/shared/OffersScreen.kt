package com.ola.recoverunsold.ui.screens.shared

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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
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
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get

// TODO : Get offers near to this user
@Composable
fun OffersScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    offersViewModel: OffersViewModel = viewModel()
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
        drawerContent = DrawerContent(navController, snackbarHostState)
    ) { paddingValues ->
        when (offersViewModel.offersApiResult.status) {
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
                            message = offersViewModel.errorMessage()
                                ?: Strings.get(R.string.unknown_error_occured),
                            actionLabel = Strings.get(R.string.ok),
                            duration = SnackbarDuration.Long
                        ).also {
                            offersViewModel.resetFilter()
                            offersViewModel.getOffers()
                        }
                    }
                }
            }
            else -> {
                val offers = offersViewModel.offersApiResult.data!!
                if (offers.items.isEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            stringResource(R.string.no_offers_found),
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
                                horizontalArrangement = Arrangement.Start
                            ) {
                                OfferFilterComponent(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    minPrice = offersViewModel.offerFilterQuery.minPrice,
                                    maxPrice = offersViewModel.offerFilterQuery.maxPrice,
                                    minDate = offersViewModel.offerFilterQuery.minDate,
                                    maxDate = offersViewModel.offerFilterQuery.maxDate,
                                    active = offersViewModel.offerFilterQuery.active,
                                    onMinPriceChange = {
                                        offersViewModel.offerFilterQuery =
                                            offersViewModel.offerFilterQuery.copy(
                                                minPrice = it
                                            )
                                    },
                                    onMaxPriceChange = {
                                        offersViewModel.offerFilterQuery =
                                            offersViewModel.offerFilterQuery.copy(
                                                maxPrice = it
                                            )
                                    },
                                    onMinDateChange = {
                                        offersViewModel.offerFilterQuery =
                                            offersViewModel.offerFilterQuery.copy(
                                                minDate = it
                                            )
                                    },
                                    onMaxDateChange = {
                                        offersViewModel.offerFilterQuery =
                                            offersViewModel.offerFilterQuery.copy(
                                                maxDate = it
                                            )
                                    },
                                    onActiveChange = {
                                        offersViewModel.offerFilterQuery =
                                            offersViewModel.offerFilterQuery.copy(
                                                active = it
                                            )
                                    },
                                    onApply = {
                                        offersViewModel.getOffers()
                                    },
                                    onReset = {
                                        offersViewModel.resetFilter()
                                        offersViewModel.getOffers()
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
                                    Button(onClick = { offersViewModel.getPrevious() }) {
                                        Text(stringResource(id = R.string.previous))
                                    }
                                }
                                if (offers.hasNext) {
                                    Button(onClick = { offersViewModel.getNext() }) {
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

class OffersViewModel(
    private val offerServiceWrapper: OfferServiceWrapper = get(OfferServiceWrapper::class.java)
) : ViewModel() {
    var offerFilterQuery by mutableStateOf(OfferFilterQuery(active = true))
    var offersApiResult: ApiCallResult<Page<Offer>> by mutableStateOf(ApiCallResult.Inactive())

    fun getOffers() {
        offersApiResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = offerServiceWrapper.getOffers(offerFilterQuery)
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
        offerFilterQuery = OfferFilterQuery(active = true)
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