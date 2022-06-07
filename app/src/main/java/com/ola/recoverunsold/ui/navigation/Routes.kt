package com.ola.recoverunsold.ui.navigation

sealed class Routes(private val path: String) {
    object Login : Routes("login")
    object CustomerRegister : Routes("register/customer")
    object DistributorRegister : Routes("register/distributor")
    object ForgotPassword : Routes("forgot-password")
    object PasswordReset : Routes("password-reset")
    object StartUserVerification : Routes("user-verification")
    object ConfirmUserVerification : Routes("user-verification/confirm")
    object Home : Routes("home")
    object Distributors : Routes("distributors")
    object Offers : Routes("offers")
    object Orders : Routes("orders")
    object Account : Routes("account")
}

