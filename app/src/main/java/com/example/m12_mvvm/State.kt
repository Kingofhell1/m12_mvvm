package com.example.m12_mvvm

sealed class State(val isLoading: Boolean = false,
                   open val request: String? = null,
                   open val result: String? = null ) {

    object Start: State()

    data class Loading(override val result: String = "Идёт поиск") : State(isLoading = true)

    data class Success(
        override val request: String?,
        override val result: String = "Поиск $request выполнен") : State(request = request)

    data class Error(
        override val request: String?,
        override val result: String = "По запросу $request ничего не найдено") : State(request = request)
}