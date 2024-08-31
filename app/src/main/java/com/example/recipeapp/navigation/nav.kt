package com.example.recipeapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.presentation.RecipeViewModel
import com.example.recipeapp.screens.CategoryScreen
import com.example.recipeapp.screens.DetailsScreen
import com.example.recipeapp.screens.HomeScreen
import com.example.recipeapp.screens.SearchList


@Composable
fun AppNav(
    recipeViewModel: RecipeViewModel,
    navController: NavHostController,
    meals: List<Meal>,
    searchString: String,
    meal: Meal,
    categories: List<Category>
) {
    NavHost(
        navController = navController,
        startDestination = RecipeViewModel.CurrentScreen.Home().title
    ) {
        composable(RecipeViewModel.CurrentScreen.Category().title) {
            CategoryScreen(
                categories = categories,
                recipeViewModel::processEvent
            )

        }
        composable(RecipeViewModel.CurrentScreen.Detail().title) {
            DetailsScreen(
                meal = meal,
                recipeViewModel::processEvent
            )

        }
        composable(RecipeViewModel.CurrentScreen.Search().title) {
            SearchList(
                list = meals,
                searchString = searchString,
                onClick = recipeViewModel::processEvent
            )
        }
        composable(RecipeViewModel.CurrentScreen.Filter().title) {
            SearchList(
                list = meals,
                searchString = searchString,
                onClick = recipeViewModel::processEvent
            )
        }
        composable(RecipeViewModel.CurrentScreen.Home().title) {
            HomeScreen(
                recipeViewModel::processEvent
                , meal
            )
        }
    }
}
//work on navigation


