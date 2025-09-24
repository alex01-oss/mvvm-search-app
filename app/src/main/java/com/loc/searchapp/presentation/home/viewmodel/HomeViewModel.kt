package com.loc.searchapp.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.domain.model.catalog.Category
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.core.domain.usecases.youtube.YoutubeUseCases
import com.loc.searchapp.presentation.shared.mapper.toUi
import com.loc.searchapp.presentation.shared.model.UiState
import com.loc.searchapp.presentation.shared.model.VideoUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases,
    private val youtubeUseCases: YoutubeUseCases,
    private val userPreferences: UserPreferences,
) : ViewModel() {
    private val _categoriesState = MutableStateFlow<UiState<List<Category>>>(UiState.Loading)
    val categoriesState = _categoriesState.asStateFlow()

    private val _productsState = MutableStateFlow<UiState<Unit>>(UiState.Loading)

    private val _videoState = MutableStateFlow<UiState<List<VideoUi>>>(UiState.Loading)
    val videoState = _videoState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
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

            try {
                val categories = catalogUseCases.getCategories()
                _categoriesState.value = UiState.Success(categories)
            } catch (e: Exception) {
                _categoriesState.value =
                    UiState.Error(e.localizedMessage ?: "Error loading categories")
            }

            try {
                val videos = youtubeUseCases.getLatestVideos().mapNotNull { it.toUi() }
                _videoState.value = if (videos.isEmpty()) UiState.Empty else UiState.Success(videos)
            } catch (e: Exception) {
                _videoState.value = UiState.Error(
                    e.localizedMessage ?: "Error loading videos"
                )
            }
        }
    }
}