package com.ola.recoverunsold.ui.screens.shared

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.query.OfferFilterQuery
import com.ola.recoverunsold.ui.components.account.UserAccountHeader
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.distributor.DistributorInformationLine
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.offer.OfferFilterComponent
import com.ola.recoverunsold.ui.components.offer.OfferItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorDetailsViewModel
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorDetailsViewModelFactory
import com.ola.recoverunsold.utils.misc.formatDate
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
                title = stringResource(id = R.string.distributor_information),
                canGoBack = true,
                navController = navController
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
                    val distributorInformation = distributorDetailsViewModel
                        .distributorApiCallResult
                        .data!!
                    val context = LocalContext.current

                    Column(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    ) {
                        DistributorInformationLine(
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .align(Alignment.CenterHorizontally),
                            data = distributorInformation.username,
                            textStyle = MaterialTheme.typography.h6
                        )

                        UserAccountHeader(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            id = distributorInformation.id,
                            name = distributorInformation.username,
                            size = (LocalConfiguration.current.screenWidthDp * 0.25).dp
                        )

                        Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                            DistributorInformationLine(
                                modifier = Modifier.padding(vertical = 5.dp),
                                data = distributorInformation.email
                            )

                            DistributorInformationLine(
                                modifier = Modifier.padding(vertical = 5.dp),
                                data = distributorInformation.phone
                            )

                            if (distributorInformation.websiteUrl != null) {
                                DistributorInformationLine(
                                    modifier = Modifier.padding(vertical = 5.dp),
                                    label = stringResource(id = R.string.website_url_label),
                                    data = distributorInformation.websiteUrl
                                )
                            }

                            DistributorInformationLine(
                                modifier = Modifier.padding(vertical = 5.dp),
                                label = stringResource(id = R.string.member_since_label),
                                data = distributorInformation.createdAt.formatDate()
                            )
                        }

                        Button(
                            onClick = {
                                val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${distributorInformation.phone}")
                                }
                                startActivity(context, phoneIntent, null)
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.75F)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(stringResource(id = R.string.call))
                        }

                        Button(
                            onClick = {
                                val emailIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "*/*"
                                    putExtra(
                                        Intent.EXTRA_EMAIL,
                                        arrayOf(distributorInformation.email)
                                    )
                                }
                                startActivity(
                                    context,
                                    Intent.createChooser(
                                        emailIntent,
                                        Strings.get(R.string.send_email)
                                    ),
                                    null
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.75F)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Text(stringResource(id = R.string.send_email))
                        }
                    }
                }
            }

            when (distributorDetailsViewModel.offersApiCallResult.status) {
                ApiStatus.LOADING, ApiStatus.INACTIVE -> {}
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
                                            Routes.OfferDetails
                                                .path
                                                .replace("{offerId}", item.id)
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