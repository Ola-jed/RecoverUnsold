package com.ola.recoverunsold.ui.screens.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.app.NoContentComponent
import com.ola.recoverunsold.ui.components.distributor.DistributorInformationComponent
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.offer.OfferItem
import com.ola.recoverunsold.ui.components.order.CustomerOrderStatsComponent
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.HomeViewModel
import com.ola.recoverunsold.utils.extensions.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

@Composable
fun CustomerHomeScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val isRefreshing by homeViewModel.isRefreshing.collectAsState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController) },
        content = {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    AppBar(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        title = stringResource(id = R.string.home)
                    )
                }
            ) { paddingValues ->
                SwipeRefresh(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
                    onRefresh = { homeViewModel.refresh() }
                ) {
                    when (homeViewModel.homeDataApiCallResult.status) {
                        ApiStatus.LOADING, ApiStatus.INACTIVE -> LoadingIndicator()
                        ApiStatus.ERROR -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Button(
                                    onClick = { homeViewModel.refresh() },
                                    modifier = Modifier.align(Alignment.Center)
                                ) {
                                    Text(stringResource(id = R.string.retry))
                                }
                            }

                            LaunchedEffect(snackbarHostState) {
                                coroutineScope.launch {
                                    snackbarHostState.show(
                                        message = homeViewModel.errorMessage()
                                            ?: Strings.get(R.string.unknown_error_occured)
                                    )
                                }
                            }
                        }

                        else -> {
                            val homeData = homeViewModel.homeDataApiCallResult.data!!
                            val offers = homeData.offers
                            val distributors = homeData.distributors

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                if (homeData.orderStats != null) {
                                    CustomerOrderStatsComponent(
                                        modifier = Modifier.padding(15.dp),
                                        customerOrderStats = homeData.orderStats
                                    )
                                }

                                Text(
                                    modifier = Modifier.padding(
                                        top = 15.dp,
                                        bottom = 15.dp,
                                        start = 15.dp
                                    ),
                                    text = stringResource(id = R.string.featured_offers),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                if (offers.isEmpty()) {
                                    NoContentComponent(message = stringResource(id = R.string.no_featured_offers))
                                } else {
                                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                                        items(items = offers) { item ->
                                            OfferItem(
                                                modifier = Modifier
                                                    .fillParentMaxWidth(0.90F)
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
                                    }
                                }

                                TextButton(
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(end = 10.dp),
                                    onClick = { navController.navigate(Routes.Offers.path) }
                                ) {
                                    Text(stringResource(id = R.string.view_all))

                                    Icon(
                                        modifier = Modifier.padding(start = 5.dp),
                                        imageVector = Icons.Default.KeyboardDoubleArrowRight,
                                        contentDescription = null
                                    )
                                }

                                Text(
                                    modifier = Modifier.padding(
                                        top = 25.dp,
                                        bottom = 15.dp,
                                        start = 15.dp
                                    ),
                                    text = stringResource(id = R.string.some_of_our_distributors),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                if (distributors.isEmpty()) {
                                    NoContentComponent(
                                        message = stringResource(id = R.string.no_distributor_found)
                                    )
                                } else {
                                    LazyRow(modifier = Modifier.fillMaxWidth()) {
                                        items(items = distributors) { item ->
                                            DistributorInformationComponent(
                                                modifier = Modifier
                                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                                distributorInformation = item,
                                                onTap = {
                                                    navController.navigate(
                                                        Routes.DistributorDetails
                                                            .path
                                                            .replace("{distributorId}", item.id)
                                                    )
                                                }
                                            )
                                        }
                                    }
                                }

                                TextButton(
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(end = 10.dp),
                                    onClick = { navController.navigate(Routes.Distributors.path) }
                                ) {
                                    Text(stringResource(id = R.string.view_all))

                                    Icon(
                                        modifier = Modifier.padding(start = 5.dp),
                                        imageVector = Icons.Default.KeyboardDoubleArrowRight,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}