package com.ola.recoverunsold.ui.screens.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.app.SubtitleWithIcon
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.location.LocationItem
import com.ola.recoverunsold.ui.components.opinion.OpinionItem
import com.ola.recoverunsold.ui.components.product.ProductItem
import com.ola.recoverunsold.ui.screens.viewmodels.OrderDetailsViewModel
import com.ola.recoverunsold.ui.screens.viewmodels.OrderDetailsViewModelFactory
import com.ola.recoverunsold.utils.misc.addSeconds
import com.ola.recoverunsold.utils.misc.formatDate
import com.ola.recoverunsold.utils.misc.formatDateTime
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros
import com.ola.recoverunsold.utils.misc.internationalizedValueSingular
import com.ola.recoverunsold.utils.misc.openMapWithCoordinates
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.misc.toIcon
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderDetailsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    orderId: String,
    orderDetailsViewModel: OrderDetailsViewModel = viewModel(
        factory = OrderDetailsViewModelFactory(orderId)
    )
) {
    val coroutineScope = rememberCoroutineScope()
    val isRefreshing by orderDetailsViewModel.isRefreshing.collectAsState()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed),
        snackbarHostState = snackbarHostState
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                canGoBack = true,
                navController = navController,
                title = stringResource(id = R.string.order_details)
            )
        },
        drawerContent = DrawerContent(navController),
        sheetContent = {
            val focusManager = LocalFocusManager.current

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTextInput(
                    modifier = Modifier.fillMaxWidth(),
                    value = orderDetailsViewModel.opinionComment,
                    onValueChange = { orderDetailsViewModel.opinionComment = it },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Comment, contentDescription = null)
                    },
                    label = { Text(text = stringResource(id = R.string.comment)) },
                    singleLine = false,
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        orderDetailsViewModel.publishOpinion()
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    },
                    enabled = orderDetailsViewModel.opinionComment.isNotBlank()
                            && orderDetailsViewModel.opinionCommentApiCallResult.status != ApiStatus.LOADING
                ) {
                    if (orderDetailsViewModel.opinionCommentApiCallResult.status == ApiStatus.LOADING) {
                        CircularProgressIndicator()
                    } else {
                        Text(text = stringResource(id = R.string.submit))
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetElevation = 25.dp,
        sheetGesturesEnabled = true,
        sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        floatingActionButton = {
            val canShowFab = orderDetailsViewModel.isCustomer
                    && orderDetailsViewModel.orderApiCallResult.data != null
                    && (orderDetailsViewModel.orderApiCallResult.data?.status == OrderStatus.Completed
                    || orderDetailsViewModel.orderApiCallResult.data?.status == OrderStatus.Approved)
            if (canShowFab) {
                FloatingActionButton(onClick = {
                    coroutineScope.launch { bottomSheetScaffoldState.bottomSheetState.expand() }
                }) {
                    Icon(imageVector = Icons.Default.AddComment, contentDescription = null)
                }
            } else {
                Box(modifier = Modifier.size(0.dp))
            }
        }
    ) { paddingValues ->
        SwipeRefresh(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
            onRefresh = { orderDetailsViewModel.refresh() }
        ) {
            when (orderDetailsViewModel.orderApiCallResult.status) {
                ApiStatus.LOADING, ApiStatus.INACTIVE -> LoadingIndicator()
                ApiStatus.ERROR -> {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Button(
                            onClick = { orderDetailsViewModel.getOrder() },
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(stringResource(id = R.string.retry))
                        }
                    }
                    LaunchedEffect(snackbarHostState) {
                        coroutineScope.launch {
                            snackbarHostState.show(
                                message = orderDetailsViewModel.errorMessage()
                                    ?: Strings.get(R.string.unknown_error_occured)
                            )
                        }
                    }
                }
                else -> {
                    val order = orderDetailsViewModel.orderApiCallResult.data!!
                    val offer = order.offer!!

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 15.dp)
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Surface(
                            modifier = Modifier.padding(top = 15.dp),
                            elevation = 15.dp,
                            color = MaterialTheme.colors.secondary,
                            shape = RoundedCornerShape(30.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 7.5.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = order.status.toIcon(),
                                    contentDescription = null,
                                    tint = MaterialTheme.colors.onSecondary
                                )
                                Text(
                                    modifier = Modifier.padding(start = 3.dp),
                                    text = order.status.internationalizedValueSingular(),
                                    color = MaterialTheme.colors.onSecondary
                                )
                            }
                        }

                        SubtitleWithIcon(
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .align(Alignment.CenterHorizontally),
                            text = stringResource(R.string.order_details),
                            imageVector = Icons.Default.Sell
                        )

                        Text(
                            text = stringResource(
                                R.string.ordered_on,
                                order.createdAt.formatDate()
                            ),
                            modifier = Modifier.padding(top = 15.dp),
                            fontSize = 18.sp
                        )

                        Text(
                            modifier = Modifier.padding(top = 15.dp),
                            text = "${stringResource(id = R.string.to_be_picked_up_on)} : ${order.withdrawalDate.formatDateTime()}",
                            fontSize = 18.sp
                        )

                        SubtitleWithIcon(
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .align(Alignment.CenterHorizontally),
                            text = stringResource(R.string.offer_details),
                            imageVector = Icons.Default.Info
                        )

                        Text(
                            modifier = Modifier.padding(top = 15.dp),
                            text = stringResource(
                                R.string.total_amount,
                                offer.price.formatWithoutTrailingZeros()
                            )
                        )

                        if (offer.beneficiaries != null) {
                            Text(
                                modifier = Modifier.padding(top = 15.dp),
                                text = stringResource(
                                    id = R.string.offer_beneficiaries_data,
                                    offer.beneficiaries
                                )
                            )
                        }

                        Text(
                            modifier = Modifier.padding(top = 15.dp),
                            text = stringResource(
                                R.string.start_date_time,
                                offer.startDate.formatDateTime()
                            )
                        )

                        Text(
                            modifier = Modifier.padding(top = 15.dp),
                            text = stringResource(
                                R.string.end_date_time,
                                offer.startDate.addSeconds(offer.duration).formatDateTime()
                            )
                        )

                        Text(
                            modifier = Modifier.padding(top = 15.dp),
                            text = stringResource(
                                R.string.published_the,
                                offer.createdAt.formatDate()
                            )
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
                                            .width((LocalConfiguration.current.screenWidthDp * 0.6).dp),
                                        product = it
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
                                location = offer.location
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
                        }

                        if (!orderDetailsViewModel.isCustomer) {
                            val customer = order.customer

                            if (customer != null) {
                                SubtitleWithIcon(
                                    modifier = Modifier
                                        .padding(top = 15.dp)
                                        .align(Alignment.CenterHorizontally),
                                    text = stringResource(R.string.customer),
                                    imageVector = Icons.Default.Person
                                )

                                Text(
                                    modifier = Modifier.padding(top = 15.dp),
                                    text = "${stringResource(id = R.string.username_label)} : ${customer.username}",
                                    fontSize = 18.sp
                                )

                                Text(
                                    modifier = Modifier.padding(top = 15.dp),
                                    text = "${stringResource(id = R.string.email_label)} : ${customer.email}",
                                    fontSize = 18.sp
                                )

                                Text(
                                    modifier = Modifier.padding(top = 15.dp),
                                    text = "${stringResource(id = R.string.member_since_label)} : ${customer.createdAt.formatDate()}",
                                    fontSize = 18.sp
                                )
                            }
                        }

                        SubtitleWithIcon(
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .align(Alignment.CenterHorizontally),
                            text = stringResource(id = R.string.comments),
                            imageVector = Icons.Default.Comment
                        )

                        if (order.opinions.isEmpty()) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 15.dp)
                                    .align(Alignment.CenterHorizontally),
                                text = stringResource(id = R.string.no_comments_yet),
                                fontSize = 17.sp
                            )
                        } else {
                            order.opinions.map {
                                OpinionItem(
                                    modifier = Modifier
                                        .padding(15.dp)
                                        .fillMaxWidth(),
                                    opinion = it,
                                    canDelete = orderDetailsViewModel.isCustomer,
                                    onDelete = {
                                        if (orderDetailsViewModel.isCustomer) {
                                            orderDetailsViewModel.deleteOpinion(
                                                opinion = it,
                                                onSuccess = {
                                                    coroutineScope.launch {
                                                        snackbarHostState.show(Strings.get(R.string.comment_deleted_successfully))
                                                        orderDetailsViewModel.getOrder()
                                                    }
                                                },
                                                onFailure = {
                                                    coroutineScope.launch {
                                                        snackbarHostState.show(Strings.get(R.string.comment_deletion_failed))
                                                    }
                                                }
                                            )
                                        }
                                    }
                                )
                            }

                            Box(modifier = Modifier.height(45.dp))
                        }
                    }
                }
            }

            when (orderDetailsViewModel.opinionCommentApiCallResult.status) {
                ApiStatus.ERROR -> coroutineScope.launch {
                    bottomSheetScaffoldState.snackbarHostState.show(Strings.get(R.string.comment_publish_failed))
                }
                ApiStatus.SUCCESS -> coroutineScope.launch {
                    bottomSheetScaffoldState.snackbarHostState.show(Strings.get(R.string.comment_published_successfully))
                    delay(500)
                    orderDetailsViewModel.getOrder()
                }
                else -> {}
            }
        }
    }
}