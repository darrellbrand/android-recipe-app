package com.example.recipeapp.data.data_source.remote

import com.example.recipeapp.domain.model.CategoryResponse
import com.example.recipeapp.domain.model.MealResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse

    @GET("search.php?")
    suspend fun getSearchMeals(@Query("s") param1: String): MealResponse

    @GET("lookup.php?")
    suspend fun getDetailMeal(@Query("i") param1: String): MealResponse

    @GET("filter.php?")
    suspend fun getSearchCategoryMeals(@Query("c") param1: String): MealResponse

    @GET("categories.php?")
    suspend fun getCategories(): CategoryResponse
}