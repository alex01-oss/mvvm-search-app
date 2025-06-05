package com.loc.searchapp.feature.shared.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.data.remote.dto.MenuCategory
import com.loc.searchapp.core.data.remote.dto.MenuResponse
import com.loc.searchapp.core.data.remote.dto.VideoId
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
    private val userPreferences: UserPreferences,
) : ViewModel() {
    private val _menuState = MutableStateFlow<UiState<MenuResponse>>(UiState.Loading)
    val menuState: StateFlow<UiState<MenuResponse>> = _menuState.asStateFlow()

    private val _categoriesState = MutableStateFlow<UiState<List<MenuCategory>>>(UiState.Loading)
    val categoriesState = _categoriesState.asStateFlow()

    private val _productsState = MutableStateFlow<UiState<Unit>>(UiState.Loading)

    private val _videoIdsState = MutableStateFlow<UiState<List<VideoId>>>(UiState.Loading)
    val videoIdsState = _videoIdsState.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val catalogFlow: Flow<PagingData<Product>> = userPreferences.tokenFlow
        .flatMapLatest { token ->
            catalogUseCases.getCatalogPaging(token = token)
        }
        .cachedIn(viewModelScope)

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // --- CATALOG ---
            try {
                val token = userPreferences.getAccessToken()
                if (token.isNullOrBlank()) {
                    _productsState.value = UiState.Error("Access token is missing")
                } else {
                    _productsState.value = UiState.Success(Unit)
                }
            } catch (e: Exception) {
                _productsState.value = UiState.Error(e.localizedMessage ?: "Error loading products")
            }

            // --- MENU ---
            try {
                val menu = catalogUseCases.getMenu()
                _menuState.value = UiState.Success(menu)

                val categories = menu.categories
                _categoriesState.value =
                    if (categories.isEmpty()) UiState.Empty
                    else UiState.Success(categories)

            } catch (e: Exception) {
                _menuState.value = UiState.Error(e.localizedMessage ?: "Error loading menu")
                _categoriesState.value = UiState.Error(e.localizedMessage ?: "Error loading categories")
            }

            // --- VIDEOS ---
            try {
                val videos = youtubeUseCases.getLatestVideos("UC3tUVI8r3Bfr8hb9-KzfCvw")
                    .filter { it.kind == "youtube#video" && it.videoId != null }

                _videoIdsState.value =
                    if (videos.isEmpty()) UiState.Empty
                    else UiState.Success(videos.map { VideoId(it.kind, it.videoId!!) })

            } catch (e: Exception) {
                _videoIdsState.value = UiState.Error(e.localizedMessage ?: "Error loading videos")
            }
        }
    }
}