package com.ola.recoverunsold.ui.screens.shared

import android.app.Activity
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.MainActivity
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.query.OfferFilterQuery
import com.ola.recoverunsold.ui.components.account.UserAccountHeader
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.CustomTextInput
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.app.NoContentComponent
import com.ola.recoverunsold.ui.components.app.PaginationComponent
import com.ola.recoverunsold.ui.components.distributor.DistributorInformationLine
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.offer.OfferFilterComponent
import com.ola.recoverunsold.ui.components.offer.OfferItem
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorDetailsViewModel
import com.ola.recoverunsold.utils.extensions.formatDate
import com.ola.recoverunsold.utils.extensions.show
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.validation.IsRequiredValidator
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistributorDetailsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    distributorId: String,
    distributorDetailsViewModel: DistributorDetailsViewModel = distributorDetailsViewModel(
        distributorId
    )
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(skipPartiallyExpanded = true),
        snackbarHostState = snackbarHostState
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController) },
        content = {
            BottomSheetScaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                scaffoldState = bottomSheetScaffoldState,
                topBar = {
                    AppBar(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        title = stringResource(id = R.string.distributor_information),
                        canGoBack = true,
                        navController = navController
                    )
                },
                sheetPeekHeight = 0.dp,
                sheetTonalElevation = 25.dp,
                sheetShadowElevation = 25.dp,
                sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
                sheetContent = {
                    val focusManager = LocalFocusManager.current

                    Column(
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 50.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CustomTextInput(
                            modifier = Modifier.fillMaxWidth(0.8F),
                            label = { Text(text = stringResource(id = R.string.report_reason)) },
                            value = distributorDetailsViewModel.reportReason,
                            onValueChange = { distributorDetailsViewModel.reportReason = it },
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            validator = IsRequiredValidator()
                        )

                        CustomTextInput(
                            modifier = Modifier.fillMaxWidth(0.8F),
                            label = { Text(text = stringResource(id = R.string.report_description)) },
                            value = distributorDetailsViewModel.reportMessage,
                            onValueChange = { distributorDetailsViewModel.reportMessage = it },
                            maxLines = 5,
                            singleLine = false,
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                        )

                        Button(
                            modifier = Modifier.fillMaxWidth(0.8F),
                            onClick = {
                                if (distributorDetailsViewModel.reportReason.isBlank()) {
                                    coroutineScope.launch {
                                        snackbarHostState.show(Strings.get(R.string.report_reason_required))
                                    }
                                } else {
                                    distributorDetailsViewModel.reportDistributor(
                                        onSuccess = {},
                                        onError = {}
                                    )
                                }
                            },
                            enabled = !distributorDetailsViewModel.reportingDistributor
                        ) {
                            if (distributorDetailsViewModel.reportingDistributor) {
                                CircularProgressIndicator()
                            } else {
                                Text(text = stringResource(id = R.string.report_distributor))
                            }
                        }
                    }
                }
            ) { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    when (distributorDetailsViewModel.distributorApiCallResult.status) {
                        ApiStatus.LOADING -> item { LoadingIndicator() }
                        ApiStatus.ERROR -> item {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Button(
                                    onClick = {
                                        distributorDetailsViewModel.offerFilterQuery =
                                            OfferFilterQuery()
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

                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    DistributorInformationLine(
                                        modifier = Modifier.padding(vertical = 5.dp),
                                        data = distributorInformation.username,
                                        textStyle = MaterialTheme.typography.titleLarge
                                    )
                                }
                            }

                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    UserAccountHeader(
                                        email = distributorInformation.email,
                                        size = (configuration.screenWidthDp * 0.25).dp
                                    )
                                }
                            }

                            item {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Column {
                                        DistributorInformationLine(
                                            modifier = Modifier.padding(vertical = 5.dp),
                                            data = distributorInformation.email
                                        )

                                        DistributorInformationLine(
                                            modifier = Modifier.padding(vertical = 5.dp),
                                            data = distributorInformation.phone
                                        )

                                        if (distributorInformation.websiteUrl != null) {
                                            val websiteLinkString = buildAnnotatedString {
                                                append(distributorInformation.websiteUrl)

                                                addStyle(
                                                    style = SpanStyle(
                                                        color = Color.Blue,
                                                        textDecoration = TextDecoration.Underline
                                                    ),
                                                    start = 0,
                                                    end = distributorInformation.websiteUrl.length
                                                )

                                                addStringAnnotation(
                                                    tag = "URL",
                                                    annotation = distributorInformation.websiteUrl,
                                                    start = 0,
                                                    end = distributorInformation.websiteUrl.length
                                                )
                                            }

                                            val uriHandler = LocalUriHandler.current

                                            DistributorInformationLine(
                                                modifier = Modifier.padding(vertical = 5.dp),
                                                text = {
                                                    ClickableText(
                                                        text = websiteLinkString,
                                                        onClick = {
                                                            val isValidUri =
                                                                distributorInformation.websiteUrl.startsWith(
                                                                    "http://"
                                                                )
                                                                        || distributorInformation.websiteUrl.startsWith(
                                                                    "https://"
                                                                )

                                                            val uriToOpen =
                                                                if (!isValidUri) {
                                                                    "http://${distributorInformation.websiteUrl}"
                                                                } else {
                                                                    distributorInformation.websiteUrl
                                                                }

                                                            uriHandler.openUri(uriToOpen)
                                                        }
                                                    )
                                                }
                                            )
                                        }

                                        DistributorInformationLine(
                                            modifier = Modifier.padding(vertical = 5.dp),
                                            label = stringResource(id = R.string.member_since_label),
                                            data = distributorInformation.createdAt.formatDate()
                                        )
                                    }
                                }
                            }

                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Button(
                                        onClick = {
                                            val phoneIntent = Intent(Intent.ACTION_DIAL).apply {
                                                data =
                                                    Uri.parse("tel:${distributorInformation.phone}")
                                            }
                                            startActivity(context, phoneIntent, null)
                                        },
                                        modifier = Modifier.fillMaxWidth(0.75F)
                                    ) {
                                        Text(stringResource(id = R.string.call))
                                    }
                                }
                            }

                            item {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Button(
                                        onClick = {
                                            val emailIntent = Intent(
                                                Intent.ACTION_SENDTO,
                                                Uri.parse("mailto:${distributorInformation.email}")
                                            )
                                            emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(context, emailIntent, null)
                                        },
                                        modifier = Modifier.fillMaxWidth(0.75F)
                                    ) {
                                        Text(stringResource(id = R.string.send_email))
                                    }
                                }
                            }
                        }
                    }

                    when (distributorDetailsViewModel.offersApiCallResult.status) {
                        ApiStatus.LOADING, ApiStatus.INACTIVE -> {}
                        ApiStatus.ERROR -> {
                            coroutineScope.launch {
                                snackbarHostState.show(Strings.get(R.string.error_occured_while_fetching_offers))
                            }
                        }

                        else -> {
                            val offers = distributorDetailsViewModel.offersApiCallResult.data!!

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
                                    NoContentComponent(
                                        modifier = Modifier.fillMaxWidth(),
                                        message = stringResource(id = R.string.no_offers_found)
                                    )
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
                                    PaginationComponent(
                                        modifier = Modifier.fillMaxWidth(),
                                        page = offers,
                                        onPrevious = { distributorDetailsViewModel.getPrevious() },
                                        onNext = { distributorDetailsViewModel.getNext() }
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

@Composable
fun distributorDetailsViewModel(distributorId: String): DistributorDetailsViewModel {
    val factory = EntryPointAccessors
        .fromActivity<MainActivity.ViewModelFactoryProvider>(LocalContext.current as Activity)
        .distributorDetailsViewModelFactory()
    return viewModel(factory = DistributorDetailsViewModel.provideFactory(factory, distributorId))
}