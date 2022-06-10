package com.ola.recoverunsold.utils.store

import android.content.Context
import androidx.datastore.core.DataStore
import com.google.protobuf.Timestamp
import com.ola.recoverunsold.Token
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TokenStore(context: Context) {
    private val dataStore: DataStore<Token> = context.tokenDataStore

    fun token(): Flow<Token> = dataStore.data.map { it }

    suspend fun storeToken(apiToken: com.ola.recoverunsold.api.responses.Token) {
        dataStore.updateData { token ->
            token.toBuilder()
                .setRole(apiToken.role)
                .setToken(apiToken.token)
                .setExpirationDate(
                    Timestamp.newBuilder()
                        .setSeconds(apiToken.expirationDate.time)
                ).build()
        }
    }
}