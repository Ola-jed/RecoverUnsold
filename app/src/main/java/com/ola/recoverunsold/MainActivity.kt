package com.ola.recoverunsold

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import co.opensi.kkiapay.uikit.Kkiapay
import com.ola.recoverunsold.ui.components.connectivity.ConnectivityStatus
import com.ola.recoverunsold.ui.navigation.NavigationManager
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorDetailsViewModel
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorLocationFormViewModel
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorOfferFormViewModel
import com.ola.recoverunsold.ui.screens.viewmodels.MainViewModel
import com.ola.recoverunsold.ui.screens.viewmodels.OfferDetailsViewModel
import com.ola.recoverunsold.ui.screens.viewmodels.OrderDetailsViewModel
import com.ola.recoverunsold.ui.screens.viewmodels.ProductFormViewModel
import com.ola.recoverunsold.ui.theme.RecoverUnsoldTheme
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    interface ViewModelFactoryProvider {
        fun productFormViewModelFactory(): ProductFormViewModel.Factory
        fun orderDetailsViewModelFactory(): OrderDetailsViewModel.Factory
        fun distributorOfferFormViewModelFactory(): DistributorOfferFormViewModel.Factory
        fun distributorLocationFormViewModelFactory(): DistributorLocationFormViewModel.Factory
        fun distributorDetailsViewModelFactory(): DistributorDetailsViewModel.Factory
        fun offerDetailsViewModelFactory(): OfferDetailsViewModel.Factory
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initializeApp(applicationContext)
        installSplashScreen().apply {
            setKeepOnScreenCondition { !viewModel.hasFinishedLoading.value }
        }
        setContent {
            RecoverUnsoldTheme {
                val bgColor = MaterialTheme.colorScheme.background

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(bgColor)
                ) {
                    ConnectivityStatus(modifier = Modifier.fillMaxWidth())

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        color = bgColor
                    ) {
                        val snackbarHostState = remember { SnackbarHostState() }
                        NavigationManager(
                            navHostController = rememberNavController(),
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            }
        }
    }

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "super.onActivityResult(requestCode, resultCode, data)",
            "androidx.appcompat.app.AppCompatActivity"
        )
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Kkiapay.get().handleActivityResult(requestCode, resultCode, data)
    }
}