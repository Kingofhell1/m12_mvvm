package com.example.m12_mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {
    private lateinit var _searchJob: Job

    private val _state: MutableStateFlow<State> = MutableStateFlow<State>(State.Start)

    val state: StateFlow<State> = _state.asStateFlow()

    val searchString = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchString
                .debounce(300)
                .filter { it.length > 2 }
                .onEach {
                    startSearch(it)
                }.collect(searchString)
        }
    }
    // начало поиска введенных данных
    fun startSearch(request: String) {

        // Отменяем предыдущую корутину, если она была запущена :: - это ссылка на класс ,либо метод
        if (::_searchJob.isInitialized && _searchJob.isActive) {
            // отменяем корутину
            _searchJob.cancel()
        }
        // Запускаем новую корутину
        _searchJob = viewModelScope.launch {
            Log.d(TAG, "запуск новой корутины... ")
            doSearch(request)
        }
    }
    //  приостанавливаем запрос поиска
    suspend fun doSearch(requestStr: String): Unit {
        _state.value = State.Loading()
        delay(3_000)
        _state.value = State.Error(requestStr)
    }
}