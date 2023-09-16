package com.ola.recoverunsold.ui.screens.distributor.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
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
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.app.NoContentComponent
import com.ola.recoverunsold.ui.components.app.PaginationComponent
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.repayment.RepaymentFilterComponent
import com.ola.recoverunsold.ui.components.repayment.RepaymentItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorRepaymentsViewModel
import com.ola.recoverunsold.utils.extensions.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

@Composable
fun DistributorRepaymentsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    repaymentsViewModel: DistributorRepaymentsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

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
                        title = stringResource(id = R.string.repayments)
                    )
                }
            ) { paddingValues ->
                when (repaymentsViewModel.repaymentsResponse.status) {
                    ApiStatus.LOADING -> LoadingIndicator()
                    ApiStatus.ERROR -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Button(
                                onClick = {
                                    repaymentsViewModel.repaymentsFilterQuery = repaymentsViewModel
                                        .repaymentsFilterQuery
                                        .copy(done = null)
                                    repaymentsViewModel.getRepayments()
                                },
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(stringResource(id = R.string.retry))
                            }
                        }

                        LaunchedEffect(snackbarHostState) {
                            coroutineScope.launch {
                                snackbarHostState.show(
                                    message = repaymentsViewModel.errorMessage()
                                        ?: Strings.get(R.string.unknown_error_occured)
                                )
                            }
                        }
                    }

                    else -> {
                        val repayments = repaymentsViewModel.repaymentsResponse.data!!

                        LazyColumn(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize()
                        ) {
                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    RepaymentFilterComponent(
                                        done = repaymentsViewModel.repaymentsFilterQuery.done,
                                        onDoneChange = {
                                            repaymentsViewModel.repaymentsFilterQuery =
                                                repaymentsViewModel
                                                    .repaymentsFilterQuery
                                                    .copy(done = it)
                                            repaymentsViewModel.getRepayments()
                                        }
                                    )
                                }
                            }

                            if (repayments.items.isEmpty()) {
                                item {
                                    NoContentComponent(
                                        modifier = Modifier.fillMaxWidth(),
                                        message = stringResource(R.string.no_repayments_found)
                                    )
                                }
                            } else {
                                items(items = repayments.items) { item ->
                                    RepaymentItem(
                                        modifier = Modifier
                                            .fillParentMaxWidth()
                                            .padding(horizontal = 20.dp, vertical = 10.dp),
                                        repayment = item,
                                        onTap = {
                                            navController.navigate(
                                                Routes.OrderDetails.path.replace(
                                                    "{orderId}",
                                                    item.order!!.id
                                                )
                                            )
                                        }
                                    )
                                }

                                item {
                                    PaginationComponent(
                                        modifier = Modifier.fillMaxWidth(),
                                        page = repayments,
                                        onLoadMore = { repaymentsViewModel.loadMore() }
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