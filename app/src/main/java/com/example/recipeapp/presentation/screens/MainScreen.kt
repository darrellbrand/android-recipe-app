package com.example.recipeapp.presentation.screens


import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.domain.model.ViewState
import com.example.recipeapp.navigation.AppNav
import com.example.recipeapp.navigation.AppRoute
import com.example.recipeapp.navigation.TopLevelRoute
import com.example.recipeapp.presentation.AppError
import com.example.recipeapp.presentation.AppEvent
import com.example.recipeapp.presentation.RecipeViewModel
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
    val generatedRecipe by recipeViewModel.generatedRecipe.collectAsStateWithLifecycle()
    val selectedList by recipeViewModel.selectedList.collectAsStateWithLifecycle()
    val title = when (viewState.currentScreen) {
        is AppRoute.Category -> "Category"
        is AppRoute.Detail -> "Instructions"
        is AppRoute.Search -> "Search"
        is AppRoute.Home -> "Home"
        is AppRoute.Filter -> "Filter"
        is AppRoute.Generate -> "Generate"
        is AppRoute.GeneratedRecipe -> "Generated"
    }
    var selectedIndex by rememberSaveable { mutableIntStateOf(-1) }
    val topLevelRoutes = listOf(
        TopLevelRoute(
            AppRoute.Category(), Icons.AutoMirrored.Filled.List
        ), TopLevelRoute(
            AppRoute.Search(), Icons.Filled.Search
        ), TopLevelRoute(
            AppRoute.Generate(), Icons.Filled.Create
        )
    )
    RecipeAppTheme {
        Scaffold(topBar = {
            TopAppBar(
                modifier = Modifier
                    .shadow(20.dp)
                    .background(MaterialTheme.colorScheme.surface),
                title = {
                    Text(
                        text = title,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 30.sp,
                            fontFamily = FontFamily.Cursive
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(
                        enabled = viewState.currentScreen.title != AppRoute.Home().title,
                        onClick = {
                            recipeViewModel.processEvent(AppEvent.HandleBackPressEvent(navController))
                        }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            "",
                        )
                    }
                },
                actions = {
                    IconButton(
                        enabled = viewState.currentScreen.title != AppRoute.Home().title,
                        onClick = {
                            recipeViewModel.processEvent(
                                AppEvent.UpdateCurrentScreenEvent(
                                    AppRoute.Home()
                                )
                            )
                        }) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "",
                            modifier = Modifier
                                .padding(10.dp)
                                .size(25.dp),
                        )
                    }
                })
        }, bottomBar = {
            NavigationBar(
            ) {
                topLevelRoutes.forEach { topLevelRoute ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                topLevelRoute.icon, contentDescription = topLevelRoute.route.title
                            )
                        },
                        label = { Text(topLevelRoute.route.title.lowercase()) },
                        selected = viewState.currentScreen.title == topLevelRoute.route.title,
                        onClick = {
                            recipeViewModel.processEvent(
                                AppEvent.UpdateCurrentScreenEvent(
                                    topLevelRoute.route
                                )
                            )
                        })
                }
            }
        }) { values ->
            Surface(
                modifier = Modifier
                    .padding(values)
                    .imePadding()
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
                    ) {
                        launchSingleTop = true
                    }

                    is AppRoute.Search -> {
                        navController.navigate(
                            AppRoute.Search().title
                        ) {
                            launchSingleTop = true
                        }
                    }

                    is AppRoute.Detail -> navController.navigate(
                        AppRoute.Detail().title
                    ) {
                        launchSingleTop = true
                    }

                    is AppRoute.Home -> navController.navigate(
                        AppRoute.Home().title
                    ) {
                        launchSingleTop = true
                    }

                    is AppRoute.Filter -> navController.navigate(
                        AppRoute.Filter().title
                    ) {
                        launchSingleTop = true
                    }

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

