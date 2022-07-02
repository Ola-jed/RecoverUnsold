package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ola.recoverunsold.R

@Composable
fun ImageSlider(
    modifier: Modifier = Modifier,
    imageUris: List<String>
) {
    var currentIndex by rememberSaveable { mutableStateOf(0) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = { currentIndex-- }, enabled = currentIndex > 0) {
            Icon(Icons.Filled.ArrowBackIos, contentDescription = null)
        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUris[currentIndex])
                .placeholder(R.mipmap.placeholder)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.7F)
                .padding(end = 5.dp)
                .clip(RoundedCornerShape(10.dp)),
        )

        IconButton(onClick = { currentIndex++ }, enabled = currentIndex < imageUris.size - 1) {
            Icon(Icons.Filled.ArrowForwardIos, contentDescription = null)
        }
    }
}