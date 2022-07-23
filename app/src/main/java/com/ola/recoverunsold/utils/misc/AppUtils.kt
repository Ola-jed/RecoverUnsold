package com.ola.recoverunsold.utils.misc

import android.content.Context
import com.ola.recoverunsold.api.services.FcmTokenService
import com.ola.recoverunsold.utils.store.TokenStore
import com.ola.recoverunsold.utils.store.UserObserver
import org.koin.java.KoinJavaComponent

suspend fun Context.logout() {
    val token = TokenStore.get()
    if (token != null) {
        try {
            val fcmTokenService = KoinJavaComponent
                .get<FcmTokenService>(FcmTokenService::class.java)
            fcmTokenService.deleteAllFcmTokens(token.bearerToken)
        } catch (e: Exception) {
            // Nothing because we should not hinder the user experience
        }
        TokenStore(this).removeToken()
    }
    UserObserver.remove()
}