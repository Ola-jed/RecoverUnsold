package com.ola.recoverunsold.ui.components.distributor

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Web
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.DistributorInformation
import com.ola.recoverunsold.utils.misc.formatDate
import java.util.Date

@Composable
fun DistributorInformationComponent(
    modifier: Modifier = Modifier,
    distributorInformation: DistributorInformation
) {
    Surface(modifier = modifier) {
        Column {
            DistributorInformationLine(
                leadingIcon = {
                    Icon(
                        Icons.Filled.AccountBox,
                        contentDescription = null,
                    )
                },
                label = stringResource(id = R.string.username_label),
                data = distributorInformation.username
            )

            DistributorInformationLine(
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = null,
                    )
                },
                label = stringResource(id = R.string.email_label),
                data = distributorInformation.email
            )

            DistributorInformationLine(
                leadingIcon = {
                    Icon(Icons.Filled.Phone, contentDescription = null)
                },
                label = stringResource(id = R.string.phone_label),
                data = distributorInformation.phone
            )

            if (distributorInformation.websiteUrl != null) {
                DistributorInformationLine(
                    leadingIcon = {
                        Icon(Icons.Filled.Web, contentDescription = null)
                    },
                    label = stringResource(id = R.string.website_url_label),
                    data = distributorInformation.websiteUrl
                )
            }

            DistributorInformationLine(
                leadingIcon = {
                    Icon(Icons.Filled.CalendarToday, contentDescription = null)
                },
                label = stringResource(id = R.string.member_since_label),
                data = distributorInformation.createdAt.formatDate()
            )
        }
    }
}

@Preview
@Composable
fun Preview() {
    DistributorInformationComponent(
        distributorInformation = DistributorInformation(
            id = "XXX",
            username = "Best Distribution",
            email = "best_distribution@mail.com",
            phone = "+229657678998",
            websiteUrl = "ola.com",
            createdAt = Date()
        )
    )
}