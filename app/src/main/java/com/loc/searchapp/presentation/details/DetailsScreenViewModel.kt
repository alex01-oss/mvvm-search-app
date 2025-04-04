package com.loc.searchapp.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.domain.usecases.catalog.CatalogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases
): ViewModel() {

    var sideEffect by mutableStateOf<String?>(null)
        private set

    fun onEvent(event: DetailsEvent) {
        when(event) {
            is DetailsEvent.UpsertDeleteProduct -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val product = catalogUseCases.selectProduct(code = event.product.code)

                    if(product == null) {
                        upsertArticle(product = event.product)
                    } else {
                        deleteArticle(product = event.product)
                    }
                }
            }

            is DetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    private suspend fun upsertArticle(product: Product) {
        catalogUseCases.addProduct(product = product)
        sideEffect = "Product inserted"
    }

    private suspend fun deleteArticle(product: Product) {
        catalogUseCases.deleteProduct(product = product)
        sideEffect = "Product deleted"
    }
}