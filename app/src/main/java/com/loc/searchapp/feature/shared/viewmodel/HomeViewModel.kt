package com.loc.searchapp.feature.shared.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.data.remote.dto.MenuResponse
import com.loc.searchapp.core.domain.model.catalog.Product
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.core.domain.usecases.youtube.YoutubeUseCases
import com.loc.searchapp.feature.shared.model.UiState
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
    private val youtubeUseCases: YoutubeUseCases,
    userPreferences: UserPreferences,
) : ViewModel() {
    private val _menu = MutableStateFlow<MenuResponse?>(null)
    val menu: StateFlow<MenuResponse?> = _menu.asStateFlow()

    private val _productsState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val productsState: StateFlow<UiState<Unit>> = _productsState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val catalogFlow: Flow<PagingData<Product>> = userPreferences.tokenFlow
        .flatMapLatest { token ->
            catalogUseCases.getCatalogPaging(token = token)
        }
        .cachedIn(viewModelScope)

    var videoIds by mutableStateOf<List<String>>(emptyList())
        private set

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _productsState.value = UiState.Loading

                val menuResult = catalogUseCases.getMenu()
                _menu.value = menuResult

                val videoIds = youtubeUseCases.getLatestVideos("UC3tUVI8r3Bfr8hb9-KzfCvw")
                this@HomeViewModel.videoIds = videoIds

                _productsState.value = UiState.Success(Unit)

            } catch (e: Exception) {
                _productsState.value = UiState.Error(e.localizedMessage ?: "Error loading data")
            }
        }
    }
}