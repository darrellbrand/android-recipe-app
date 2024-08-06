package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipeapp.screens.CategoryScreen
import com.example.recipeapp.screens.DetailsScreen
import com.example.recipeapp.screens.MainApp
import com.example.recipeapp.screens.SearchList


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val recipeViewModel: RecipeViewModel = viewModel()
            val categories by recipeViewModel.categories.collectAsState()
            val viewState by recipeViewModel.viewState.collectAsState()
            val meals by recipeViewModel.meals.collectAsState(initial = listOf(Meal()))
            val searchString by recipeViewModel.searchString.collectAsState()
            val meal by recipeViewModel.meal.collectAsState()
            MainApp(
                content =
                {
                    when (viewState.currentScreen) {
                        // pass in state to screens
                        is RecipeViewModel.CurrentScreen.Search -> SearchList(
                            list = meals,
                            searchString = searchString,
                            onValueChanged = recipeViewModel::onValueChanged
                        ) //CategoryScreen(categories = categories, viewState = viewState)
                        is RecipeViewModel.CurrentScreen.Detail -> {
                            DetailsScreen(meal = meal)
                        }
                        is RecipeViewModel.CurrentScreen.Category -> CategoryScreen(viewState,
                            categories
                        ) //CategoryScreen(categories = categories, viewState = viewState)
                        //  null ->// CategoryScreen(viewState = viewState)//
                        // DetailsScreen( viewState = recipeViewModel.viewState)//
                        //CategoryScreen(viewState = viewState)
                        // RecipeList( viewState = recipeViewModel.viewState)//
                    }
                })
        }
    }
}
