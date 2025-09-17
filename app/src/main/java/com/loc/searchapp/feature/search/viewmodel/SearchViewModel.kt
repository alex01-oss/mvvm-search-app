package com.loc.searchapp.feature.search.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.loc.searchapp.core.data.remote.dto.FiltersResponse
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.core.utils.FilterParams
import com.loc.searchapp.feature.search.model.SearchState
import com.loc.searchapp.feature.shared.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialCategoryId: Int = savedStateHandle["categoryId"] ?: 1
    private val _state = mutableStateOf(SearchState(categoryId = initialCategoryId))
    val state: State<SearchState> = _state

    private val _filtersState = MutableStateFlow<UiState<FiltersResponse>>(UiState.Loading)
    val filtersState = _filtersState.asStateFlow()

    private val _selectedFilters = MutableStateFlow<Map<String, List<Int>>>(emptyMap())
    val selectedFilters = _selectedFilters.asStateFlow()

    private val _searchFlow = MutableStateFlow<SearchState?>(null)

    init {
        loadFilters()
        loadCatalog()
    }

    private fun loadFilters() {
        viewModelScope.launch {
            try {
                val res = catalogUseCases.getFilters(_state.value.categoryId)
                if (res.isSuccessful) {
                    val filters = res.body()
                    if (filters != null) {
                        _filtersState.value = if (
                            filters.bonds.isEmpty() &&
                            filters.grids.isEmpty() &&
                            filters.mountings.isEmpty()
                        ) UiState.Empty else UiState.Success(filters)
                    }
                } else {
                    _filtersState.value = UiState.Error("Error loading filters: ${res.code()}")
                }
            } catch (e: Exception) {
                _filtersState.value = UiState.Error(e.localizedMessage ?: "Error loading filters")
            }
        }
    }

    private fun loadCatalog() {
        _searchFlow.value = _state.value
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val products = _searchFlow
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { params ->
            catalogUseCases.getCatalogPaging(
                search = state.value.searchParams,
                filters = state.value.filterParams,
                categoryId = state.value.categoryId,
                itemsPerPage = 8
            )
        }
        .cachedIn(viewModelScope)

    fun toggleFilter(categoryKey: String, itemId: Int, isSelected: Boolean) {
        val current = _selectedFilters.value.toMutableMap()
        val currentList = current[categoryKey]?.toMutableList() ?: mutableListOf()

        if (isSelected) currentList.add(itemId) else currentList.remove(itemId)
        current[categoryKey] = currentList

        _selectedFilters.value = current
        updateFilters()
    }

    private fun updateFilters() {
        val filters = _selectedFilters.value
        val catalogFilters = FilterParams(
            bondIds = filters["bonds"] ?: emptyList(),
            gridSizeIds = filters["grid_sizes"] ?: emptyList(),
            mountingIds = filters["mountings"] ?: emptyList(),
        )

        _state.value = _state.value.copy(filterParams = catalogFilters)
        _searchFlow.value = _state.value
    }

    fun search(searchParams: Any) {
        // TODO: implement search logic
        _searchFlow.value = _state.value
    }
}