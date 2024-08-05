package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recipeapp.screens.CategoryScreen
import com.example.recipeapp.screens.DetailsScreen
import com.example.recipeapp.screens.MainApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val recipeViewModel: RecipeViewModel = viewModel()
            val viewState by recipeViewModel.viewState
            MainApp (searchScreen = recipeViewModel::searchScreen,
                randomScreen = recipeViewModel::randomScreen,
                content =
                {
                    when (viewState.currentScreen) {
                        // pass in state to screens
                        is RecipeViewModel.CurrentScreen.Category -> CategoryScreen(viewState = viewState)
                        is RecipeViewModel.CurrentScreen.Detail ->  {}
                        is RecipeViewModel.CurrentScreen.List -> {}
                        is RecipeViewModel.CurrentScreen.Search -> {}
                        null ->// CategoryScreen(viewState = viewState)//
                     DetailsScreen(recipeViewModel = recipeViewModel, viewState = recipeViewModel.viewState)//
                    //CategoryScreen(viewState = viewState)
                    }
                })
        }
    }
}
