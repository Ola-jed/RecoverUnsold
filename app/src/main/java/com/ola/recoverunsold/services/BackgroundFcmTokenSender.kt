package com.ola.recoverunsold.services

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import com.ola.recoverunsold.api.requests.FcmTokenCreateRequest
import com.ola.recoverunsold.api.services.FcmTokenService
import com.ola.recoverunsold.utils.store.TokenStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

/**
 * Send a fcm token to the backend later in the app lifecycle
 * For the use case when a token is generated but the user is not logged yet, so wait for the token
 */
class BackgroundFcmTokenSender : JobService() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onStartJob(p0: JobParameters?): Boolean {
        if (fcmTokenToSend == null) return false
        val apiToken = TokenStore.get() ?: return false
        val fcmTokenService = KoinJavaComponent.get<FcmTokenService>(FcmTokenService::class.java)
        scope.launch {
            try {
                val response = fcmTokenService.createFcmToken(
                    apiToken.bearerToken,
                    FcmTokenCreateRequest(fcmTokenToSend!!)
                )
                if (response.isSuccessful) fcmTokenToSend = null
            } catch (e: Exception) {
                Log.d("BackgroundFcmTokenSender", "Token sending failed : ${e.message}")
            }
        }
        return false
    }

    override fun onStopJob(p0: JobParameters?): Boolean = false

    companion object {
        var fcmTokenToSend: String? = null
    }
}