package com.example.recipeapp.data.data_source.repository

import com.example.recipeapp.data.data_source.remote.ApiService
import com.example.recipeapp.data.data_source.remote.OpenAiService
import com.example.recipeapp.domain.model.ApiKeyResponse
import com.example.recipeapp.domain.model.CategoryResponse
import com.example.recipeapp.domain.model.MealResponse
import com.example.recipeapp.domain.model.OpenAiResponse
import com.example.recipeapp.repository.RecipeRepository
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(val openAiService: OpenAiService, val apiService: ApiService) :
    RecipeRepository {
    override suspend fun getRandomMeal(): MealResponse {
       return apiService.getRandomMeal()
    }

    override suspend fun getSearchMeals(param1: String): MealResponse {
        return apiService.getSearchMeals(param1)
    }

    override suspend fun getDetailMeal(param1: String): MealResponse {
        return apiService.getDetailMeal(param1)
    }

    override suspend fun getSearchCategoryMeals(param1: String): MealResponse {
        return apiService.getSearchCategoryMeals(param1)
    }

    override suspend fun getCategories(): CategoryResponse {
        return apiService.getCategories()
    }

    override suspend fun getApiKey(androidId: String): ApiKeyResponse {
        return openAiService.getApiKey(androidId)
    }

    override suspend fun getOpenAIRecipe(
        androidId: String,
        apiKey: String,
        message: String
    ): OpenAiResponse {
        return openAiService.getOpenAIRecipe(
            androidId = androidId,
            apiKey = apiKey,
            message = message
        )
    }
}