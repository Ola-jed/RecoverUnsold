package com.ola.recoverunsold.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Web
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Distributor
import java.text.DateFormat
import java.util.Locale

@Composable
fun DistributorInformationList(distributor: Distributor, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserAccountHeader(
            id = distributor.createdAt.toString(),
            name = distributor.username,
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
            data = distributor.username
        )

        AccountInformationTile(
            leadingIcon = {
                Icon(
                    Icons.Filled.Email,
                    contentDescription = null,
                )
            },
            label = stringResource(id = R.string.email_label),
            data = distributor.email
        )

        AccountInformationTile(
            leadingIcon = {
                Icon(Icons.Filled.Phone, contentDescription = null)
            },
            label = stringResource(id = R.string.phone_label),
            data = distributor.phone
        )

        AccountInformationTile(
            leadingIcon = {
                Icon(Icons.Filled.Numbers, contentDescription = null)
            },
            label = stringResource(id = R.string.tax_id_label),
            data = distributor.taxId
        )

        AccountInformationTile(
            leadingIcon = {
                Icon(Icons.Filled.Info, contentDescription = null)
            },
            label = stringResource(id = R.string.rccm_label),
            data = distributor.rccm
        )

        AccountInformationTile(
            leadingIcon = {
                Icon(Icons.Filled.Web, contentDescription = null)
            },
            label = stringResource(id = R.string.website_url_label),
            data = distributor.websiteUrl ?: stringResource(R.string.n_a)
        )

        AccountInformationTile(
            leadingIcon = {
                Icon(Icons.Filled.CalendarToday, contentDescription = null)
            },
            label = stringResource(id = R.string.member_since_label),
            data = DateFormat
                .getDateInstance(DateFormat.DEFAULT, Locale.getDefault())
                .format(distributor.createdAt)
        )
    }
}