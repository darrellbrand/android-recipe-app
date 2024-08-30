package com.example.recipeapp.repository
import com.example.recipeapp.domain.model.ApiKeyResponse
import com.example.recipeapp.domain.model.CategoryResponse
import com.example.recipeapp.domain.model.MealResponse
import com.example.recipeapp.domain.model.OpenAiResponse

interface RecipeRepository {

    suspend fun getRandomMeal(): MealResponse
    suspend fun getSearchMeals(param1: String): MealResponse
    suspend fun getDetailMeal(param1: String): MealResponse
    suspend fun getSearchCategoryMeals(param1: String): MealResponse
    suspend fun getCategories(): CategoryResponse


    suspend fun getApiKey(androidId: String): ApiKeyResponse

    suspend fun getOpenAIRecipe(androidId: String, apiKey: String, message: String)
            : OpenAiResponse

}