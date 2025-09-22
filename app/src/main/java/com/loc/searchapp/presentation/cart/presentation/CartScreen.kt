package com.loc.searchapp.presentation.cart.presentation

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.loc.searchapp.R
import com.loc.searchapp.presentation.shared.components.SharedTopBar
import com.loc.searchapp.presentation.cart.components.CartList
import com.loc.searchapp.presentation.shared.components.loading.LoadingScreen
import com.loc.searchapp.core.ui.values.Dimens.IconSize
import com.loc.searchapp.core.ui.values.Dimens.MediumPadding1
import com.loc.searchapp.presentation.shared.components.notifications.GuestUser
import com.loc.searchapp.presentation.shared.model.AuthState
import com.loc.searchapp.presentation.shared.viewmodel.AuthViewModel
import com.loc.searchapp.presentation.shared.viewmodel.ProductViewModel

@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    navigateToDetails: (id: Int) -> Unit,
    authViewModel: AuthViewModel,
    viewModel: ProductViewModel,
    onAuthClick: () -> Unit,
) {
    val authState by authViewModel.authState.collectAsState()
    val cartState by viewModel.cartState.collectAsState()

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
                .padding(top = paddingValues.calculateTopPadding())
                .padding(horizontal = MediumPadding1),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (authState) {
                is AuthState.Authenticated -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        CartList(
                            state = cartState,
                            onClick = { id -> navigateToDetails(id) },
                            onRemove = { id -> viewModel.removeFromCart(id) },
                            inProgress = viewModel.inProgress.collectAsState().value,
                            buttonStates = viewModel.buttonStates.collectAsState().value
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