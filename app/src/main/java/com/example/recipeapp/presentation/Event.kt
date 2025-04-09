package com.example.recipeapp.presentation

import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.navigation.AppRoute

sealed class AppEvent {
    data class UpdateCurrentScreenEvent(val screen: AppRoute) : AppEvent()
    data class OnValueChangedEvent(val search: TextFieldValue) : AppEvent()
    data class LoadListFromCategoryEvent(val category: Category) : AppEvent()
    data class LoadDetailFromMealEvent(val meal: Meal) : AppEvent()
    data class HandleBackPressEvent(val navController: NavHostController) : AppEvent()
    data object GenerateOpenAIRecipeEvent : AppEvent()
    data class GenerateOpenAICustomRecipeEvent(val selectedList : List<String>) : AppEvent()
    data object InitEvent : AppEvent()
}