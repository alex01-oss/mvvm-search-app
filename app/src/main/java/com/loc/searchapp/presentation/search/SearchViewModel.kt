package com.loc.searchapp.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.domain.model.ListItem
import com.loc.searchapp.domain.usecases.catalog.CatalogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases
) : ViewModel() {

    private val _state = mutableStateOf(SearchState())
    val state: State<SearchState> = _state

    fun onEvent(event: SearchEvent) {
        viewModelScope.launch {
            when (event) {
                is SearchEvent.SearchProducts -> {
                    searchProducts()
                }

                is SearchEvent.UpdateSearchQuery -> {
                    _state.value = state.value.copy(
                        searchQuery = event.searchQuery,
                        searchType = event.searchType,
                        page = event.page
                    )
                }
            }
        }
    }

    private fun searchProducts() {
        viewModelScope.launch {
            val products = catalogUseCases.getCatalog(
                searchQuery = state.value.searchQuery,
                searchType = state.value.searchType,
                page = state.value.page
            )
            val listItems = products.map { ListItem.CatalogListItem(it) }
            _state.value = state.value.copy(products = listItems)
        }
    }
}
