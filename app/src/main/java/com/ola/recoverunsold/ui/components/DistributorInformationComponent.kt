package com.ola.recoverunsold.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Distributor
import java.text.DateFormat
import java.util.Locale

@Composable
fun DistributorInformationComponent(distributor: Distributor, modifier: Modifier = Modifier) {
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(id = R.string.username_label),
                textDecoration = TextDecoration.Underline
            )
            Text(distributor.username)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(id = R.string.email_label),
                textDecoration = TextDecoration.Underline
            )
            Text(distributor.email)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(id = R.string.phone_label),
                textDecoration = TextDecoration.Underline
            )
            Text(distributor.phone)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(id = R.string.tax_id_label),
                textDecoration = TextDecoration.Underline
            )
            Text(distributor.taxId)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(id = R.string.rccm_label),
                textDecoration = TextDecoration.Underline
            )
            Text(distributor.rccm)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(id = R.string.website_url_label),
                textDecoration = TextDecoration.Underline
            )
            Text(distributor.websiteUrl ?: stringResource(R.string.n_a))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(id = R.string.member_since_label),
                textDecoration = TextDecoration.Underline
            )
            Text(
                DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault())
                    .format(distributor.createdAt)
            )
        }
    }
}