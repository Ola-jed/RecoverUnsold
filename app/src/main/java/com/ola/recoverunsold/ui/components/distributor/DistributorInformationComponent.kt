package com.ola.recoverunsold.ui.components.distributor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.DistributorInformation
import com.ola.recoverunsold.utils.misc.formatDate

@Composable
fun DistributorInformationComponent(
    modifier: Modifier = Modifier,
    distributorInformation: DistributorInformation,
    onTap: () -> Unit
) {
    Surface(
        modifier = modifier.clickable { onTap() },
        elevation = 10.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            DistributorInformationLine(
                modifier = Modifier.padding(vertical = 5.dp),
                leadingIcon = {
                    Icon(
                        Icons.Filled.AccountBox,
                        contentDescription = null,
                    )
                },
                data = distributorInformation.username
            )

            DistributorInformationLine(
                modifier = Modifier.padding(vertical = 5.dp),
                leadingIcon = {
                    Icon(
                        Icons.Filled.Email,
                        contentDescription = null,
                    )
                },
                data = distributorInformation.email
            )

            DistributorInformationLine(
                modifier = Modifier.padding(vertical = 5.dp),
                leadingIcon = {
                    Icon(Icons.Filled.Phone, contentDescription = null)
                },
                data = distributorInformation.phone
            )

            if (distributorInformation.websiteUrl != null) {
                DistributorInformationLine(
                    modifier = Modifier.padding(vertical = 5.dp),
                    leadingIcon = {
                        Icon(Icons.Filled.Web, contentDescription = null)
                    },
                    label = stringResource(id = R.string.website_url_label),
                    data = distributorInformation.websiteUrl
                )
            }

            DistributorInformationLine(
                modifier = Modifier.padding(vertical = 5.dp),
                leadingIcon = {
                    Icon(Icons.Filled.CalendarToday, contentDescription = null)
                },
                label = stringResource(id = R.string.member_since_label),
                data = distributorInformation.createdAt.formatDate()
            )
        }
    }
}