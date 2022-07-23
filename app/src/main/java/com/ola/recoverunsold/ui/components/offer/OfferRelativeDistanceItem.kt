package com.ola.recoverunsold.ui.components.offer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.OfferWithRelativeDistance
import com.ola.recoverunsold.ui.components.app.ImageSlider
import com.ola.recoverunsold.utils.misc.formatDate
import com.ola.recoverunsold.utils.misc.formatDateTime
import androidx.compose.ui.platform.LocalConfiguration as LocalConfiguration1

@Composable
fun OfferRelativeDistanceItem(
    modifier: Modifier = Modifier,
    offerWithRelativeDistance: OfferWithRelativeDistance,
    onMoreInformationRequest: () -> Unit,
    onMapShowRequest: () -> Unit
) {
    val offer = offerWithRelativeDistance.offer
    val startDate = offer.startDate
    val productsImagesUris = offer.products?.flatMap { it.images }?.map { it.url }
    val height = (LocalConfiguration1.current.screenHeightDp * 0.2).dp

    Surface(
        modifier = modifier.clickable { onMoreInformationRequest() },
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            if (!productsImagesUris.isNullOrEmpty()) {
                ImageSlider(
                    modifier = Modifier.fillMaxWidth(),
                    imageHeight = height,
                    imageUris = productsImagesUris
                )
            }

            Text(
                text = stringResource(R.string.total_amount, offer.price.toString()),
                modifier = Modifier.padding(top = 10.dp)
            )

            if (offer.beneficiaries != null) {
                Text(
                    text = stringResource(
                        R.string.offer_beneficiaries_data,
                        offer.beneficiaries
                    )
                )
            }

            Text(
                text = stringResource(
                    R.string.start_date_time,
                    startDate.formatDateTime()
                )
            )

            Text(
                text = stringResource(
                    R.string.published_the,
                    offer.createdAt.formatDate()
                ),
                modifier = Modifier.padding(top = 5.dp)
            )


            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onMoreInformationRequest) {
                    Text(text = stringResource(id = R.string.view_more))
                }

                Button(onClick = onMapShowRequest) {
                    Text(text = stringResource(id = R.string.view_location_on_maps))
                }
            }
        }
    }
}