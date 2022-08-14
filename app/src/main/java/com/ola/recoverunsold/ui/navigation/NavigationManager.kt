package com.ola.recoverunsold.ui.navigation

import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ola.recoverunsold.ui.screens.customer.AlertsScreen
import com.ola.recoverunsold.ui.screens.customer.CustomerAccountScreen
import com.ola.recoverunsold.ui.screens.customer.OrdersScreen
import com.ola.recoverunsold.ui.screens.distributor.account.DistributorAccountScreen
import com.ola.recoverunsold.ui.screens.distributor.account.DistributorLocationFormScreen
import com.ola.recoverunsold.ui.screens.distributor.offers.DistributorOfferFormScreen
import com.ola.recoverunsold.ui.screens.distributor.offers.DistributorOffersScreen
import com.ola.recoverunsold.ui.screens.distributor.offers.DistributorProductFormScreen
import com.ola.recoverunsold.ui.screens.distributor.orders.DistributorOrdersReceivedScreen
import com.ola.recoverunsold.ui.screens.shared.AboutScreen
import com.ola.recoverunsold.ui.screens.shared.CloseOffersScreen
import com.ola.recoverunsold.ui.screens.shared.DistributorDetailsScreen
import com.ola.recoverunsold.ui.screens.shared.DistributorsScreen
import com.ola.recoverunsold.ui.screens.shared.HomeScreen
import com.ola.recoverunsold.ui.screens.shared.OfferDetailsScreen
import com.ola.recoverunsold.ui.screens.shared.OffersScreen
import com.ola.recoverunsold.ui.screens.shared.OrderDetailsScreen
import com.ola.recoverunsold.ui.screens.shared.auth.CustomerRegisterScreen
import com.ola.recoverunsold.ui.screens.shared.auth.DistributorRegisterScreen
import com.ola.recoverunsold.ui.screens.shared.auth.ForgotPasswordScreen
import com.ola.recoverunsold.ui.screens.shared.auth.LoginScreen
import com.ola.recoverunsold.ui.screens.shared.auth.PasswordResetScreen
import com.ola.recoverunsold.ui.screens.shared.auth.PasswordResetSuccessScreen
import com.ola.recoverunsold.ui.screens.shared.auth.RegisterScreen
import com.ola.recoverunsold.ui.screens.shared.auth.StartUserVerificationScreen
import com.ola.recoverunsold.ui.screens.shared.auth.UserVerificationScreen
import com.ola.recoverunsold.ui.screens.shared.auth.UserVerificationSuccessScreen

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
        composable(Routes.UserVerificationSuccess.path) {
            UserVerificationSuccessScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.PasswordResetSuccess.path) {
            PasswordResetSuccessScreen(
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
        composable(
            Routes.DistributorDetails.path,
            arguments = listOf(
                navArgument("distributorId") {
                    nullable = false
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            DistributorDetailsScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState,
                distributorId = backStackEntry.arguments?.getString("distributorId")!!
            )
        }
        composable(Routes.Offers.path) {
            OffersScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.CloseOffers.path) {
            CloseOffersScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.DistributorOffers.path) {
            DistributorOffersScreen(
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
        composable(
            Routes.OrderDetails.path,
            arguments = listOf(
                navArgument("orderId") {
                    nullable = false
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            OrderDetailsScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState,
                orderId = backStackEntry.arguments?.getString("orderId")!!
            )
        }
        composable(Routes.DistributorAccount.path) {
            DistributorAccountScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.DistributorOrdersReceived.path) {
            DistributorOrdersReceivedScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.CustomerAccount.path) {
            CustomerAccountScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.About.path) {
            AboutScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(Routes.Alerts.path) {
            AlertsScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState
            )
        }
        composable(
            Routes.LocationCreateOrUpdate.path,
            arguments = listOf(navArgument("location") {
                nullable = true
                type = NavType.StringType
            })
        ) { backStackEntry ->
            DistributorLocationFormScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState,
                serializedLocation = backStackEntry.arguments?.getString("location")
            )
        }
        composable(
            Routes.OfferCreateOrUpdate.path,
            arguments = listOf(navArgument("offer") {
                nullable = true
                type = NavType.StringType
            })
        ) { backStackEntry ->
            DistributorOfferFormScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState,
                serializedOffer = backStackEntry.arguments?.getString("offer")
            )
        }
        composable(
            Routes.OfferProduct.path,
            arguments = listOf(
                navArgument("offerId") {
                    nullable = false
                    type = NavType.StringType
                }, navArgument("product") {
                    nullable = true
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            DistributorProductFormScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState,
                offerId = backStackEntry.arguments?.getString("offerId")!!,
                serializedProduct = backStackEntry.arguments?.getString("product")
            )
        }
        composable(
            Routes.OfferDetails.path,
            arguments = listOf(navArgument("offerId") {
                nullable = false
                type = NavType.StringType
            })
        ) { backStackEntry ->
            OfferDetailsScreen(
                navController = navHostController,
                snackbarHostState = snackbarHostState,
                offerId = backStackEntry.arguments?.getString("offerId")!!
            )
        }
    }
}