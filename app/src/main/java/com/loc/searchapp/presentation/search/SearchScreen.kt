package com.loc.searchapp.presentation.search

import android.content.Intent
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.paging.compose.collectAsLazyPagingItems
import com.loc.searchapp.R
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.presentation.Dimens.MediumPadding1
import com.loc.searchapp.presentation.common.components.ProductsList
import com.loc.searchapp.presentation.common.components.SearchBar
import com.loc.searchapp.presentation.search.components.BurgerMenu
import com.loc.searchapp.presentation.common.base.ProductViewModel
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(
    state: SearchState,
    event: (SearchEvent) -> Unit,
    navigateToDetails: (Product) -> Unit,
    viewModel: SearchViewModel,
    productViewModel: ProductViewModel
) {
    val lazyProducts = viewModel.products.collectAsLazyPagingItems()
    val localCartChanges = productViewModel.localCartChanges.collectAsState().value

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val menu = viewModel.menu.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.onSearchOpened()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .width(300.dp)
                    .statusBarsPadding(),
                drawerContainerColor = MaterialTheme.colorScheme.background
            ) {
                BurgerMenu(
                    menu = menu,
                    onOpenUrl = { url ->
                        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                        context.startActivity(intent)
                    },
                    onNavigateToSearch = { searchType ->
                        event(SearchEvent.ChangeSearchType(searchType))
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(
                    top = MediumPadding1,
                    start = MediumPadding1,
                    end = MediumPadding1
                )
                .fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .burgerButtonBorder(),
                    color =
                        if (isSystemInDarkTheme()) colorResource(id = R.color.input_background)
                        else Color.Transparent,
                    tonalElevation = 0.dp,
                    shape = MaterialTheme.shapes.medium
                ) {
                    IconButton(onClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                SearchBar(
                    text = state.searchQuery,
                    placeholder = state.placeholder,
                    readOnly = false,
                    onValueChange = {
                        event(SearchEvent.UpdateSearchQuery(it, state.searchType, state.page))
                    },
                    onSearch = { event(SearchEvent.SearchProducts) }
                )
            }

            Spacer(modifier = Modifier.height(MediumPadding1))

            ProductsList(
                items = lazyProducts,
                onClick = navigateToDetails,
                onAdd = { productViewModel.addToCart(it.code) },
                onRemove = { productViewModel.removeFromCart(it.code) },
                localCartChanges = localCartChanges,
                showShimmerOnFirstLoad = false,
                modifier = Modifier.padding(horizontal = MediumPadding1),
            )
        }
    }
}

fun Modifier.burgerButtonBorder() = composed {
    if (!isSystemInDarkTheme()) {
        border(
            width = 1.dp,
            color = Color.Black,
            shape = MaterialTheme.shapes.medium
        )
    } else {
        this
    }
}