package com.ola.recoverunsold.utils.store

import android.content.Context
import androidx.datastore.core.DataStore
import com.ola.recoverunsold.AppUser
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Locale

class AppUserStore(context: Context) {
    private val dataStore: DataStore<AppUser> = context.appUserDataStore

    fun user(): Flow<User> {
        return dataStore.data.map {
            when (it.status) {
                AppUser.Status.CUSTOMER -> it.toCustomer()
                else -> it.toDistributor()
            }
        }
    }

    suspend fun storeUser(user: User) {
        dataStore.updateData { appUser ->
            when (user) {
                is Customer -> appUser.toBuilder()
                    .setStatus(AppUser.Status.CUSTOMER)
                    .setUsername(user.username)
                    .setEmail(user.email)
                    .setFirstName(user.firstName ?: "")
                    .setLastName(user.lastName ?: "")
                    .setEmailVerifiedAt(user.emailVerifiedAt?.let {
                        SimpleDateFormat(
                            "dd-MMM-yyyy",
                            Locale.getDefault()
                        ).format(it)
                    } ?: "")
                    .setCreatedAt(
                        SimpleDateFormat(
                            "dd-MMM-yyyy",
                            Locale.getDefault()
                        ).format(user.createdAt)
                    )
                    .build()
                is Distributor -> appUser.toBuilder()
                    .setStatus(AppUser.Status.DISTRIBUTOR)
                    .setUsername(user.username)
                    .setEmail(user.email)
                    .setPhone(user.phone)
                    .setTaxId(user.taxId)
                    .setRccm(user.rccm)
                    .setWebsiteUrl(user.websiteUrl)
                    .setEmailVerifiedAt(user.emailVerifiedAt?.let {
                        SimpleDateFormat(
                            "dd-MMM-yyyy",
                            Locale.getDefault()
                        ).format(it)
                    } ?: "")
                    .setCreatedAt(
                        SimpleDateFormat(
                            "dd-MMM-yyyy",
                            Locale.getDefault()
                        ).format(user.createdAt)
                    )
                    .build()
            }
        }
    }

    suspend fun removeUser() {
        dataStore.updateData { it.toBuilder().clear().build() }
    }

    companion object {
        lateinit var user: User

        fun hasUser(): Boolean = ::user.isInitialized

        fun init(producer: () -> User) {
            user = producer()
        }

        fun get(): User? = if (::user.isInitialized) {
            user
        } else {
            null
        }

        fun getOr(producer: () -> User): User {
            return if (::user.isInitialized) {
                user
            } else {
                user = producer()
                user
            }
        }
    }
}

fun AppUser.toCustomer(): Customer {
    return Customer(
        username = this.username,
        email = this.email,
        firstName = this.firstName.ifBlank { null },
        lastName = this.lastName.ifBlank { null },
        emailVerifiedAt = SimpleDateFormat(
            "dd-MMM-yyyy",
            Locale.getDefault()
        ).parse(this.emailVerifiedAt),
        createdAt = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(this.createdAt)!!,
    )
}

fun AppUser.toDistributor(): Distributor {
    return Distributor(
        username = this.username,
        email = this.email,
        phone = this.phone,
        taxId = this.taxId,
        rccm = this.rccm,
        websiteUrl = this.websiteUrl.ifBlank { null },
        emailVerifiedAt = SimpleDateFormat(
            "dd-MMM-yyyy",
            Locale.getDefault()
        ).parse(this.emailVerifiedAt),
        createdAt = SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).parse(this.createdAt)!!,
    )
}