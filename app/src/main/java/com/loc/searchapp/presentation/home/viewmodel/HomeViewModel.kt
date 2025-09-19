package com.loc.searchapp.presentation.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.loc.searchapp.core.data.local.datastore.UserPreferences
import com.loc.searchapp.core.data.remote.dto.Category
import com.loc.searchapp.core.domain.usecases.catalog.CatalogUseCases
import com.loc.searchapp.core.domain.usecases.youtube.YoutubeUseCases
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
                val res = catalogUseCases.getCategories()
                if (res.isSuccessful) {
                    val body = res.body()

                    if (body != null) {
                        _categoriesState.value = UiState.Success(body)
                    } else {
                        _categoriesState.value = UiState.Empty
                    }
                }

            } catch (e: Exception) {
                _categoriesState.value = UiState.Error(e.localizedMessage ?: "Error loading categories")
            }

            try {
                val res = youtubeUseCases.getLatestVideos()
                if (res.isSuccessful) {
                    val body = res.body()
                    if (body != null) {
                        val videos = body.filter { it.snippet.resourceId.videoId?.isNotBlank() == true }

                        val uiVideos = videos.map { item ->
                            VideoUi(
                                videoId = item.snippet.resourceId.videoId!!,
                                title = item.snippet.title,
                                thumbnailUrl = item.snippet.thumbnails.high?.url
                                    ?: item.snippet.thumbnails.medium?.url
                                    ?: ""
                            )
                        }

                        _videoState.value = UiState.Success(uiVideos)
                    } else {
                        _videoState.value = UiState.Empty
                    }
                }
            } catch (e: Exception) {
                _videoState.value = UiState.Error(
                    e.localizedMessage ?: "Error loading videos"
                )
            }
        }
    }
}