package com.ola.recoverunsold.ui.screens.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.ola.recoverunsold.utils.extensions.show
import com.ola.recoverunsold.utils.resources.Strings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DistributorsScreen(
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    distributorsViewModel: DistributorsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { DrawerContent(navController) },
        content = {
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                topBar = {
                    AppBar(
                        coroutineScope = coroutineScope,
                        drawerState = drawerState,
                        title = stringResource(id = R.string.distributors)
                    )
                }
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

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            SearchBar(
                                modifier = Modifier
                                    .align(Alignment.TopCenter),
                                query = distributorsViewModel.distributorFilterQuery.name ?: "",
                                onQueryChange = {
                                    distributorsViewModel.distributorFilterQuery =
                                        distributorsViewModel
                                            .distributorFilterQuery
                                            .copy(name = it)
                                },
                                onSearch = { distributorsViewModel.getDistributors() },
                                active = distributorsViewModel.isSearching,
                                onActiveChange = { distributorsViewModel.isSearching = it },
                                placeholder = { Text(text = stringResource(id = R.string.search)) },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Search,
                                        contentDescription = null
                                    )
                                },
                            ) {
                                LazyColumn(
                                    modifier = Modifier
                                        .padding(paddingValues)
                                        .fillMaxSize()
                                ) {
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
                                                    .padding(
                                                        horizontal = 10.dp,
                                                        vertical = 10.dp
                                                    ),
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
                                                onLoadMore = { distributorsViewModel.loadMore() }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
