package com.ola.recoverunsold.ui.components.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ola.recoverunsold.R
import com.ola.recoverunsold.utils.misc.md5

@Composable
fun UserAccountHeader(
    email: String,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
) {
    Box(modifier.size(size), contentAlignment = Alignment.Center) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("http://gravatar.com/avatar/${email.md5()}")
                .placeholder(R.mipmap.placeholder)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .height(size)
                .clip(CircleShape)
        )
    }
}