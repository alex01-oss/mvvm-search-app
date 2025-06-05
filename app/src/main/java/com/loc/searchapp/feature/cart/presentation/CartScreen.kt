package com.loc.searchapp.feature.cart.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.core.domain.model.catalog.CartItem
import com.loc.searchapp.core.ui.components.common.SharedTopBar
import com.loc.searchapp.core.ui.components.lists.CartList
import com.loc.searchapp.core.ui.components.loading.LoadingScreen
import com.loc.searchapp.core.ui.values.Dimens.IconSize
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.feature.shared.components.GuestUser
import com.loc.searchapp.feature.shared.model.AuthState
import com.loc.searchapp.feature.shared.viewmodel.AuthViewModel
import com.loc.searchapp.feature.shared.viewmodel.ProductViewModel

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    navigateToDetails: (CartItem) -> Unit,
    authViewModel: AuthViewModel,
    viewModel: ProductViewModel,
    cartModified: State<Boolean>,
    onCartUpdated: () -> Unit,
    onAuthClick: () -> Unit,
) {
    val authState by authViewModel.authState.collectAsState()
    val cartState by viewModel.cartState.collectAsState()

    var cartInitialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(authState, cartModified) {
        if (authState is AuthState.Authenticated) {
            if (!cartInitialized) {
                viewModel.loadCart()
                cartInitialized = true
            } else if (cartModified.value) {
                viewModel.loadCart()
                onCartUpdated()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            SharedTopBar(
                title = stringResource(id = R.string.cart),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.cart),
                        contentDescription = null,
                        modifier.size(IconSize)
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    start = MediumPadding1,
                    end = MediumPadding1
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (authState) {
                is AuthState.Authenticated -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        CartList(
                            state = cartState,
                            onClick = { cartItem -> navigateToDetails(cartItem) },
                            onRemove = { cartItem -> viewModel.removeFromCart(cartItem.product.code) },
                        )
                    }
                }

                AuthState.Unauthenticated -> {
                    Spacer(modifier = Modifier.height(MediumPadding1))

                    GuestUser(onAuthClick = onAuthClick)
                }

                AuthState.Loading -> LoadingScreen()

                is AuthState.Error -> {
                    Text(text = stringResource(id = R.string.cart, (authState as AuthState.Error).message))
                }
            }
        }
    }
}