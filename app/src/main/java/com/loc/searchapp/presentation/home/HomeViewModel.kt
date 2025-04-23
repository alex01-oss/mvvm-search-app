package com.loc.searchapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.loc.searchapp.data.preferences.UserPreferences
import com.loc.searchapp.domain.model.Product
import com.loc.searchapp.domain.usecases.catalog.CatalogUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val catalogUseCases: CatalogUseCases,
    userPreferences: UserPreferences
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val catalogFlow: Flow<PagingData<Product>> = userPreferences.tokenFlow
        .flatMapLatest { token ->
            catalogUseCases.getCatalogPaging(token = token)
        }
        .cachedIn(viewModelScope)
}
