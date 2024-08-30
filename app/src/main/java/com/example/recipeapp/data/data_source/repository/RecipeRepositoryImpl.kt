package com.example.recipeapp.data.data_source.repository

import com.example.recipeapp.domain.model.ApiKeyResponse
import com.example.recipeapp.domain.model.CategoryResponse
import com.example.recipeapp.domain.model.MealResponse
import com.example.recipeapp.domain.model.OpenAiResponse
import com.example.recipeapp.repository.RecipeRepository
import retrofit2.Retrofit

class RecipeRepositoryImpl(openAiService: Retrofit, apiService: Retrofit) : RecipeRepository {
    override suspend fun getRandomMeal(): MealResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchMeals(param1: String): MealResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getDetailMeal(param1: String): MealResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getSearchCategoryMeals(param1: String): MealResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getCategories(): CategoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getApiKey(androidId: String): ApiKeyResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getOpenAIRecipe(
        androidId: String,
        apiKey: String,
        message: String
    ): OpenAiResponse {
        TODO("Not yet implemented")
    }
}