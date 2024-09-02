package com.example.recipeapp.presentation.screens


import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.domain.model.ViewState
import com.example.recipeapp.presentation.RecipeViewModel
import com.example.recipeapp.navigation.AppNav
import com.example.recipeapp.presentation.AppError
import com.example.recipeapp.presentation.AppEvent
import com.example.recipeapp.ui.theme.RecipeAppTheme


@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun MainApp(
) {
    val navController = rememberNavController()
    val recipeViewModel: RecipeViewModel = viewModel()
    val categories by recipeViewModel.categories.collectAsStateWithLifecycle()
    val viewState by recipeViewModel.viewState.collectAsStateWithLifecycle()
    val meals by recipeViewModel.meals.collectAsStateWithLifecycle(initialValue = listOf(Meal()))
    val searchString by recipeViewModel.searchString.collectAsStateWithLifecycle()
    val meal by recipeViewModel.meal.collectAsStateWithLifecycle()
    val title = when (viewState.currentScreen) {
        is RecipeViewModel.CurrentScreen.Category -> "Category"
        is RecipeViewModel.CurrentScreen.Detail -> "Instructions"
        is RecipeViewModel.CurrentScreen.Search -> "Search"
        is RecipeViewModel.CurrentScreen.Home -> "Recipe King"
        is RecipeViewModel.CurrentScreen.Filter -> "Filter"
    }
    RecipeAppTheme {
        Scaffold(topBar = {
            TopAppBar(title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )

            },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = {
                        recipeViewModel.processEvent(AppEvent.HandleBackPressEvent(navController))

                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            "",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        recipeViewModel.processEvent(
                            AppEvent.UpdateCurrentScreenEvent(
                                RecipeViewModel.CurrentScreen.Home()
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "",
                            modifier = Modifier.padding(10.dp),
                            tint = Color.White
                        )
                    }
                })
        }) { values ->
            Surface(
                modifier = Modifier.padding(values),
            ) {
                AppNav(
                    viewState = viewState,
                    navController = navController,
                    meals = meals,
                    searchString = searchString,
                    meal = meal,
                    categories = categories,
                    processEvent = recipeViewModel::processEvent
                )
                BackHandler(enabled = true) {
                    recipeViewModel.processEvent(AppEvent.HandleBackPressEvent(navController))
                }
                when (viewState.currentScreen) {
                    is RecipeViewModel.CurrentScreen.Category -> navController.navigate(
                        RecipeViewModel.CurrentScreen.Category().title
                    ) { launchSingleTop = true }

                    is RecipeViewModel.CurrentScreen.Search -> {
                        navController.navigate(
                            RecipeViewModel.CurrentScreen.Search().title
                        ) { launchSingleTop = true }
                    }

                    is RecipeViewModel.CurrentScreen.Detail -> navController.navigate(
                        RecipeViewModel.CurrentScreen.Detail().title
                    ) { launchSingleTop = true }

                    is RecipeViewModel.CurrentScreen.Home -> navController.navigate(
                        RecipeViewModel.CurrentScreen.Home().title
                    ) { launchSingleTop = true }

                    is RecipeViewModel.CurrentScreen.Filter -> navController.navigate(
                        RecipeViewModel.CurrentScreen.Filter().title
                    ) { launchSingleTop = true }
                }
                MyAlertDialog(viewState = viewState) { recipeViewModel.processEvent(AppEvent.InitEvent) }
            }
        }
    }
}


@Composable
fun MyAlertDialog(viewState: ViewState, onClick: () -> Unit) {
    if (viewState.appErrors.any { it is AppError.ApiKeyNetworkError || it is AppError.NetworkError }) { // 2
        AlertDialog(
            onDismissRequest = {
                onClick()
            },
            title = { Text(text = "Server Connection Failed") },
            text = { Text(text = "Can't connect to server. Please try Again") },
            confirmButton = {
                Button(
                    onClick = {
                        onClick()
                    }
                ) {
                    Text(
                        text = "Confirm",
                        color = Color.White
                    )
                }
            }
        )
    }
}

