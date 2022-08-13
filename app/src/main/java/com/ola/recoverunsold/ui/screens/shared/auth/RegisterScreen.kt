package com.ola.recoverunsold.ui.screens.shared.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.ui.components.app.AppHero
import com.ola.recoverunsold.ui.navigation.Routes

@Composable
fun RegisterScreen(navController: NavController, snackbarHostState: SnackbarHostState) {
    Scaffold(
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) { padding ->
        RegisterScreenContent(modifier = Modifier.padding(padding), navController)
    }
}

@OptIn(ExperimentalMaterialApi::class)
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

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            backgroundColor = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(30.dp),
            onClick = { navController.navigate(Routes.CustomerRegister.path) }
        ) {
            Text(
                stringResource(R.string.customer_status_declaration),
                modifier = Modifier.padding(vertical = 40.dp, horizontal = 20.dp),
                color = MaterialTheme.colors.onSecondary,
                fontSize = 19.sp
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp),
            backgroundColor = MaterialTheme.colors.secondary,
            shape = RoundedCornerShape(30.dp),
            onClick = { navController.navigate(Routes.DistributorRegister.path) }
        ) {
            Text(
                stringResource(R.string.distributor_status_declaration),
                modifier = Modifier.padding(vertical = 40.dp, horizontal = 20.dp),
                color = MaterialTheme.colors.onSecondary,
                fontSize = 19.sp
            )
        }
    }
}