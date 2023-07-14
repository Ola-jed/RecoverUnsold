package com.ola.recoverunsold.ui.components.offer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.ui.components.app.ItemDetailsLine
import com.ola.recoverunsold.utils.extensions.addSeconds
import com.ola.recoverunsold.utils.extensions.formatDate
import com.ola.recoverunsold.utils.extensions.formatDateTime
import com.ola.recoverunsold.utils.extensions.formatWithoutTrailingZeros

@Composable
fun OfferDetailsComponent(offer: Offer) {
    Card {
        Column {
            ItemDetailsLine(
                modifier = Modifier.padding(
                    top = 13.dp,
                    bottom = 13.dp,
                    start = 10.dp
                ),
                icon = Icons.Default.Payments,
                text = stringResource(
                    R.string.total_amount,
                    offer.price.formatWithoutTrailingZeros()
                )
            )
            Divider()
            if (offer.beneficiaries != null) {
                ItemDetailsLine(
                    modifier = Modifier.padding(
                        top = 13.dp,
                        bottom = 13.dp,
                        start = 10.dp
                    ),
                    icon = Icons.Default.Group,
                    text = stringResource(
                        R.string.offer_beneficiaries_data,
                        offer.beneficiaries
                    )
                )
                Divider()
            }
            ItemDetailsLine(
                modifier = Modifier.padding(
                    top = 13.dp,
                    bottom = 13.dp,
                    start = 10.dp
                ),
                icon = Icons.Default.Event,
                text = stringResource(
                    R.string.start_date_time,
                    offer.startDate.formatDateTime()
                )
            )
            Divider()
            ItemDetailsLine(
                modifier = Modifier.padding(
                    top = 13.dp,
                    bottom = 13.dp,
                    start = 10.dp
                ),
                icon = Icons.Default.EventBusy,
                text = stringResource(
                    R.string.end_date_time,
                    offer.startDate.addSeconds(offer.duration).formatDateTime()
                )
            )
            Divider()
            ItemDetailsLine(
                modifier = Modifier.padding(
                    top = 13.dp,
                    bottom = 13.dp,
                    start = 10.dp
                ),
                icon = Icons.Default.CalendarMonth,
                text = stringResource(
                    R.string.published_the,
                    offer.createdAt.formatDate()
                )
            )
            Divider()
            ItemDetailsLine(
                modifier = Modifier.padding(
                    top = 13.dp,
                    bottom = 13.dp,
                    start = 10.dp
                ),
                icon = Icons.Default.CreditCard,
                text = "${stringResource(R.string.online_payment)} : " + stringResource(
                    if (offer.onlinePayment) R.string.yes else R.string.no
                )
            )
        }
    }
}