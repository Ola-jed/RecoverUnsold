package com.ola.recoverunsold.ui.screens.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ola.recoverunsold.R
import com.ola.recoverunsold.api.core.ApiStatus
import com.ola.recoverunsold.ui.components.app.AppBar
import com.ola.recoverunsold.ui.components.app.LoadingIndicator
import com.ola.recoverunsold.ui.components.app.NoContentComponent
import com.ola.recoverunsold.ui.components.app.PaginationComponent
import com.ola.recoverunsold.ui.components.distributor.DistributorInformationComponent
import com.ola.recoverunsold.ui.components.drawer.DrawerContent
import com.ola.recoverunsold.ui.navigation.Routes
import com.ola.recoverunsold.ui.screens.viewmodels.DistributorsViewModel
import com.ola.recoverunsold.utils.misc.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

@Composable
fun DistributorsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    distributorsViewModel: DistributorsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                coroutineScope = coroutineScope,
                scaffoldState = scaffoldState,
                title = stringResource(id = R.string.distributors)
            )
        },
        drawerContent = DrawerContent(navController)
    ) { paddingValues ->
        when (distributorsViewModel.distributorsApiResult.status) {
            ApiStatus.LOADING -> LoadingIndicator()
            ApiStatus.ERROR -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    Button(
                        onClick = {
                            distributorsViewModel.resetFilter()
                            distributorsViewModel.getDistributors()
                        },
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(stringResource(id = R.string.retry))
                    }
                }

                LaunchedEffect(snackbarHostState) {
                    coroutineScope.launch {
                        snackbarHostState.show(
                            message = distributorsViewModel.errorMessage()
                                ?: Strings.get(R.string.unknown_error_occured)
                        )
                    }
                }
            }
            else -> {
                val distributors = distributorsViewModel.distributorsApiResult.data!!

                LazyColumn(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    item {
                        TextField(
                            modifier = Modifier
                                .fillParentMaxWidth(),
                            value = distributorsViewModel.distributorFilterQuery.name ?: "",
                            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                            onValueChange = {
                                distributorsViewModel.distributorFilterQuery = distributorsViewModel
                                    .distributorFilterQuery
                                    .copy(name = it)
                            },
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search,
                                keyboardType = KeyboardType.Text
                            ),
                            keyboardActions = KeyboardActions(onSearch = {
                                distributorsViewModel.getDistributors()
                            }),
                            singleLine = true
                        )
                    }

                    if (distributors.items.isEmpty()) {
                        item {
                            NoContentComponent(
                                modifier = Modifier.fillMaxWidth(),
                                message = stringResource(R.string.no_distributor_found)
                            )
                        }
                    } else {
                        items(items = distributors.items) { item ->
                            DistributorInformationComponent(
                                modifier = Modifier
                                    .fillParentMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 10.dp),
                                distributorInformation = item,
                                onTap = {
                                    navController.navigate(
                                        Routes.DistributorDetails
                                            .path
                                            .replace("{distributorId}", item.id)
                                    )
                                }
                            )
                        }

                        item {
                            PaginationComponent(
                                modifier = Modifier.fillMaxWidth(),
                                page = distributors,
                                onPrevious = { distributorsViewModel.getPrevious() },
                                onNext = { distributorsViewModel.getNext() }
                            )
                        }
                    }
                }
            }
        }
    }
}
