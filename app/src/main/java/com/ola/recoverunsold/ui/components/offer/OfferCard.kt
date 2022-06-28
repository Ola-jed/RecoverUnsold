package com.ola.recoverunsold.ui.components.offer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Offer
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OfferCard(
    modifier: Modifier = Modifier,
    offer: Offer,
    isEditable: Boolean = false,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    Surface(
        modifier = modifier,
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        val dateTimeFormatter = DateFormat.getDateTimeInstance()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.total_amount)
                        .format(offer.price)
                )
                if (offer.beneficiaries != null) {
                    Text(
                        text = stringResource(id = R.string.offer_beneficiaries_data)
                            .format(offer.beneficiaries)
                    )
                }
            }
            val startDate = offer.startDate
            val endDate = Date.from(startDate.toInstant().plusSeconds(offer.duration.toLong()))

            Text(
                text = stringResource(id = R.string.duration)
                    .format(dateTimeFormatter.format(startDate), dateTimeFormatter.format(endDate))
            )

            Text(
                text = stringResource(R.string.published_the)
                    .format(
                        DateFormat
                            .getDateInstance(DateFormat.DEFAULT, Locale.getDefault())
                            .format(offer.createdAt)
                    ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOfferCard() {
    OfferCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        offer = Offer(
            id = "",
            startDate = Date(),
            duration = 12225UL,
            beneficiaries = 4,
            price = 145.0,
            createdAt = Date(),
            location = null,
            products = null
        )
    )
}