package com.example.recipeapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.domain.model.ViewState
import com.example.recipeapp.presentation.AppEvent
import com.example.recipeapp.presentation.RecipeViewModel
import com.example.recipeapp.presentation.screens.CategoryScreen
import com.example.recipeapp.presentation.screens.DetailsScreen
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
    processEvent: (event: AppEvent) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = RecipeViewModel.CurrentScreen.Home().title
    ) {

        composable(RecipeViewModel.CurrentScreen.Category().title) {
            CategoryScreen(
                categories = categories,
                processEvent
            )

        }
        composable(RecipeViewModel.CurrentScreen.Detail().title) {
            DetailsScreen(
                meal = meal,
                processEvent, viewState = viewState
            )

        }
        composable(RecipeViewModel.CurrentScreen.Search().title) {
            SearchList(
                list = meals,
                searchString = searchString,
                onClick = processEvent
            )
        }
        composable(RecipeViewModel.CurrentScreen.Filter().title) {
            SearchList(
                list = meals,
                searchString = searchString,
                onClick = processEvent
            )
        }
        composable(RecipeViewModel.CurrentScreen.Home().title) {
            HomeScreen(
                processEvent, meal
            )
        }
    }
}



