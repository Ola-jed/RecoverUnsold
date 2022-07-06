package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.components.app.AppHero
import com.ola.recoverunsold.ui.components.app.FlippedCard
import com.ola.recoverunsold.ui.navigation.Routes

@Composable
fun RegisterScreen(navController: NavController, snackbarHostState: SnackbarHostState) {
    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) { padding ->
        RegisterScreenContent(modifier = Modifier.padding(padding), navController)
    }
}

@Composable
fun RegisterScreenContent(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppHero(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 30.dp),
            text = stringResource(R.string.who_are_you)
        )

        FlippedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .clickable { navController.navigate(Routes.CustomerRegister.path) },
            color = MaterialTheme.colors.secondary,
        ) {
            Text(
                stringResource(R.string.customer_status_declaration),
                Modifier.padding(30.dp),
                color = MaterialTheme.colors.onSecondary
            )
        }

        FlippedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .clickable { navController.navigate(Routes.DistributorRegister.path) },
            color = MaterialTheme.colors.secondary,
        ) {
            Text(
                stringResource(R.string.distributor_status_declaration),
                Modifier.padding(30.dp),
                color = MaterialTheme.colors.onSecondary
            )
        }
    }
}