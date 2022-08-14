package com.ola.recoverunsold.ui.components.order

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.Opinion
import com.ola.recoverunsold.models.Order
import com.ola.recoverunsold.models.OrderStatus
import com.ola.recoverunsold.ui.components.opinion.OpinionItem
import com.ola.recoverunsold.utils.misc.formatDate
import com.ola.recoverunsold.utils.misc.formatDateTime
import com.ola.recoverunsold.utils.misc.formatWithoutTrailingZeros
import com.ola.recoverunsold.utils.misc.internationalizedValueSingular
import com.ola.recoverunsold.utils.misc.toIcon
import java.util.Date

@Composable
fun OrderItem(
    modifier: Modifier = Modifier,
    order: Order,
    onMoreInformationRequest: () -> Unit,
    canPublish: Boolean = false,
    onCommentPublishRequest: () -> Unit = {},
    onOpinionDelete: (Opinion) -> Unit = {}
) {
    val offer = order.offer!!
    var showCommentsSection by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier,
        elevation = 10.dp,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Surface(
                elevation = 15.dp,
                color = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(30.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 7.5.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = order.status.toIcon(),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSecondary
                    )
                    Text(
                        modifier = Modifier.padding(start = 3.dp),
                        text = order.status.internationalizedValueSingular(),
                        color = MaterialTheme.colors.onSecondary
                    )
                }
            }

            Text(
                text = stringResource(
                    R.string.total_amount,
                    offer.price.formatWithoutTrailingZeros()
                ),
                modifier = Modifier.padding(top = 10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            Text(
                modifier = Modifier.padding(vertical = 5.dp),
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

            Text(
                text = stringResource(R.string.ordered_on, order.createdAt.formatDate()),
                modifier = Modifier.padding(top = 10.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = "${stringResource(id = R.string.to_be_picked_up_on)} : ${order.withdrawalDate.formatDateTime()}",
                fontWeight = FontWeight.Bold
            )

            IconButton(
                modifier = Modifier.align(Alignment.End),
                onClick = { showCommentsSection = !showCommentsSection }
            ) {
                Icon(
                    imageVector = if (showCommentsSection) {
                        Icons.Default.ExpandLess
                    } else {
                        Icons.Default.ExpandMore
                    },
                    contentDescription = null
                )
            }

            if (showCommentsSection) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    order.opinions.map {
                        OpinionItem(
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .fillMaxWidth(),
                            opinion = it,
                            canDelete = true,
                            onDelete = { onOpinionDelete(it) }
                        )
                    }.ifEmpty {
                        Text(stringResource(id = R.string.no_comments_yet))
                    }

                    if (canPublish && order.status == OrderStatus.Completed) {
                        IconButton(
                            modifier = Modifier.align(Alignment.End),
                            onClick = onCommentPublishRequest
                        ) {
                            Icon(imageVector = Icons.Default.AddComment, contentDescription = null)
                        }
                    }
                }
            }

            Button(onClick = onMoreInformationRequest, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(id = R.string.view_more_about_offer))
            }
        }
    }
}