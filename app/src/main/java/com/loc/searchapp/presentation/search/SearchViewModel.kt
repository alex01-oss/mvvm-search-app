package com.loc.searchapp.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.data.network.dto.MenuResponse
import com.loc.searchapp.domain.usecases.catalog.CatalogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases
) : ViewModel() {

    private val _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    private val _menu = MutableStateFlow<MenuResponse?>(null)
    val menu: StateFlow<MenuResponse?> = _menu.asStateFlow()

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

    fun onEvent(event: SearchEvent) {
        viewModelScope.launch {
            when (event) {
                is SearchEvent.SearchProducts -> {
                    safeLaunch { searchProducts() }
                }

                is SearchEvent.UpdateSearchQuery -> {
                    _state.value = state.value.copy(
                        searchQuery = event.searchQuery,
                        searchType = event.searchType,
                        page = event.page
                    )
                }

                is SearchEvent.ChangeSearchType -> {
                    val placeholder = when(event.searchType) {
                        "code" -> "Enter code"
                        "shape" -> "Enter shape"
                        "dimensions" -> "Enter dimensions"
                        "machine" -> "Enter machine model"
                        else -> "Search..."
                    }

                    _state.value = state.value.copy(
                        searchQuery = "",
                        searchType = event.searchType,
                        placeholder = placeholder
                    )
                }
            }
        }
    }

    private fun searchProducts() {
        viewModelScope.launch {
            val products = catalogUseCases.getCatalogPaging(
                searchQuery = state.value.searchQuery,
                searchType = state.value.searchType,
                token = state.value.token
            )
            _state.value = state.value.copy(products = products)
        }
    }

    private suspend fun safeLaunch(action: suspend () -> Unit) {
        try {
            isLoading = true
            error = null
            action()
        } catch (e: Exception) {
            error = e.localizedMessage ?: "Unknown error"
        } finally {
            isLoading = false
        }
    }
}
