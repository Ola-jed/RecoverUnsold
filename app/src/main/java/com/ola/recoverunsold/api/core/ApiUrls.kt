package com.ola.recoverunsold.api.core

object ApiUrls {
    const val apiBaseUrl = "https://recover-unsold-api.herokuapp.com/api/"
    const val customerRegisterUrl = "Auth/Register/Customer"
    const val distributorRegisterUrl = "Auth/Register/Distributor"
    const val loginUrl = "Auth/Login"
    const val forgotPasswordUrl = "ForgotPassword/Start"
    const val passwordResetUrl = "ForgotPassword/Reset"
    const val userVerificationStartUrl = "UserVerification/Start"
    const val userVerificationConfirmUrl = "UserVerification/Verify"
    const val accountUrl = "Account"
    const val customerAccountUrl = "Account/Customer"
    const val distributorAccountUrl = "Account/Distributor"
    const val accountPasswordUrl = "Account/Password"
    const val locationsUrl = "Locations"
    const val distributorsLocationsUrl = "Locations/Distributors"
    const val locationsSearchUrl = "Locations/Search"
    const val offersUrl = "Offers"
    const val distributorOffersUrl = "Offers/Distributors"
    const val closeOffersUrl = "Offers/CloseToLocation"
    const val productsUrl = "Products"
    const val distributorProductsUrl = "Products/Distributors"
}