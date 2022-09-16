package com.ola.recoverunsold.ui.components.app

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ola.recoverunsold.R

@Composable
fun NoContentComponent(modifier: Modifier = Modifier, message: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.no_data))
    val progress by animateLottieCompositionAsState(composition)

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        LottieAnimation(composition = composition, progress = { progress })

        Text(text = message, color = MaterialTheme.colors.onBackground)
    }
}