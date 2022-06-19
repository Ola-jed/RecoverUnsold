package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.navigation.Routes

@Composable
fun UserVerificationSuccessScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.padding(16.dp),
                elevation = 100.dp,
                shape = RoundedCornerShape(size = 20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val painter = painterResource(id = R.drawable.check)
                    Text(
                        stringResource(R.string.account_verified_successfully),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h5,
                        color = MaterialTheme.colors.secondary
                    )
                    Text(
                        stringResource(R.string.account_verified_successfully_full_message),
                        modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                        textAlign = TextAlign.Center,
                    )
                    Image(
                        modifier = Modifier
                            .defaultMinSize(minWidth = 100.dp, minHeight = 100.dp)
                            .padding(bottom = 15.dp),
                        painter = painter,
                        contentDescription = null
                    )
                    Button(onClick = {
                        navController.navigate(Routes.Login.path)
                    }) {
                        Text(
                            stringResource(R.string.navigate_to_login),
                            style = MaterialTheme.typography.h6
                        )
                    }
                }
            }
        }
    }
}