package com.ola.recoverunsold.ui.components.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Order
import com.ola.recoverunsold.utils.misc.backgroundColor
import com.ola.recoverunsold.utils.misc.foregroundColor
import com.ola.recoverunsold.utils.misc.formatDate
import com.ola.recoverunsold.utils.misc.formatDateTime
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros
import com.ola.recoverunsold.utils.misc.internationalizedValueSingular
import com.ola.recoverunsold.utils.misc.toIcon
import java.util.Date

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderItem(
    modifier: Modifier = Modifier,
    order: Order,
    onTap: () -> Unit
) {
    val offer = order.offer!!

    Card(
        modifier = modifier,
        elevation = 10.dp,
        onClick = onTap
    ) {
        Column {
            Surface(
                elevation = 15.dp,
                color = order.status.backgroundColor()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 7.5.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = order.status.toIcon(),
                        contentDescription = null,
                        tint = order.status.foregroundColor()
                    )

                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = order.status.internationalizedValueSingular(),
                        color = order.status.foregroundColor()
                    )
                }
            }

            Text(
                text = stringResource(
                    R.string.total_amount,
                    offer.price.formatWithoutTrailingZeros()
                ),
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            Text(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                text = "${offer.startDate.formatDateTime()} - ${
                    Date.from(
                        offer.startDate.toInstant().plusSeconds(offer.duration.toLong())
                    ).formatDateTime()
                }"
            )

            Text(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                text = stringResource(
                    R.string.published_the,
                    offer.createdAt.formatDate()
                )
            )

            Text(
                text = stringResource(R.string.ordered_on, order.createdAt.formatDate()),
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            Text(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 10.dp),
                text = "${stringResource(id = R.string.to_be_picked_up_on)} : ${order.withdrawalDate.formatDateTime()}",
                fontWeight = FontWeight.Bold
            )
        }
    }
}