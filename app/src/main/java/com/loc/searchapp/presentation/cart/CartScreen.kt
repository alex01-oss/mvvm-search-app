package com.loc.searchapp.presentation.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.CartItem
import com.loc.searchapp.presentation.Dimens.AvatarHeight
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.auth.AuthState
import com.loc.searchapp.presentation.auth.AuthViewModel
import com.loc.searchapp.presentation.common.Avatar
import com.loc.searchapp.presentation.common.CartList

@Composable
fun CartScreen(
    cartItems: State<List<CartItem>>,
    navigateToDetails: (CartItem) -> Unit,
    onRemove: (CartItem) -> Unit,
    viewModel: CartViewModel,
    authViewModel: AuthViewModel,
    cartModified: Boolean,
    onCartUpdated: () -> Unit,
    onAuthClick: () -> Unit
) {

    val authState = authViewModel.authState.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        when(authState) {
            is AuthState.Authenticated -> {
                LaunchedEffect(Unit) {
                    viewModel.getCart()
                }

                LaunchedEffect(cartModified) {
                    if (cartModified == true) {
                        viewModel.getCart()
                        onCartUpdated()
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = MediumPadding1,
                            start = MediumPadding1,
                            end = MediumPadding1
                        )
                ) {
                    Text(
                        text = "Cart",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = colorResource(id = R.color.text_title)
                    )

                    Spacer(modifier = Modifier.height(MediumPadding1))

                    CartList(
                        modifier = Modifier.padding(horizontal = MediumPadding1),
                        items = cartItems.value,
                        onClick = { item -> navigateToDetails(item) },
                        onRemove = { item -> onRemove(item) },
                    )
                }
            }
            AuthState.Unauthenticated -> {
                Avatar(
                    firstName = "",
                    lastName = "",
                    size = AvatarHeight,
                    textStyle = MaterialTheme.typography.labelLarge,
                    placeholder = {
                        Image(
                            painter = painterResource(R.drawable.person),
                            contentDescription = "User Avatar",
                            modifier = Modifier.size(40.dp)
                        )
                    }
                )

                Text(text = "To be able to create a cart and place an order, you must log in or create an account.")

                Spacer(modifier = Modifier.height(MediumPadding1))

                Button(
                    onClick = onAuthClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(text = "Auth", color = Color.White)
                }
            }
            AuthState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is AuthState.Error -> {
                Text("Something went wrong: ${authState.message}")
            }
        }
    }
}