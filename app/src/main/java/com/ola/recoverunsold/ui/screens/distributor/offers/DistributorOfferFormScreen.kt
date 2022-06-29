package com.ola.recoverunsold.ui.screens.distributor.offers

import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.api.services.wrappers.OfferServiceWrapper
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.utils.misc.jsonDeserialize
import org.koin.java.KoinJavaComponent.get

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
        },
        drawerGesturesEnabled = false
    ) {}
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
    private val offer: Offer?
) : ViewModel() {

}