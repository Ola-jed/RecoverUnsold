package com.ola.recoverunsold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.ola.recoverunsold.ui.navigation.NavigationManager
import com.ola.recoverunsold.ui.screens.viewmodels.MainViewModel
import com.ola.recoverunsold.ui.theme.RecoverUnsoldTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.fetchData(applicationContext)
        installSplashScreen().apply {
            setKeepOnScreenCondition { !viewModel.hasFinishedLoading.value }
        }
        setContent {
            RecoverUnsoldTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
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