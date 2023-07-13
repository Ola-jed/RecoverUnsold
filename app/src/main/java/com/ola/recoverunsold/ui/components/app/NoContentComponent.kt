package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ola.recoverunsold.R

@Composable
fun NoContentComponent(modifier: Modifier = Modifier, message: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_data))
    val progress by animateLottieCompositionAsState(composition)
    val height = (LocalConfiguration.current.screenHeightDp * 0.33).dp

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.height(height),
            alignment = Alignment.Center,
            contentScale = ContentScale.Fit
        )

        Text(
            text = message,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )
    }
}