package com.ola.recoverunsold.utils.store

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import com.ola.recoverunsold.AppUser
import java.io.InputStream
import java.io.OutputStream

object AppUserSerializer : Serializer<AppUser> {
    override val defaultValue: AppUser = AppUser.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): AppUser {
        try {
            return AppUser.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: AppUser, output: OutputStream) = t.writeTo(output)
}

val Context.appUserDataStore: DataStore<AppUser> by dataStore(
    fileName = "appUsers.pd",
    serializer = AppUserSerializer
)