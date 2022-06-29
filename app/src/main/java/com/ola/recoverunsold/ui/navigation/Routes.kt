package com.ola.recoverunsold.ui.navigation

sealed class Routes(val path: String) {
    object Login : Routes("login")
    object Register : Routes("register")
    object CustomerRegister : Routes("register/customer")
    object DistributorRegister : Routes("register/distributor")
    object ForgotPassword : Routes("forgot-password")
    object PasswordReset : Routes("password-reset")
    object StartUserVerification : Routes("user-verification")
    object ConfirmUserVerification : Routes("user-verification/confirm")
    object UserVerificationSuccess : Routes("user-verification/success")
    object PasswordResetSuccess : Routes("password-reset/success")
    object Home : Routes("home")
    object Distributors : Routes("distributors")
    object Offers : Routes("offers")
    object OfferDetails : Routes("offers/{offerId}")
    object DistributorOffers : Routes("distributor/offers")
    object OfferCreateOrUpdate : Routes("offers/edit?offer={offer}")
    object Orders : Routes("orders")
    object DistributorOrdersReceived : Routes("orders/received")
    object DistributorAccount : Routes("account/distributor")
    object CustomerAccount : Routes("account/customer")
    object LocationCreateOrUpdate : Routes("locations?location={location}")
    object About : Routes("about")
}

