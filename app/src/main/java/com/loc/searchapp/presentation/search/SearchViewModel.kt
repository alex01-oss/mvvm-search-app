package com.loc.searchapp.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.loc.searchapp.data.remote.dto.MenuResponse
import com.loc.searchapp.data.local.preferences.UserPreferences
import com.loc.searchapp.domain.usecases.catalog.CatalogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state

    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    private val _menu = MutableStateFlow<MenuResponse?>(null)
    val menu: StateFlow<MenuResponse?> = _menu.asStateFlow()

    private var lastSearchParams: Triple<String, String, Int>? = null
    private val _searchFlow = MutableStateFlow<Triple<String, String, Int>?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val products = _searchFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { (query, type, _) ->
            flow {
                val token = userPreferences.getToken()
                emitAll(
                    catalogUseCases.getCatalogPaging(
                        searchQuery = query,
                        searchType = type,
                        token = token
                    )
                )
            }
        }.cachedIn(viewModelScope)

    fun onSearchOpened() {
        if(_menu.value == null) {
            fetchMenu()
        }
    }

    fun onEvent(event: SearchEvent) {
        viewModelScope.launch {
            when (event) {
                is SearchEvent.SearchProducts -> {

                    val currentParams = Triple(
                        state.value.searchQuery,
                        state.value.searchType,
                        state.value.page
                    )

                    if(lastSearchParams != currentParams) {
                        lastSearchParams = currentParams
                        _searchFlow.value = currentParams
                    }
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
