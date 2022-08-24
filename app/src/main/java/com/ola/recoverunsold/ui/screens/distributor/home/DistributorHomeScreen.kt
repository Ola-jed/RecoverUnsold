package com.ola.recoverunsold.ui.screens.distributor.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.KeyboardDoubleArrowRight
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.DatePicker
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.order.OrderItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorHomeViewModel
import com.ola.recoverunsold.utils.misc.formatDate
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.misc.toBars
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch
import me.bytebeats.views.charts.bar.BarChart
import me.bytebeats.views.charts.bar.BarChartData
import me.bytebeats.views.charts.bar.render.bar.SimpleBarDrawer
import me.bytebeats.views.charts.bar.render.label.SimpleLabelDrawer
import me.bytebeats.views.charts.bar.render.xaxis.SimpleXAxisDrawer
import me.bytebeats.views.charts.bar.render.yaxis.SimpleYAxisDrawer
import me.bytebeats.views.charts.simpleChartAnimation
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.Date

@Composable
fun DistributorHomeScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    homeViewModel: DistributorHomeViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val isRefreshing by homeViewModel.isRefreshing.collectAsState()
    var showPeriodStartDialog by remember { mutableStateOf(false) }
    var showPeriodEndDialog by remember { mutableStateOf(false) }
    var isDateRegionExpanded by remember { mutableStateOf(false) }

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
                    val orders = homeData.orders
                    val screenHeight = LocalConfiguration.current.screenHeightDp
                    val onBgColor = MaterialTheme.colors.onBackground

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.padding(
                                    top = 15.dp,
                                    bottom = 15.dp,
                                    start = 15.dp
                                ),
                                text = stringResource(R.string.order_statistics),
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.body1
                            )

                            IconButton(onClick = { isDateRegionExpanded = !isDateRegionExpanded }) {
                                Icon(
                                    modifier = Modifier.then(Modifier.size(48.dp)),
                                    imageVector = if (isDateRegionExpanded) {
                                        Icons.Default.ExpandLess
                                    } else {
                                        Icons.Default.ExpandMore
                                    },
                                    contentDescription = null
                                )
                            }
                        }

                        if (isDateRegionExpanded) {
                            CustomTextInput(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .clickable { showPeriodStartDialog = true },
                                value = homeViewModel.periodQuery.periodStart.formatDate(),
                                readOnly = true,
                                enabled = false,
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.start_date)) },
                                trailingIcon = {
                                    Icon(Icons.Default.EditCalendar, contentDescription = null)
                                }
                            )

                            CustomTextInput(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .clickable { showPeriodEndDialog = true },
                                value = homeViewModel.periodQuery.periodEnd.formatDate(),
                                readOnly = true,
                                enabled = false,
                                onValueChange = {},
                                label = { Text(text = stringResource(R.string.end_date)) },
                                trailingIcon = {
                                    Icon(Icons.Default.EditCalendar, contentDescription = null)
                                }
                            )
                        }

                        val bars = homeData.toBars()
                        if (bars.isEmpty()) {
                            Text(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(vertical = 15.dp),
                                text = stringResource(R.string.no_order_found),
                                style = MaterialTheme.typography.body1
                            )
                        } else {
                            BarChart(
                                barChartData = BarChartData(
                                    bars = bars,
                                    maxBarValue = if (bars.isEmpty()) 5F else bars.maxOf { it.value }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height((screenHeight * 0.4).dp)
                                    .padding(vertical = 10.dp),
                                animation = simpleChartAnimation(),
                                barDrawer = SimpleBarDrawer(),
                                xAxisDrawer = SimpleXAxisDrawer(
                                    axisLineColor = onBgColor,
                                    axisLineThickness = 2.dp
                                ),
                                yAxisDrawer = SimpleYAxisDrawer(
                                    axisLineColor = onBgColor,
                                    labelTextColor = onBgColor,
                                    labelValueFormatter = { it.toInt().toString() },
                                    drawLabelEvery = 6,
                                    axisLineThickness = 2.dp
                                ),
                                labelDrawer = SimpleLabelDrawer(
                                    labelTextSize = 13.sp,
                                    labelTextColor = onBgColor
                                )
                            )
                        }

                        Text(
                            modifier = Modifier
                                .padding(top = 25.dp, bottom = 15.dp, start = 15.dp)
                                .align(Alignment.Start),
                            text = stringResource(id = R.string.latest_orders),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.body1
                        )

                        LazyRow(modifier = Modifier.fillMaxWidth()) {
                            if (orders.isEmpty()) {
                                item {
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            stringResource(R.string.no_order_found),
                                            style = MaterialTheme.typography.h6,
                                            modifier = Modifier
                                                .padding(horizontal = 10.dp)
                                                .align(Alignment.Center)
                                        )
                                    }
                                }
                            } else {
                                items(items = orders) { item ->
                                    OrderItem(
                                        modifier = Modifier
                                            .fillParentMaxWidth(0.90F)
                                            .padding(horizontal = 20.dp, vertical = 10.dp),
                                        order = item,
                                        onTap = {
                                            navController.navigate(
                                                Routes.OrderDetails
                                                    .path
                                                    .replace("{orderId}", item.id)
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        TextButton(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(end = 10.dp, bottom = 15.dp),
                            onClick = { navController.navigate(Routes.DistributorOrdersReceived.path) }
                        ) {
                            Text(stringResource(id = R.string.view_all))

                            Icon(
                                modifier = Modifier.padding(start = 5.dp),
                                imageVector = Icons.Default.KeyboardDoubleArrowRight,
                                contentDescription = null
                            )
                        }

                        if (showPeriodStartDialog) {
                            DatePicker(
                                date = homeViewModel.periodQuery.periodStart.toLocalDate(),
                                onDateUpdated = {
                                    homeViewModel.periodQuery = homeViewModel.periodQuery
                                        .copy(periodStart = it.toDate())
                                    showPeriodStartDialog = false
                                    homeViewModel.getHomeData()
                                },
                                onCancel = { showPeriodStartDialog = false },
                                show = showPeriodStartDialog,
                                maxDate = homeViewModel.periodQuery.periodEnd
                            )
                        }

                        if (showPeriodEndDialog) {
                            DatePicker(
                                date = homeViewModel.periodQuery.periodEnd.toLocalDate(),
                                onDateUpdated = {
                                    homeViewModel.periodQuery = homeViewModel.periodQuery
                                        .copy(periodEnd = it.toDate())
                                    showPeriodEndDialog = false
                                    homeViewModel.getHomeData()
                                },
                                onCancel = { showPeriodEndDialog = false },
                                minDate = homeViewModel.periodQuery.periodStart,
                                show = showPeriodEndDialog
                            )
                        }
                    }
                }
            }
        }
    }
}


private fun Date.toLocalDate(): LocalDate {
    return LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault()).toLocalDate()!!
}

private fun LocalDate.toDate(): Date {
    return Date.from(
        LocalDateTime.of(this, LocalTime.MIDNIGHT)
            .atZone(ZoneId.systemDefault())
            .toInstant()
    )
}