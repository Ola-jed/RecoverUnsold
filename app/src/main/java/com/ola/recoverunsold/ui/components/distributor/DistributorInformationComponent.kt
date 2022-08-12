package com.ola.recoverunsold.ui.components.distributor

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.ola.recoverunsold.R
import com.ola.recoverunsold.models.DistributorInformation
import com.ola.recoverunsold.utils.misc.formatDate

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DistributorInformationComponent(
    modifier: Modifier = Modifier,
    distributorInformation: DistributorInformation,
    onTap: (() -> Unit)? = null
) {
    val context = LocalContext.current

    Card(
        modifier = modifier,
        elevation = 10.dp,
        shape = RoundedCornerShape(20.dp),
        onClick = { onTap?.invoke() }
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            DistributorInformationLine(
                modifier = Modifier.padding(vertical = 5.dp),
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null) },
                data = distributorInformation.username
            )

            DistributorInformationLine(
                modifier = Modifier.padding(vertical = 5.dp),
                leadingIcon = { Icon(Icons.Filled.Email, contentDescription = null) },
                data = distributorInformation.email
            )

            DistributorInformationLine(
                modifier = Modifier.padding(vertical = 5.dp),
                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null) },
                data = distributorInformation.phone
            )


            var distributorWebsite = distributorInformation.websiteUrl

            if (distributorWebsite == null) {
                DistributorInformationLine(
                    modifier = Modifier.padding(vertical = 5.dp),
                    leadingIcon = {
                        if (distributorInformation.websiteUrl == null) {
                            Icon(Icons.Filled.Language, contentDescription = null)
                        } else {
                            Icon(
                                modifier = Modifier.clickable {
                                    val websiteUri = Uri.parse(distributorInformation.websiteUrl)
                                    val websiteIntent = Intent(Intent.ACTION_VIEW, websiteUri)
                                    startActivity(context, websiteIntent, null)
                                },
                                imageVector = Icons.Filled.Language,
                                contentDescription = null
                            )
                        }
                    },
                    data = stringResource(id = R.string.n_a)
                )
            } else {
                if (!distributorWebsite.startsWith("http://")
                    && !distributorWebsite.startsWith("https://")
                ) {
                    distributorWebsite = "http://$distributorWebsite"
                }

                val websiteLinkString = buildAnnotatedString {
                    append(distributorWebsite)

                    addStyle(
                        style = SpanStyle(
                            color = Color.Blue,
                            textDecoration = TextDecoration.Underline
                        ),
                        start = 0,
                        end = distributorWebsite.length
                    )

                    addStringAnnotation(
                        tag = "URL",
                        annotation = distributorWebsite,
                        start = 0,
                        end = distributorWebsite.length
                    )
                }

                val uriHandler = LocalUriHandler.current

                DistributorInformationLine(
                    modifier = Modifier.padding(vertical = 5.dp),
                    leadingIcon = { Icon(Icons.Filled.Language, contentDescription = null) },
                    text = {
                        ClickableText(
                            text = websiteLinkString,
                            onClick = { uriHandler.openUri(distributorWebsite) }
                        )
                    }
                )
            }

            DistributorInformationLine(
                modifier = Modifier.padding(vertical = 5.dp),
                leadingIcon = { Icon(Icons.Filled.CalendarToday, contentDescription = null) },
                label = stringResource(id = R.string.member_since_label),
                data = distributorInformation.createdAt.formatDate()
            )
        }
    }
}