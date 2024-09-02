package com.example.recipeapp.presentation

sealed class AppError() {
    class ApiKeyNetworkError(val message: String) : AppError()
    class NetworkError(val message: String) : AppError()
}
