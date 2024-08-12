package com.example.recipeapp.screens


import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.recipeapp.Category
import com.example.recipeapp.Meal
import com.example.recipeapp.R
import com.example.recipeapp.RecipeViewModel
import com.example.recipeapp.getIngredientsString
import com.example.recipeapp.navigation.AppNav
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
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = {
                    IconButton(onClick = {
                        recipeViewModel.handleBackPress(navController)

                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            "",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        recipeViewModel.updateCurrentScreen(
                            RecipeViewModel.CurrentScreen.Home()
                        )
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "",
                            modifier = Modifier.padding(10.dp),
                            tint = Color.White
                        )
                    }
                }
            )
        }) { values ->
            Surface(
                modifier = Modifier.padding(values),
            ) {
                AppNav(
                    recipeViewModel = recipeViewModel,
                    navController = navController,
                    meals = meals,
                    searchString = searchString,
                    meal = meal,
                    categories = categories
                )
                BackHandler(enabled = true) {
                    recipeViewModel.handleBackPress(navController)
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
            }
        }
    }
}


@Composable
fun CategoryScreen(
    categories: List<Category>,
    onClick: (category: Category) -> Unit
) {
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp)
        ) {
            items(categories) { category ->
                CategoryItem(category = category, onClick)
            }
        }
    }
}


@Composable
fun CategoryItem(category: Category, block: (Category) -> Unit) {

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(5.dp)
            .clickable { block(category) },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            AsyncImage(
                category.strCategoryThumb,
                contentDescription = "null",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
                modifier = Modifier
                    .padding(2.dp).clip(RoundedCornerShape(15.dp)))
            Text(
                text = category.strCategory,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun SearchList(
    list: List<Meal>,
    searchString: String,
    onValueChanged: (String) -> Unit,
    onClick: (meal: Meal) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxHeight()
    ) {
        Log.i("screens", "search = $searchString meals= ${list.size} = $list")
        Spacer(modifier = Modifier.size(10.dp))
        TextField(
            value = searchString,
            label = { Text(text = "Search", style = MaterialTheme.typography.titleMedium) },
            onValueChange = onValueChanged,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp)),
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
                fontSize = 20.sp
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
        )
        Spacer(modifier = Modifier.size(10.dp))
        if (list.isNotEmpty() && list.firstOrNull()?.strMeal?.isNotEmpty() == true) {
            println("list " + list.size)
            LazyColumn() {
                items(list) { meal ->
                    MealItem(meal = meal, onClick)
                }
            }
        }
    }
}

@Composable
fun MealItem(meal: Meal, onClick: (meal: Meal) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .clickable { onClick(meal) }
            .padding(2.dp)
            .clip(RoundedCornerShape(15.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            AsyncImage(
                meal.strMealThumb,
                contentDescription = "null",
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp))//.height(200.dp).width(200.dp)

            )
            Spacer(modifier = Modifier.weight(1f))
            meal.strMeal?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(5.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center

                )
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}


@Composable
fun DetailsScreen(meal: Meal) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(
            rememberScrollState()
        )
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        ) {
            Text(
                text = meal.strMeal ?: "",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Center
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                AsyncImage(
                    meal.strMealThumb,
                    contentDescription = "null",
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp))
                        .size(400.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
                )
            }
            Text(
                text = "Instructions",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Center
            )
            meal.strInstructions?.let { it ->
                val ingredients = getIngredientsString(meal)
                Text(
                    text = "$it \n $ingredients",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(25.dp),
                    textAlign = TextAlign.Left
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun HomeScreen(
    onCategoryClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    meal: Meal = Meal()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.primaryContainer),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(onClick = { /*TODO*/ }, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary), shape = CircleShape,) {
            AsyncImage(
                meal.strMealThumb,
                contentDescription = "null",
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
                modifier = Modifier
                    .size(300.dp).padding(10.dp)
                    .clip(CircleShape)//.height(200.dp).width(200.dp)

            )
        }

        Spacer(modifier = Modifier.size(50.dp))
        Button(
            onClick = { onCategoryClick() }, modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(text = "Categories", style = MaterialTheme.typography.headlineSmall)
        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = { onSearchClick() }, modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(text = "Search", style = MaterialTheme.typography.headlineSmall)
        }
    }
}
