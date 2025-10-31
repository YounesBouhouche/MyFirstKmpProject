package org.example.project.presentation

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.dto.Photo
import org.example.project.data.dto.SearchResponse
import org.example.project.domain.DownloadUseCase
import org.example.project.domain.MyRepo
import org.example.project.util.NetworkError
import org.example.project.util.Resource
import org.example.project.util.onError
import org.example.project.util.onSuccess

class MyViewModel(
    private val repo: MyRepo,
    private val dataStore: DataStore<Preferences>,
    private val downloadUseCase: DownloadUseCase
): ViewModel() {
    val THEME_KEY = stringPreferencesKey("theme")

    val theme = dataStore.data.map {
        try {
            Themes.valueOf(it[THEME_KEY] ?: "LIGHT")
        } catch (_: Exception) {
            Themes.LIGHT
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Themes.LIGHT)

    private val _searchResult =
        MutableStateFlow<Resource<SearchResponse, NetworkError>>(Resource.Idle)
    val searchResult = _searchResult.asStateFlow()

    private val _downloadState = MutableStateFlow(DownloadState())
    val downloadState = _downloadState.asStateFlow()

    private val _selectedPicture = MutableStateFlow<Photo?>(null)
    val selectedPicture = _selectedPicture.asStateFlow()

    fun selectPicture(picture: Photo) {
        _selectedPicture.value = picture
    }

    fun unselectPicture() {
        _selectedPicture.value = null
        _downloadState.value = DownloadState()
    }

    fun getHelloWorld(): String {
        return repo.helloWorld()
    }

    fun searchPictures(query: String) {
        _searchResult.value = Resource.Loading
        viewModelScope.launch(Dispatchers.IO) {
            repo.searchPictures(query).onSuccess {
                _searchResult.value = Resource.Success(it)
            }.onError {
                _searchResult.value = Resource.Error(it)
            }
        }
    }

    fun cancelSearch() {
        viewModelScope.coroutineContext.cancelChildren()
        _searchResult.value = Resource.Idle
    }

    fun switchTheme() {
        viewModelScope.launch(Dispatchers.IO) {
             dataStore.edit { prefs ->
                 prefs[THEME_KEY] = when(theme.first()) {
                     Themes.LIGHT -> "DARK"
                     Themes.DARK -> "SYSTEM_DEFAULT"
                     Themes.SYSTEM_DEFAULT -> "LIGHT"
                 }
             }
        }
    }

    fun download(url: String, path: String) {
        _downloadState.update {
            it.copy(
                status = Status.DOWNLOADING,
                progress = 0f
            )
        }
        _selectedPicture.value?.let {
            viewModelScope.launch(Dispatchers.IO) {
                downloadUseCase(
                    url,
                    path,
                    onError = { error ->
                        _downloadState.update {
                            it.copy(
                                status = Status.ERROR,
                                errorMessage = error.message
                            )
                        }
                    },
                    onUpdate = { progress ->
                        _downloadState.update {
                            it.copy(
                                status = Status.DOWNLOADING,
                                progress = progress
                            )
                        }
                    },
                    onSuccess = {
                        _downloadState.update {
                            it.copy(status = Status.SUCCESS)
                        }
                    }
                )
            }
        }
    }
}