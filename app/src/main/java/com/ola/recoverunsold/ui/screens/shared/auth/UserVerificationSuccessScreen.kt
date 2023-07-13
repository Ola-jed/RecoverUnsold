package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.navigation.Routes

@Composable
fun UserVerificationSuccessScreen(navController: NavController) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier.padding(16.dp),
                shadowElevation = 100.dp,
                tonalElevation = 100.dp,
                shape = RoundedCornerShape(size = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.check))
                    val progress by animateLottieCompositionAsState(composition)

                    Text(
                        stringResource(R.string.account_verified_successfully),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Text(
                        stringResource(R.string.account_verified_successfully_full_message),
                        modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    LottieAnimation(composition = composition, progress = { progress })

                    Button(onClick = { navController.navigate(Routes.Login.path) }) {
                        Text(
                            stringResource(R.string.navigate_to_login),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
        }
    }
}