package com.example.recipeapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val retrofit = Retrofit.Builder().baseUrl("http://www.themealdb.com/api/json/v1/1/")
    .addConverterFactory(GsonConverterFactory.create()).build()

val recipeService: ApiService? = retrofit.create(ApiService::class.java)

interface ApiService {
    @GET("random.php")
    suspend fun getRandomMeal(): MealResponse

    @GET("search.php?")
    suspend fun getSearchMeal( @Query("s") param1: String): MealResponse
}