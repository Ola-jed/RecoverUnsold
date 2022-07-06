package com.ola.recoverunsold.ui.screens.distributor.offers

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.api.core.ApiCallResult
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.api.query.PaginationQuery
import com.ola.recoverunsold.api.requests.ProductCreateRequest
import com.ola.recoverunsold.api.services.wrappers.LocationServiceWrapper
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.utils.misc.jsonDeserialize
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.validation.FormState
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.get
import java.util.Date

// TODO : finish

@Composable
fun DistributorOfferFormScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    serializedOffer: String? = null,
    distributorOfferFormViewModel: DistributorOfferFormViewModel = viewModel(
        factory = DistributorOfferFormViewModelFactory(serializedOffer.jsonDeserialize<Offer>())
    )
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    val offer = serializedOffer.jsonDeserialize<Offer>()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                canGoBack = true,
                navController = navController
            )
        }
    ) { paddingValues ->
        DistributorOfferFormContent(
            modifier = Modifier
                .padding(paddingValues),
        )
    }
}

@Composable
fun DistributorOfferFormContent(
    modifier: Modifier,

) {

}

class DistributorOfferFormViewModelFactory(private val offer: Offer?) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DistributorOfferFormViewModel(offer = offer) as T
    }
}

class DistributorOfferFormViewModel(
    private val offerServiceWrapper: OfferServiceWrapper = get(OfferServiceWrapper::class.java),
    private val locationServiceWrapper: LocationServiceWrapper = get(LocationServiceWrapper::class.java),
    private val offer: Offer?
) : ViewModel() {
    private val token = TokenStore.get()!!
    var formState by mutableStateOf(FormState())
    var locationPaginationQuery by mutableStateOf(PaginationQuery())
    var locationsResponse by mutableStateOf(ApiStatus.INACTIVE)
    var locations: List<Location> by mutableStateOf(emptyList())

    var startDate by mutableStateOf(offer?.startDate ?: Date())
    var endDate by mutableStateOf(
        if (offer != null) {
            Date.from(
                offer.startDate.toInstant().plusSeconds(offer.duration.toLong())
            )
        } else {
            Date()
        }
    )
    var beneficiaries by mutableStateOf(offer?.beneficiaries ?: 0)
    var price by mutableStateOf(offer?.price ?: 0.0)
    var locationId by mutableStateOf(offer?.location?.id ?: "")
    var products by mutableStateOf(emptyList<ProductCreateRequest>())

    init {
        fetchPublishedLocations()
    }

    private fun fetchPublishedLocations() {
        locationsResponse = ApiStatus.LOADING
        viewModelScope.launch {
            val response =
                locationServiceWrapper.getLocations(token.bearerToken, locationPaginationQuery)
            if (response.isSuccessful) {
                val responsePage = response.body()!!
                if (responsePage.hasNext) {
                    locations = locations.plus(responsePage.items)
                    locationPaginationQuery = locationPaginationQuery++
                    fetchPublishedLocations()
                } else {
                    locations = locations.plus(responsePage.items)
                }
            } else {
                locationsResponse = ApiStatus.ERROR
            }
        }
    }


}