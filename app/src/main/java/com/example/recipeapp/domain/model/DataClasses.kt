package com.example.recipeapp.domain.model

import com.example.recipeapp.presentation.RecipeViewModel

data class ViewState(
    val error: String = "",
    var currentScreen: RecipeViewModel.CurrentScreen = RecipeViewModel.CurrentScreen.Home(),
    var isLoadingAiResponse: Boolean = false
)

data class MealResponse(
    val meals: List<Meal>? = null
)
data class ApiKeyResponse(
    val apiKey: String? = null
)
data class OpenAiResponse(
    val generate: String? = null
)

data class Category(
    val idCategory: String = "",
    val strCategory: String = "",
    val strCategoryThumb: String = "",
    val strCategoryDescription: String = ""
)

data class CategoryResponse(
    val categories: List<Category>
)

data class Meal(
    // val meals: String? = null,
    val idMeal: String = "",
    val strMealThumb: String? = null,
    val strMeal: String? = null,
    var strInstructions: String? = null,
    val strIngredient1: String? = null,
    val strIngredient2: String? = null,
    val strIngredient3: String? = null,
    val strIngredient4: String? = null,
    val strIngredient5: String? = null,
    val strIngredient6: String? = null,
    val strIngredient7: String? = null,
    val strIngredient8: String? = null,
    val strIngredient9: String? = null,
    val strIngredient10: String? = null,
    val strIngredient11: String? = null,
    val strIngredient12: String? = null,
    val strIngredient13: String? = null,
    val strIngredient14: String? = null,
    val strIngredient15: String? = null,
    val strIngredient16: String? = null,
    val strIngredient17: String? = null,
    val strIngredient18: String? = null,
    val strIngredient19: String? = null,
    val strIngredient20: String? = null,

    val strMeasure1: String? = null,
    val strMeasure2: String? = null,
    val strMeasure3: String? = null,
    val strMeasure4: String? = null,
    val strMeasure5: String? = null,
    val strMeasure6: String? = null,
    val strMeasure7: String? = null,
    val strMeasure8: String? = null,
    val strMeasure9: String? = null,
    val strMeasure10: String? = null,
    val strMeasure11: String? = null,
    val strMeasure12: String? = null,
    val strMeasure13: String? = null,
    val strMeasure14: String? = null,
    val strMeasure15: String? = null,
    val strMeasure16: String? = null,
    val strMeasure17: String? = null,
    val strMeasure18: String? = null,
    val strMeasure19: String? = null,
    val strMeasure20: String? = null
)