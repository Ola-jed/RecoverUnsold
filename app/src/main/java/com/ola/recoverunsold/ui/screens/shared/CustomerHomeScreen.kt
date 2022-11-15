package com.ola.recoverunsold.ui.screens.shared

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import co.opensi.kkiapay.uikit.Kkiapay
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ola.recoverunsold.App
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
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

@Composable
fun CustomerHomeScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val isRefreshing by homeViewModel.isRefreshing.collectAsState()
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                title = stringResource(id = R.string.home)
            )
        },
        drawerContent = DrawerContent(navController)
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

                    Kkiapay.get()
                        .setListener { status, transactionId ->
                            Toast.makeText(
                                context,
                                "Transaction: ${status.name} -> $transactionId",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    val activity = object : AppCompatActivity() {
                        override fun getApplicationContext(): Context = App.instance

                        @Deprecated("Deprecated in Java")
                        override fun startActivityForResult(intent: Intent, requestCode: Int) {
                        }
                    }

                    Kkiapay
                        .get()
                        .requestPayment(
                            activity,
                            1,
                            "Paiement de services",
                            "Nom Prenom",
                            "olabijed@gmail.com",
                            phone = "+22967975095"
                        )

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
                            modifier = Modifier.padding(top = 15.dp, bottom = 15.dp, start = 15.dp),
                            text = stringResource(id = R.string.featured_offers),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
                        )

                        if (offers.isEmpty()) {
                            NoContentComponent(message = stringResource(id = R.string.no_offers_found))
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
                            modifier = Modifier.padding(top = 25.dp, bottom = 15.dp, start = 15.dp),
                            text = stringResource(id = R.string.some_of_our_distributors),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
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