package com.ola.recoverunsold.ui.components.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Location
import com.ola.recoverunsold.models.Offer
import com.ola.recoverunsold.models.Order
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.models.Product
import com.ola.recoverunsold.models.ProductImage
import com.ola.recoverunsold.utils.misc.formatDate
import com.ola.recoverunsold.utils.misc.formatDateTime
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros
import com.ola.recoverunsold.utils.misc.internationalizedValue
import com.ola.recoverunsold.utils.misc.toIcon
import java.util.Date

@Composable
fun OrderItem(
    modifier: Modifier = Modifier,
    order: Order,
    onMoreInformationRequest: () -> Unit
) {
    val offer = order.offer!!

    Surface(
        modifier = modifier,
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = order.status.toIcon(), contentDescription = null)
                Text(text = order.status.internationalizedValue())
            }

            Text(
                text = stringResource(
                    R.string.total_amount,
                    offer.price.formatWithoutTrailingZeros()
                ),
                modifier = Modifier.padding(top = 10.dp)
            )

            Text(
                text = "${offer.startDate.formatDateTime()} - ${
                    Date.from(
                        offer.startDate.toInstant().plusSeconds(offer.duration.toLong())
                    ).formatDateTime()
                }"
            )

            Text(
                text = stringResource(
                    R.string.published_the,
                    offer.createdAt.formatDate()
                )
            )

            Button(onClick = onMoreInformationRequest) {
                Text(text = stringResource(id = R.string.view_more))
            }

            Text(
                text = stringResource(R.string.ordered_on, order.createdAt.formatDate()),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

@Preview
@Composable
fun preview() {
    OrderItem(
        order = Order(
            id = "1234",
            status = OrderStatus.Rejected,
            customerId = "123",
            offerId = "1234",
            offer = Offer(
                id = "1234",
                startDate = Date(),
                duration = 1455UL,
                beneficiaries = 4,
                price = 4500.0,
                createdAt = Date(),
                distributorId = "123",
                location = Location.Dummy,
                products = listOf(
                    Product(
                        id = "123",
                        name = "Produit 1",
                        description = "Description du produit 1",
                        offerId = "123",
                        createdAt = Date(),
                        images = listOf(ProductImage(""))
                    )
                )
            ),
            createdAt = Date(),
        ),
        onMoreInformationRequest = {}
    )
}