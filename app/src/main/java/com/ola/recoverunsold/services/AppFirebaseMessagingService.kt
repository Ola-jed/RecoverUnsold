package com.ola.recoverunsold.services

import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessagingService
import com.ola.recoverunsold.api.requests.FcmTokenCreateRequest
import com.ola.recoverunsold.api.services.FcmTokenService
import com.ola.recoverunsold.utils.store.TokenStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent


class AppFirebaseMessagingService : FirebaseMessagingService() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    /**
     * We send the token only if we have an api token to make an authenticated request
     * And if the device have google play services enabled because otherwise, they won't work
     */
    override fun onNewToken(token: String) {
        Log.d("AppFirebaseMessagingService", "New token generated : $token")
        super.onNewToken(token)
        val apiToken = TokenStore.get() ?: return
        val isGooglePlayAvailable =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (isGooglePlayAvailable != ConnectionResult.SUCCESS) return
        val fcmTokenService = KoinJavaComponent.get<FcmTokenService>(FcmTokenService::class.java)
        scope.launch {
            fcmTokenService.createFcmToken(
                apiToken.bearerToken,
                FcmTokenCreateRequest(token)
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}