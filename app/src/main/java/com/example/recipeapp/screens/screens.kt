package com.example.recipeapp.screens


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.recipeapp.Category
import com.example.recipeapp.Meal
import com.example.recipeapp.R
import com.example.recipeapp.RecipeViewModel
import com.example.recipeapp.ViewState
import com.example.recipeapp.getIngredientsString
import com.example.recipeapp.navigation.AppNav
import com.example.recipeapp.ui.theme.RecipeAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
) {
    val navController = rememberNavController()
    val recipeViewModel: RecipeViewModel = viewModel()
    val categories by recipeViewModel.categories.collectAsState()
    val viewState by recipeViewModel.viewState.collectAsState()
    val meals by recipeViewModel.meals.collectAsState(initial = listOf(Meal()))
    val searchString by recipeViewModel.searchString.collectAsState()
    val meal by recipeViewModel.meal.collectAsState()
    val title = when (viewState.currentScreen) {
        is RecipeViewModel.CurrentScreen.Category -> "Recipe Category"
        is RecipeViewModel.CurrentScreen.Detail -> "Recipe Instructions"
        is RecipeViewModel.CurrentScreen.Search -> "Recipe Search"
        is RecipeViewModel.CurrentScreen.Home -> "Recipe King"
        is RecipeViewModel.CurrentScreen.Filter -> "Recipe Filter"
    }
    RecipeAppTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Text(text = title)

                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                navigationIcon = {
                    IconButton(onClick = {
                        handleBackPress(navController, recipeViewModel)

                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            ""
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
                            modifier = Modifier.padding(10.dp)
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
                    handleBackPress(navController, recipeViewModel)
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

fun handleBackPress(navController: NavHostController, recipeViewModel: RecipeViewModel) {
    val ret = navController.popBackStack()
    println(ret)
    navController.currentBackStackEntry?.destination?.route?.let {

        when (it) {
            RecipeViewModel.CurrentScreen.Category().title -> {
                recipeViewModel.updateCurrentScreen(RecipeViewModel.CurrentScreen.Category())
            }

            RecipeViewModel.CurrentScreen.Search().title -> {
                recipeViewModel.updateCurrentScreen(RecipeViewModel.CurrentScreen.Search())
            }

            RecipeViewModel.CurrentScreen.Detail().title -> {
                recipeViewModel.updateCurrentScreen(RecipeViewModel.CurrentScreen.Detail())
            }

            RecipeViewModel.CurrentScreen.Filter().title -> {
                recipeViewModel.updateCurrentScreen(RecipeViewModel.CurrentScreen.Filter())
            }
            RecipeViewModel.CurrentScreen.Home().title -> {
                recipeViewModel.updateCurrentScreen(RecipeViewModel.CurrentScreen.Home())
            }
        }

    }
}

@Composable
fun CategoryScreen(
    categories: List<Category>,
    onClick: (category: Category) -> Unit
) {
    LazyVerticalGrid(GridCells.Adaptive(150.dp)) {
        items(categories) { category ->
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
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
            .clickable { block(category) }
            .clip(RoundedCornerShape(15.dp)),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(5.dp)
        ) {

            AsyncImage(
                category.strCategoryThumb,
                contentDescription = "null",
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
                modifier = Modifier
                    .padding(5.dp)
                    .clip(RoundedCornerShape(15.dp))
            )
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

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Log.i("screens", "search = $searchString meals= ${list.size} = $list")
        TextField(
            value = searchString,
            label = { Text(text = "Search", style = MaterialTheme.typography.titleMedium) },
            onValueChange = onValueChanged,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .padding(2.dp),
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
            .padding(5.dp)) {
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
    val ingredients = getIngredientsString(meal)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(
            rememberScrollState()
        )
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .padding(15.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = meal.strMeal ?: "",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Center
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                AsyncImage(
                    meal.strMealThumb,
                    contentDescription = "null",
                    modifier = Modifier
                        .clip(RoundedCornerShape(15.dp)),
                    contentScale = ContentScale.FillBounds,
                    placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
                )
            }
            Text(
                text = "Instructions",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Center
            )
            meal.strInstructions?.let { it ->
                Text(
                    text = "$it \n $ingredients",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }
    }
}

@Preview
@Composable
fun HomeScreen(onCategoryClick: () -> Unit = {}, onSearchClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_color),
            contentDescription = "",
            modifier = Modifier
                .clip(
                    RoundedCornerShape(15.dp)
                )
                .size(200.dp)
        )
        Button(
            onClick = { onCategoryClick() }, modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(text = "Recipe Categories")
        }
        Button(
            onClick = { onSearchClick() }, modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Text(text = "Recipe Search")
        }
    }

}
