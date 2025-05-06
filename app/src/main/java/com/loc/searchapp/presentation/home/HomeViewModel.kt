package com.loc.searchapp.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.loc.searchapp.data.local.preferences.UserPreferences
import com.loc.searchapp.data.remote.dto.MenuResponse
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.domain.usecases.catalog.CatalogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases,
    userPreferences: UserPreferences,
) : ViewModel() {
    private val _menu = MutableStateFlow<MenuResponse?>(null)
    val menu: StateFlow<MenuResponse?> = _menu.asStateFlow()
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val catalogFlow: Flow<PagingData<Product>> = userPreferences.tokenFlow
        .flatMapLatest { token ->
            catalogUseCases.getCatalogPaging(token = token)
        }
        .cachedIn(viewModelScope)

    init {
        fetchMenu()
    }

    private fun fetchMenu() {
        viewModelScope.launch {
            isLoading = true
            try {
                val result = catalogUseCases.getMenu()
                _menu.value = result
                error = null
            } catch (e: Exception) {
                error = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }
}
