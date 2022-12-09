package com.ola.recoverunsold.services

import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessagingService
import com.ola.recoverunsold.api.requests.FcmTokenCreateRequest
import com.ola.recoverunsold.api.services.FcmTokenService
import com.ola.recoverunsold.utils.store.TokenStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppFirebaseMessagingService : FirebaseMessagingService() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var fcmTokenService: FcmTokenService

    /**
     * We send the token only if we have an api token to make an authenticated request
     * If a token is generated and the user is not logged yet, we send it later
     * And if the device have google play services enabled because otherwise, they won't work
     * @param token The new fcm token generated
     */
    override fun onNewToken(token: String) {
        Log.d("AppFirebaseMessagingService", "New token generated : $token")
        val apiToken = TokenStore.get()
        val isGooglePlayAvailable = GoogleApiAvailability
            .getInstance()
            .isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS

        if (apiToken == null || !isGooglePlayAvailable) {
            Log.e(
                "AppFirebaseMessagingService",
                "Api token is null or no google play services available"
            )
            return
        }
        scope.launch {
            try {
                fcmTokenService.createFcmToken(FcmTokenCreateRequest(token))
                super.onNewToken(token)
            } catch (e: Exception) {
                Log.d("AppFirebaseMessagingService", "Token sending failed : ${e.message}")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}