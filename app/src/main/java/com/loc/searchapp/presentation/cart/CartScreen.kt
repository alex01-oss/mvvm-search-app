package com.loc.searchapp.presentation.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.CartItem
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.auth.AuthState
import com.loc.searchapp.presentation.auth.components.GuestUser
import com.loc.searchapp.presentation.common.base.AuthViewModel
import com.loc.searchapp.presentation.common.base.ProductViewModel
import com.loc.searchapp.presentation.common.components.CartList

@Composable
fun CartScreen(
    cartItems: State<List<CartItem>>,
    navigateToDetails: (CartItem) -> Unit,
    authViewModel: AuthViewModel,
    viewModel: ProductViewModel,
    cartModified: Boolean,
    onCartUpdated: () -> Unit,
    onAuthClick: () -> Unit
) {
    val authState = authViewModel.authState.collectAsState().value
    var cartInitialized by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(authState, cartModified) {
        if (authState is AuthState.Authenticated) {
            if (!cartInitialized) {
                viewModel.getCart()
                cartInitialized = true
            } else if (cartModified) {
                viewModel.getCart()
                onCartUpdated()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = MediumPadding1,
                start = MediumPadding1,
                end = MediumPadding1
            )
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when (authState) {
            is AuthState.Authenticated -> {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.cart),
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = colorResource(id = R.color.text_title)
                    )

                    Spacer(modifier = Modifier.height(MediumPadding1))

                    CartList(
                        modifier = Modifier.padding(horizontal = MediumPadding1),
                        items = cartItems.value,
                        onClick = { cartItem -> navigateToDetails(cartItem) },
                        onRemove = { cartItem -> viewModel.removeFromCart(cartItem.product.code) },
                    )
                }
            }

            AuthState.Unauthenticated -> GuestUser(onAuthClick = onAuthClick)

            AuthState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is AuthState.Error -> {
                Text(text = stringResource(id = R.string.cart, authState.message))
            }
        }
    }
}