package com.ola.recoverunsold.utils.misc

import android.content.Context
import com.ola.recoverunsold.api.core.ApiClient
import com.ola.recoverunsold.api.services.FcmTokenService
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver

suspend fun Context.logout() {
    val token = TokenStore.get()
    if (token != null) {
        try {
            val fcmTokenService: FcmTokenService = ApiClient.buildService()
            fcmTokenService.deleteAllFcmTokens()
        } catch (e: Exception) {
            // Nothing because we should not hinder the user experience
        }
        TokenStore(this).removeToken()
    }
    UserObserver.remove()
}