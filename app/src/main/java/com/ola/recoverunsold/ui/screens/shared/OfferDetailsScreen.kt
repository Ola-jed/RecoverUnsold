package com.ola.recoverunsold.ui.screens.shared

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.app.SubtitleWithIcon
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.components.location.LocationItem
import com.ola.recoverunsold.ui.components.product.ProductItem
import com.ola.recoverunsold.utils.misc.formatDateTime
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import com.ola.recoverunsold.utils.store.UserObserver
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import java.util.Date

// TODO: handle product update and delete for distributors

@Composable
fun OfferDetailsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    offerId: String,
    offerDetailsViewModel: OfferDetailsViewModel = viewModel(
        factory = OfferDetailsViewModelFactory(offerId)
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
                canGoBack = true,
                navController = navController
            )
        },
        drawerContent = DrawerContent(navController, snackbarHostState)
    ) { paddingValues ->
        when (offerDetailsViewModel.offerApiCallResult.status) {
            ApiStatus.LOADING -> LoadingIndicator()
            ApiStatus.ERROR -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = { offerDetailsViewModel.getOffer() },
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(stringResource(id = R.string.retry))
                    }
                }
                LaunchedEffect(snackbarHostState) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = offerDetailsViewModel.errorMessage()
                                ?: Strings.get(R.string.unknown_error_occured)
                        )
                    }
                }
            }
            else -> {
                val offer = offerDetailsViewModel.offerApiCallResult.data!!
                val height = LocalConfiguration.current.screenHeightDp
                val width = LocalConfiguration.current.screenWidthDp

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 15.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    SubtitleWithIcon(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text = stringResource(id = R.string.offer_details),
                        imageVector = Icons.Default.Info
                    )

                    Text(
                        text = stringResource(R.string.total_amount, offer.price.toString()),
                        modifier = Modifier.padding(top = 10.dp)
                    )

                    if (offer.beneficiaries != null) {
                        Text(
                            text = stringResource(
                                R.string.offer_beneficiaries_data,
                                offer.beneficiaries
                            ),
                            modifier = Modifier.padding(vertical = 5.dp)
                        )
                    }

                    Text(
                        text = stringResource(
                            R.string.start_date_time,
                            offer.startDate.formatDateTime()
                        ),
                        modifier = Modifier.padding(vertical = 5.dp)
                    )

                    Text(
                        text = stringResource(
                            R.string.end_date_time,
                            Date.from(
                                offer.startDate.toInstant().plusSeconds(offer.duration.toLong())
                            ).formatDateTime()
                        ),
                        modifier = Modifier.padding(vertical = 5.dp)
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
                                .height((height * 0.3).dp)
                        ) {
                            items(items = offer.products) {
                                ProductItem(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .width((width * 0.6).dp),
                                    product = it,
                                    isEditable = true
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
                            location = offer.location,
                            isModifiable = true
                        )

                        val context = LocalContext.current
                        Button(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 15.dp), onClick = {
                                val mapsIntent = Intent(Intent.ACTION_VIEW)
                                mapsIntent.data = Uri.Builder().scheme("https")
                                    .authority("www.google.com")
                                    .appendPath("maps")
                                    .appendPath("dir")
                                    .appendPath("")
                                    .appendQueryParameter("api", "1")
                                    .appendQueryParameter(
                                        "destination",
                                        "${offer.location.coordinates.latitude},${offer.location.coordinates.longitude}"
                                    ).build()
                                startActivity(context, mapsIntent, null)
                            }) {
                            Text(stringResource(id = R.string.view_location_on_maps))
                        }
                    }
                }
            }
        }
    }
}


class OfferDetailsViewModelFactory(private val offerId: String) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OfferDetailsViewModel(offerId = offerId) as T
    }
}

class OfferDetailsViewModel(
    private val offerServiceWrapper: OfferServiceWrapper = get(OfferServiceWrapper::class.java),
    private val offerId: String
) : ViewModel() {
    var offerApiCallResult: ApiCallResult<Offer> by mutableStateOf(ApiCallResult.Inactive())
    val currentUserId = UserObserver.user.value?.id ?: ""

    init {
        getOffer()
    }

    fun getOffer() {
        offerApiCallResult = ApiCallResult.Loading()
        viewModelScope.launch {
            val response = offerServiceWrapper.getOffer(offerId)
            offerApiCallResult = if (response.isSuccessful) {
                ApiCallResult.Success(_data = response.body())
            } else {
                ApiCallResult.Error(code = response.code())
            }
        }
    }

    fun errorMessage(): String? = when (offerApiCallResult.statusCode) {
        404 -> Strings.get(R.string.offer_not_found)
        in 400..600 -> Strings.get(R.string.unknown_error_occured)
        else -> null
    }
}