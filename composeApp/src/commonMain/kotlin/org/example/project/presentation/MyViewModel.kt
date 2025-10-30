package org.example.project.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.data.dto.SearchResponse
import org.example.project.domain.MyRepo
import org.example.project.util.NetworkError
import org.example.project.util.Resource
import org.example.project.util.onError
import org.example.project.util.onSuccess

class MyViewModel(private val repo: MyRepo): ViewModel() {

    private val _searchResult =
        MutableStateFlow<Resource<SearchResponse, NetworkError>>(Resource.Idle)
    val searchResult = _searchResult.asStateFlow()

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
}