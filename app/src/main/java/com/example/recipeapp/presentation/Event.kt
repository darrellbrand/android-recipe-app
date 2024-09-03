package com.example.recipeapp.presentation

import androidx.navigation.NavHostController
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Meal

sealed class AppEvent {
    data class UpdateCurrentScreenEvent(val screen: RecipeViewModel.CurrentScreen) : AppEvent()
    data class OnValueChangedEvent(val search: String) : AppEvent()
    data class LoadListFromCategoryEvent(val category: Category) : AppEvent()
    data class LoadDetailFromMealEvent(val meal: Meal) : AppEvent()
    data class HandleBackPressEvent(val navController: NavHostController) : AppEvent()
    data object GenerateOpenAIRecipeEvent : AppEvent()
    data object GenerateOpenAICustomRecipeEvent : AppEvent()
    data class ToggleIngredientEvent(val ingredient: String) : AppEvent()
    data object InitEvent : AppEvent()
}