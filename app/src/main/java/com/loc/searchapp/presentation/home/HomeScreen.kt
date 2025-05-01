package com.loc.searchapp.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.auth.AuthState
import com.loc.searchapp.presentation.common.base.AuthViewModel
import com.loc.searchapp.presentation.common.base.ProductViewModel
import com.loc.searchapp.presentation.common.components.AppSnackbar
import com.loc.searchapp.presentation.common.components.ProductsList
import com.loc.searchapp.presentation.common.components.SearchBar
import com.loc.searchapp.presentation.home.components.HomeTopBar
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    authViewModel: AuthViewModel,
    productViewModel: ProductViewModel,
    products: LazyPagingItems<Product>,
    navigateToSearch: () -> Unit,
    navigateToDetails: (Product) -> Unit,
    onAuthClick: () -> Unit
) {
    val localCartChanges = productViewModel.localCartChanges.collectAsState().value
    val authState = authViewModel.authState.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val mustLoginMessage = stringResource(id = R.string.must_login)
    val loginActionLabel = stringResource(id = R.string.login)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = MediumPadding1, top = MediumPadding1, end = MediumPadding1)
        ) {
            HomeTopBar(
                viewModel = authViewModel
            )

            Spacer(modifier = Modifier.height(MediumPadding1))

            SearchBar(
                text = "",
                readOnly = false,
                onValueChange = {},
                onClick = { navigateToSearch() },
                onSearch = {},
                placeholder = stringResource(id = R.string.search_placeholder)
            )

            Spacer(modifier = Modifier.height(MediumPadding1))

//            ProductsList(
//                modifier = Modifier.padding(horizontal = MediumPadding1),
//                items = products,
//                onClick = { product -> navigateToDetails(product) },
//                onAdd = { product ->
//                    if (authState !is AuthState.Authenticated) {
//                        scope.launch {
//                            val result = snackbarHostState.showSnackbar(
//                                message = mustLoginMessage,
//                                actionLabel = loginActionLabel,
//                                duration = SnackbarDuration.Short
//                            )
//                            if (result == SnackbarResult.ActionPerformed) {
//                                onAuthClick()
//                            }
//                        }
//                    } else {
//                        productViewModel.addToCart(product.code)
//                    }
//                },
//                onRemove = { product -> productViewModel.removeFromCart(product.code) },
//                localCartChanges = localCartChanges,
//                showShimmerOnFirstLoad = true,
//            )

            Text(text = "З 1966 року ПрАТ «ПОЛТАВСЬКИЙ АЛМАЗНИЙ ІНСТРУМЕНТ» продовжує традиції якості та інновацій у виробництві алмазного та CBN інструменту. Наша компанія пройшла шлях від розробника перших алмазних порошків у СРСР до сучасного підприємства, що виготовляє складні рішення для промисловості по всьому світу.") // translate

            Spacer(modifier = Modifier.height(MediumPadding1))


        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            snackbar = { snackbarData ->
                AppSnackbar(
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier
                )
            }
        )
    }
}