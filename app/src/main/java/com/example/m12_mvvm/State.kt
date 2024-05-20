package com.example.m12_mvvm

sealed class State {
    object Start: State()
    object Loading : State()
    data class Success(val result:String) : State()
}