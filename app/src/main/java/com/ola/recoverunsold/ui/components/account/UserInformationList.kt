package com.ola.recoverunsold.ui.components.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Customer
import com.ola.recoverunsold.models.Distributor
import com.ola.recoverunsold.models.User
import com.ola.recoverunsold.ui.components.app.ItemDetailsLine
import com.ola.recoverunsold.utils.extensions.formatDate

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
            .padding(horizontal = 10.dp, vertical = 10.dp)
    ) {
        UserAccountHeader(
            email = user.email,
            size = 90.dp,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
        )

        Card {
            ItemDetailsLine(
                modifier = Modifier.padding(
                    top = 13.dp,
                    bottom = 13.dp,
                    start = 10.dp
                ),
                icon = Icons.Filled.AccountBox,
                text = "${stringResource(id = R.string.username_label)} : ${user.username}"
            )

            Divider()

            if (user is Customer) {
                ItemDetailsLine(
                    modifier = Modifier.padding(
                        top = 13.dp,
                        bottom = 13.dp,
                        start = 10.dp
                    ),
                    icon = Icons.Filled.AccountBox,
                    text = "${stringResource(R.string.first_name_label)} : ${
                        user.firstName ?: stringResource(
                            R.string.n_a
                        )
                    }"
                )

                Divider()

                ItemDetailsLine(
                    modifier = Modifier.padding(
                        top = 13.dp,
                        bottom = 13.dp,
                        start = 10.dp
                    ),
                    icon = Icons.Filled.AccountBox,
                    text = "${stringResource(R.string.last_name_label)} : ${
                        user.lastName ?: stringResource(
                            R.string.n_a
                        )
                    }"
                )

                Divider()
            }

            ItemDetailsLine(
                modifier = Modifier.padding(
                    top = 13.dp,
                    bottom = 13.dp,
                    start = 10.dp
                ),
                icon = Icons.Filled.Email,
                text = "${stringResource(id = R.string.email_label)} : ${user.email}"
            )

            Divider()

            if (user is Distributor) {
                ItemDetailsLine(
                    modifier = Modifier.padding(
                        top = 13.dp,
                        bottom = 13.dp,
                        start = 10.dp
                    ),
                    icon = Icons.Filled.Phone,
                    text = "${stringResource(id = R.string.phone_label)} : ${user.phone}"
                )

                Divider()

                ItemDetailsLine(
                    modifier = Modifier.padding(
                        top = 13.dp,
                        bottom = 13.dp,
                        start = 10.dp
                    ),
                    icon = Icons.Filled.Numbers,
                    text = "${stringResource(id = R.string.tax_id_label)} : ${user.taxId}"
                )

                Divider()

                ItemDetailsLine(
                    modifier = Modifier.padding(
                        top = 13.dp,
                        bottom = 13.dp,
                        start = 10.dp
                    ),
                    icon = Icons.Filled.Info,
                    text = "${stringResource(id = R.string.rccm_label)} : ${user.rccm}"
                )

                Divider()

                ItemDetailsLine(
                    modifier = Modifier.padding(
                        top = 13.dp,
                        bottom = 13.dp,
                        start = 10.dp
                    ),
                    icon = Icons.Filled.Language,
                    text = "${stringResource(id = R.string.website_url_label)} : ${
                        (user.websiteUrl ?: stringResource(
                            R.string.n_a
                        ))
                    }"
                )

                Divider()
            }

            ItemDetailsLine(
                modifier = Modifier.padding(
                    top = 13.dp,
                    bottom = 13.dp,
                    start = 10.dp
                ),
                icon = Icons.Filled.CalendarToday,
                text = "${stringResource(id = R.string.member_since_label)} : ${user.createdAt.formatDate()}"
            )
        }
    }
}