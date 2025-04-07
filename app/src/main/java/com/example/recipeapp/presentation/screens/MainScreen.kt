package com.example.recipeapp.presentation.screens


import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.domain.model.ViewState
import com.example.recipeapp.presentation.RecipeViewModel
import com.example.recipeapp.navigation.AppNav
import com.example.recipeapp.navigation.TopLevelRoute
import com.example.recipeapp.presentation.AppError
import com.example.recipeapp.presentation.AppEvent
import com.example.recipeapp.ui.theme.RecipeAppTheme

import com.example.recipeapp.navigation.AppRoute

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
    val generatedRecipe by recipeViewModel.generatedRecipe.collectAsStateWithLifecycle()
    val selectedList by recipeViewModel.selectedList.collectAsStateWithLifecycle()
    val title = when (viewState.currentScreen) {
        is AppRoute.Category -> "Category"
        is AppRoute.Detail -> "Instructions"
        is AppRoute.Search -> "Search"
        is AppRoute.Home -> "Recipe King"
        is AppRoute.Filter -> "Filter"
        is AppRoute.Generate -> "Generate"
        is AppRoute.GeneratedRecipe -> "Generated"
    }

    val topLevelRoutes = listOf(
        TopLevelRoute(
            AppRoute.Category(),
            Icons.AutoMirrored.Filled.List
        ), TopLevelRoute(
            AppRoute.Search(), Icons.Filled.Search
        ), TopLevelRoute(
            AppRoute.Generate(), Icons.Filled.Create
        )
    )
    RecipeAppTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineLarge,
                    )

                },
                navigationIcon = {
                    IconButton(onClick = {
                        recipeViewModel.processEvent(AppEvent.HandleBackPressEvent(navController))

                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            "",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        recipeViewModel.processEvent(
                            AppEvent.UpdateCurrentScreenEvent(
                                AppRoute.Home()
                            )
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "",
                            modifier = Modifier.padding(10.dp),
                        )
                    }
                })

        }, bottomBar = {
            BottomNavigation(backgroundColor = MaterialTheme.colorScheme.primaryContainer) {

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                topLevelRoutes.forEach { topLevelRoute ->
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                topLevelRoute.icon, contentDescription = topLevelRoute.route.title
                            )
                        },
                        label = { Text(topLevelRoute.route.title) },
                        selected = currentDestination?.route == topLevelRoute.route.title,
                        onClick = {
                            recipeViewModel.processEvent(
                                AppEvent.UpdateCurrentScreenEvent(
                                    topLevelRoute.route
                                )
                            )
                        })
                }
            }
        }
        ) { values ->
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
                    processEvent = recipeViewModel::processEvent,
                    generatedRecipe = generatedRecipe,
                    selectedList = selectedList
                )
                BackHandler(enabled = true) {
                    recipeViewModel.processEvent(AppEvent.HandleBackPressEvent(navController))
                }
                when (viewState.currentScreen) {
                    is AppRoute.Category -> navController.navigate(
                        AppRoute.Category().title
                    ) { launchSingleTop = true }

                    is AppRoute.Search -> {
                        navController.navigate(
                            AppRoute.Search().title
                        ) { launchSingleTop = true }
                    }

                    is AppRoute.Detail -> navController.navigate(
                        AppRoute.Detail().title
                    ) { launchSingleTop = true }

                    is AppRoute.Home -> navController.navigate(
                        AppRoute.Home().title
                    ) { launchSingleTop = true }

                    is AppRoute.Filter -> navController.navigate(
                        AppRoute.Filter().title
                    ) { launchSingleTop = true }

                    is AppRoute.Generate -> {
                        navController.navigate(AppRoute.Generate().title) {
                            launchSingleTop = true
                        }
                    }

                    is AppRoute.GeneratedRecipe -> {
                        navController.navigate(AppRoute.GeneratedRecipe().title) {
                            launchSingleTop = true
                        }
                    }
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
                    }) {
                    Text(
                        text = "Confirm", color = Color.White
                    )
                }
            })
    }
}

