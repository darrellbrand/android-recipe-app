package com.example.recipeapp.presentation

import com.example.recipeapp.data.data_source.remote.ApiService

sealed class AppError() {
    class ApiKeyNetworkError(val message: String) : AppError()
    class NetworkError(val message: String) : AppError()
}
