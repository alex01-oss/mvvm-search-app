package com.loc.searchapp.feature.search.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.feature.search.model.SearchEvent
import com.loc.searchapp.feature.search.model.SearchState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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

    private var lastSearchParams: Triple<String, String, Int>? = null
    private val _searchFlow = MutableStateFlow<Triple<String, String, Int>?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val products = _searchFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { (query, type, _) ->
            flow {
                val token = userPreferences.getAccessToken()
                emitAll(
                    catalogUseCases.getCatalogPaging(
                        searchQuery = query,
                        searchType = type,
                        token = token
                    )
                )
            }
        }.cachedIn(viewModelScope)

    fun onEvent(event: SearchEvent) {
        viewModelScope.launch {
            when (event) {
                is SearchEvent.SearchProducts -> {
                    val currentParams = Triple(
                        state.value.searchQuery.trim(),
                        state.value.searchType.trim(),
                        state.value.page
                    )

                    if (currentParams == lastSearchParams) return@launch

                    lastSearchParams = currentParams
                    _searchFlow.value = currentParams
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
}