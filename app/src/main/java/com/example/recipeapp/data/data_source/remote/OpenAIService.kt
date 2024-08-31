package com.example.recipeapp.data.data_source.remote

import com.example.recipeapp.BuildConfig
import com.example.recipeapp.domain.model.ApiKeyResponse
import com.example.recipeapp.domain.model.OpenAiResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

private const val apiKeyHeader = BuildConfig.xapikey
private const val androidIdHeader = BuildConfig.xandroidid
private const val basicAuthHeader = BuildConfig.xbasicauth
interface OpenAiService {

    @GET("register")
    @Headers("Authorization: Basic $basicAuthHeader")
    suspend fun getApiKey(@Header(androidIdHeader) androidId: String): ApiKeyResponse

    @POST("ai/generate")
    @Headers("Authorization: Basic $basicAuthHeader")
    suspend fun getOpenAIRecipe(
        @Header(androidIdHeader) androidId: String,
        @Header(apiKeyHeader) apiKey: String, @Query("message") message: String
    ): OpenAiResponse
}
/*
    @GET("lookup.php?")
    suspend fun getDetailMeal(@Query("i") param1: String): MealResponse

    @GET("filter.php?")
    suspend fun getSearchCategoryMeals(@Query("c") param1: String): MealResponse

    @GET("categories.php?")
    suspend fun getCategories(): CategoryResponse*/
