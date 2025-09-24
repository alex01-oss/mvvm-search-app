package com.loc.searchapp.presentation.search.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.loc.searchapp.core.domain.model.autocomplete.AutocompleteParams
import com.loc.searchapp.core.domain.model.catalog.CatalogParams
import com.loc.searchapp.core.domain.model.catalog.CategoryId
import com.loc.searchapp.core.domain.model.catalog.FilterParams
import com.loc.searchapp.core.domain.model.catalog.Filters
import com.loc.searchapp.core.domain.model.catalog.SearchParams
import com.loc.searchapp.core.domain.usecases.autocomplete.AutocompleteUseCases
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.presentation.search.model.SearchState
import com.loc.searchapp.presentation.shared.model.UiState
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
    private val autocompleteUseCases: AutocompleteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialCategoryId: Int = savedStateHandle["categoryId"] ?: 1
    private val _state = mutableStateOf(SearchState(categoryId = initialCategoryId))
    val state: State<SearchState> = _state

    private val _filtersState = MutableStateFlow<UiState<Filters>>(UiState.Loading)
    val filtersState = _filtersState.asStateFlow()

    private val _selectedFilters = MutableStateFlow<Map<String, List<Int>>>(emptyMap())
    val selectedFilters = _selectedFilters.asStateFlow()

    private val _searchFlow = MutableStateFlow<SearchState?>(null)

    private val _autocompleteSuggestions = MutableStateFlow<List<String>>(emptyList())
    val autocompleteSuggestions = _autocompleteSuggestions.asStateFlow()

    private val _isLoadingAutocomplete = MutableStateFlow(false)
    val isLoadingAutocomplete = _isLoadingAutocomplete.asStateFlow()

    private val _currentAutocompleteField = MutableStateFlow<String?>(null)
    val currentAutocompleteField = _currentAutocompleteField.asStateFlow()

    init {
        loadFilters()
        loadCatalog()
    }

    private fun loadFilters() {
        viewModelScope.launch {
            try {
                val filters = catalogUseCases.getFilters(
                    CategoryId(_state.value.categoryId)
                )
                _filtersState.value = if (
                    filters.bonds.isEmpty() &&
                    filters.grids.isEmpty() &&
                    filters.mountings.isEmpty()
                ) UiState.Empty else UiState.Success(filters)
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
        .flatMapLatest {
            val params = CatalogParams(
                searchCode = state.value.searchParams.searchCode,
                searchShape = state.value.searchParams.searchShape,
                searchDimensions = state.value.searchParams.searchDimensions,
                searchMachine = state.value.searchParams.searchMachine,
                bondIds = state.value.filterParams.bondIds,
                gridSizeIds = state.value.filterParams.gridSizeIds,
                mountingIds = state.value.filterParams.mountingIds,
                categoryId = state.value.categoryId,
                itemsPerPage = 8,
            )

            catalogUseCases.getCatalogPaging(params)
        }
        .cachedIn(viewModelScope)

    fun toggleFilter(categoryKey: String, itemId: Int, isSelected: Boolean) {
        val current = _selectedFilters.value.toMutableMap()
        val currentList = current[categoryKey]?.toMutableList() ?: mutableListOf()

        if (isSelected) currentList.add(itemId) else currentList.remove(itemId)
        current[categoryKey] = currentList

        _selectedFilters.value = current
    }

    fun updateFilters() {
        val filters = _selectedFilters.value
        val catalogFilters = FilterParams(
            bondIds = filters["bonds"] ?: emptyList(),
            gridSizeIds = filters["grids"] ?: emptyList(),
            mountingIds = filters["mountings"] ?: emptyList(),
        )

        _state.value = _state.value.copy(filterParams = catalogFilters)
        _searchFlow.value = _state.value
    }

    fun search(searchParams: Any) {
        _state.value = _state.value.copy(searchParams = searchParams as SearchParams)
        _searchFlow.value = _state.value
    }

    fun getCurrentSearchParams(): SearchParams {
        val params = _state.value.searchParams
        return params
    }

    fun getAutocomplete(query: String, fieldType: String) {
        if (query.isEmpty()) {
            _autocompleteSuggestions.value = emptyList()
            return
        }

        _currentAutocompleteField.value = fieldType
        _isLoadingAutocomplete.value = true

        viewModelScope.launch {
            try {
                val currentState = _state.value
                val params = AutocompleteParams(
                    query = query,
                    categoryId = currentState.categoryId,
                    searchCode = if (fieldType != "code") currentState.searchParams.searchCode else null,
                    searchShape = if (fieldType != "shape") currentState.searchParams.searchShape else null,
                    searchDimensions = if (fieldType != "dimensions") currentState.searchParams.searchDimensions else null,
                    searchMachine = if (fieldType != "machine") currentState.searchParams.searchMachine else null,
                    bondIds = currentState.filterParams.bondIds,
                    gridSizeIds = currentState.filterParams.gridSizeIds,
                    mountingIds = currentState.filterParams.mountingIds
                )

                val suggestions = when (fieldType) {
                    "code" -> autocompleteUseCases.autocompleteCode(params)
                    "shape" -> autocompleteUseCases.autocompleteShape(params)
                    "dimensions" -> autocompleteUseCases.autocompleteDimensions(params)
                    "machine" -> autocompleteUseCases.autocompleteMachine(params)
                    else -> emptyList()
                }
                _autocompleteSuggestions.value = suggestions
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Autocomplete Error", e)
                _autocompleteSuggestions.value = emptyList()
            } finally {
                _isLoadingAutocomplete.value = false
            }
        }
    }

    fun clearAutocomplete() {
        _autocompleteSuggestions.value = emptyList()
        _currentAutocompleteField.value = null
        _isLoadingAutocomplete.value = false
    }
}