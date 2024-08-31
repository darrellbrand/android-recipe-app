package com.example.recipeapp.di

import com.example.recipeapp.data.data_source.remote.ApiService
import com.example.recipeapp.data.data_source.remote.OpenAiService
import com.example.recipeapp.data.data_source.repository.RecipeRepositoryImpl
import com.example.recipeapp.repository.RecipeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    @Named("RecipeRetrofit")
    fun provideRecipeRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("AIRetrofit")
    fun provideAIRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://spring-backend-ghcr-latest.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipeApiService(@Named("RecipeRetrofit") retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAIApiService(@Named("AIRetrofit") retrofit: Retrofit): OpenAiService {
        return retrofit.create(OpenAiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(
        retrofitAI: OpenAiService,
        retrofitRecipe: ApiService
    ): RecipeRepository {
        return RecipeRepositoryImpl(openAiService = retrofitAI, apiService = retrofitRecipe)
    }
}