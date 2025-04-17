package com.loc.searchapp.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.loc.searchapp.data.preferences.UserPreferences
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.domain.usecases.catalog.CatalogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _localCartChanges = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val localCartChanges = _localCartChanges.asStateFlow()

    val catalogFlow: Flow<PagingData<Product>> = flow {
        val token = userPreferences.getToken()
        emitAll(catalogUseCases.getCatalogPaging(token = token))
    }.cachedIn(viewModelScope)

    var cartModified by mutableStateOf(false)
        internal set

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            val token = userPreferences.getToken()
            catalogUseCases.getCatalog(token = token)
        }
    }

    fun addToCart(code: String) {
        viewModelScope.launch {
            catalogUseCases.addProduct(code)
            _localCartChanges.value = _localCartChanges.value + (code to true)
                cartModified = true
        }
    }

    fun removeFromCart(code: String) {
        viewModelScope.launch {
            catalogUseCases.deleteProduct(code)
            _localCartChanges.value = _localCartChanges.value + (code to false)
            cartModified = true
        }
    }
}
