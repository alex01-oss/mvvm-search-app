package com.loc.searchapp.presentation.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
                        upsertArticle(code = event.product.code)
                    } else {
                        deleteArticle(code = event.product.code)
                    }
                }
            }

            is DetailsEvent.RemoveSideEffect -> {
                sideEffect = null
            }
        }
    }

    private suspend fun upsertArticle(code: String) {
        catalogUseCases.addProduct(code = code)
        sideEffect = "Product inserted"
    }

    private suspend fun deleteArticle(code: String) {
        catalogUseCases.deleteProduct(code = code)
        sideEffect = "Product deleted"
    }
}