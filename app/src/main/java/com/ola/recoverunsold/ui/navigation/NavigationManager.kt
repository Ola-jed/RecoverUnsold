package com.ola.recoverunsold.ui.navigation

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ola.recoverunsold.ui.screens.customer.DistributorsScreen
import com.ola.recoverunsold.ui.screens.customer.OffersScreen
import com.ola.recoverunsold.ui.screens.customer.OrdersScreen
import com.ola.recoverunsold.ui.screens.shared.HomeScreen
import com.ola.recoverunsold.ui.screens.shared.auth.*

@Composable
fun NavigationManager(navHostController: NavHostController, snackbarHostState: SnackbarHostState) {
    NavHost(navController = navHostController, startDestination = Routes.Home.path) {
        composable(Routes.Home.path) {
            HomeScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.Login.path) {
            LoginScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.Register.path) {
            RegisterScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.CustomerRegister.path) {
            CustomerRegisterScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.DistributorRegister.path) {
            DistributorRegisterScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.ForgotPassword.path) {
            ForgotPasswordScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.PasswordReset.path) {
            PasswordResetScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.StartUserVerification.path) {
            StartUserVerificationScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.ConfirmUserVerification.path) {
            UserVerificationScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.Distributors.path) {
            DistributorsScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.Offers.path) {
            OffersScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.Orders.path) {
            OrdersScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
    }
}