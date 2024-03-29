package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ola.recoverunsold.R

@Composable
fun ImageSlider(
    modifier: Modifier = Modifier,
    imageUris: List<String>,
    imageHeight: Dp? = null,
    rounded: Boolean = true
) {
    var currentIndex by rememberSaveable { mutableStateOf(0) }

    Box(
        modifier = modifier
    ) {
        var imageModifier = if (rounded) {
            Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .clip(RoundedCornerShape(10.dp))
        } else {
            Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        }

        if (imageHeight != null) {
            imageModifier = imageModifier.height(imageHeight)
        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUris[currentIndex])
                .placeholder(R.mipmap.placeholder)
                .crossfade(true)
                .build(),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = imageModifier
        )

        if (currentIndex > 0) {
            FloatingActionButton(
                modifier = Modifier.align(Alignment.CenterStart),
                containerColor = Color.White.copy(alpha = 0.65F),
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                onClick = { currentIndex-- }) {
                Icon(Icons.Filled.ArrowBackIos, contentDescription = null)
            }
        }

        if (currentIndex < imageUris.size - 1) {
            FloatingActionButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                containerColor = Color.White.copy(alpha = 0.65F),
                elevation = FloatingActionButtonDefaults.elevation(0.dp),
                onClick = { currentIndex++ }) {
                Icon(Icons.Filled.ArrowForwardIos, contentDescription = null)
            }
        }
    }
}