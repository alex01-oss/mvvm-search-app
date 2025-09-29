package com.loc.searchapp.presentation.search.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.loc.searchapp.core.domain.model.autocomplete.AutocompleteParams
import com.loc.searchapp.core.domain.model.catalog.CategoryId
import com.loc.searchapp.core.domain.model.catalog.FilterParams
import com.loc.searchapp.core.domain.model.catalog.Filters
import com.loc.searchapp.core.domain.model.catalog.SearchParams
import com.loc.searchapp.core.domain.model.catalog.toCatalogParams
import com.loc.searchapp.core.domain.usecases.autocomplete.AutocompleteUseCases
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.presentation.shared.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases,
    private val autocompleteUseCases: AutocompleteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: Int = savedStateHandle["categoryId"] ?: 1
    private val _searchParams = MutableStateFlow(SearchParams())
    val searchParams = _searchParams.asStateFlow()

    private val _filterParams = MutableStateFlow(FilterParams())

    private val _filtersState = MutableStateFlow<UiState<Filters>>(UiState.Loading)
    val filtersState = _filtersState.asStateFlow()

    private val _selectedFilters = MutableStateFlow<Map<String, List<Int>>>(emptyMap())
    val selectedFilters = _selectedFilters.asStateFlow()

    private val _autocompleteSuggestions = MutableStateFlow<List<String>>(emptyList())
    val autocompleteSuggestions = _autocompleteSuggestions.asStateFlow()

    private val _isLoadingAutocomplete = MutableStateFlow(false)
    val isLoadingAutocomplete = _isLoadingAutocomplete.asStateFlow()

    private val _currentAutocompleteField = MutableStateFlow<String?>(null)
    val currentAutocompleteField = _currentAutocompleteField.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val filters = catalogUseCases.getFilters(
                    CategoryId(categoryId)
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val products = combine(_searchParams, _filterParams) { search, filter ->
        search to filter
    }.flatMapLatest { (search, filter) ->
            val params = search.toCatalogParams(
                filterParams = filter,
                categoryId = categoryId
            )
            catalogUseCases.getCatalogPaging(params)
        }.cachedIn(viewModelScope)

    fun toggleFilter(categoryKey: String, itemId: Int, isSelected: Boolean) {
        val current = _selectedFilters.value.toMutableMap()
        val currentList = current[categoryKey]?.toMutableList() ?: mutableListOf()

        if (isSelected) currentList.add(itemId) else currentList.remove(itemId)
        current[categoryKey] = currentList

        _selectedFilters.value = current
    }

    fun updateFilters() {
        val filters = _selectedFilters.value
        _filterParams.value = FilterParams(
            bondIds = filters["bonds"] ?: emptyList(),
            gridSizeIds = filters["grids"] ?: emptyList(),
            mountingIds = filters["mountings"] ?: emptyList(),
        )
    }

    fun search(searchParams: SearchParams) {
        _searchParams.value = searchParams
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
                val params = AutocompleteParams(
                    query = query,
                    categoryId = categoryId,
                    searchCode = if (fieldType != "code") _searchParams.value.searchCode else null,
                    searchShape = if (fieldType != "shape") _searchParams.value.searchShape else null,
                    searchDimensions = if (fieldType != "dimensions") _searchParams.value.searchDimensions else null,
                    searchMachine = if (fieldType != "machine") _searchParams.value.searchMachine else null,
                    bondIds = _filterParams.value.bondIds,
                    gridSizeIds = _filterParams.value.gridSizeIds,
                    mountingIds = _filterParams.value.mountingIds
                )

                val suggestions = when (fieldType) {
                    "code" -> autocompleteUseCases.autocompleteCode(params)
                    "shape" -> autocompleteUseCases.autocompleteShape(params)
                    "dimensions" -> autocompleteUseCases.autocompleteDimensions(params)
                    "machine" -> autocompleteUseCases.autocompleteMachine(params)
                    else -> emptyList()
                }
                _autocompleteSuggestions.value = suggestions
            } catch (_: Exception) {
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