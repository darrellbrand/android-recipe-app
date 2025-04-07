package com.example.recipeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.domain.model.ViewState
import com.example.recipeapp.presentation.AppEvent
import com.example.recipeapp.presentation.screens.CategoryScreen
import com.example.recipeapp.presentation.screens.DetailsScreen
import com.example.recipeapp.presentation.screens.GenerateScreen
import com.example.recipeapp.presentation.screens.GeneratedRecipeScreen
import com.example.recipeapp.presentation.screens.HomeScreen
import com.example.recipeapp.presentation.screens.SearchList


@Composable
fun AppNav(
    viewState: ViewState,
    navController: NavHostController,
    meals: List<Meal>,
    searchString: String,
    meal: Meal,
    categories: List<Category>,
    processEvent: (event: AppEvent) -> Unit,
    selectedList: List<String>,
    generatedRecipe: String
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Home().title
    ) {

        composable(AppRoute.Category().title) {
            CategoryScreen(
                categories = categories,
                processEvent
            )

        }
        composable(AppRoute.Detail().title) {
            DetailsScreen(
                meal = meal,
                processEvent, viewState = viewState
            )

        }
        composable(AppRoute.Search().title) {
            SearchList(
                list = meals,
                searchString = searchString,
                onClick = processEvent, viewState = viewState
            )
        }
        composable(AppRoute.Filter().title) {
            SearchList(
                list = meals,
                searchString = searchString,
                onClick = processEvent,
                viewState = viewState,
            )
        }
        composable(AppRoute.Home().title) {
            HomeScreen(
                processEvent, meal
            )
        }
        composable(AppRoute.Generate().title) {
            GenerateScreen(
                viewState = viewState,
                block = processEvent
            )
        }
        composable(AppRoute.GeneratedRecipe().title) {
            GeneratedRecipeScreen(generatedRecipe = generatedRecipe)
        }
    }
}



