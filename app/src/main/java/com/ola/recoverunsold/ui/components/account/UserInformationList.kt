package com.ola.recoverunsold.ui.components.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.models.User
import com.ola.recoverunsold.utils.misc.formatDate

/**
 * Component to show information about users
 *
 * Customer or Distributor, it shows only relevant data
 */
@Composable
fun UserInformationList(user: User, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserAccountHeader(
            id = user.createdAt.toString(),
            name = user.username,
            size = 90.dp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        AccountInformationTile(
            leadingIcon = {
                Icon(
                    Icons.Filled.AccountBox,
                    contentDescription = null,
                )
            },
            label = stringResource(id = R.string.username_label),
            data = user.username
        )

        if (user is Customer) {
            AccountInformationTile(
                leadingIcon = {
                    Icon(Icons.Filled.AccountBox, contentDescription = null)
                },
                label = stringResource(R.string.first_name_label),
                data = user.firstName ?: stringResource(R.string.n_a)
            )

            AccountInformationTile(
                leadingIcon = {
                    Icon(Icons.Filled.AccountBox, contentDescription = null)
                },
                label = stringResource(R.string.last_name_label),
                data = user.lastName ?: stringResource(R.string.n_a)
            )
        }

        AccountInformationTile(
            leadingIcon = {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = null,
                )
            },
            label = stringResource(id = R.string.email_label),
            data = user.email
        )

        if (user is Distributor) {
            AccountInformationTile(
                leadingIcon = {
                    Icon(Icons.Filled.Phone, contentDescription = null)
                },
                label = stringResource(id = R.string.phone_label),
                data = user.phone
            )

            AccountInformationTile(
                leadingIcon = {
                    Icon(Icons.Filled.Numbers, contentDescription = null)
                },
                label = stringResource(id = R.string.tax_id_label),
                data = user.taxId
            )

            AccountInformationTile(
                leadingIcon = {
                    Icon(Icons.Filled.Info, contentDescription = null)
                },
                label = stringResource(id = R.string.rccm_label),
                data = user.rccm
            )

            AccountInformationTile(
                leadingIcon = {
                    Icon(Icons.Filled.Language, contentDescription = null)
                },
                label = stringResource(id = R.string.website_url_label),
                data = user.websiteUrl ?: stringResource(R.string.n_a)
            )
        }

        AccountInformationTile(
            leadingIcon = {
                Icon(Icons.Filled.CalendarToday, contentDescription = null)
            },
            label = stringResource(id = R.string.member_since_label),
            data = user.createdAt.formatDate()
        )
    }
}