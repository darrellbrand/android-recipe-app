package com.example.recipeapp.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.recipeapp.Category
import com.example.recipeapp.Meal
import com.example.recipeapp.R
import com.example.recipeapp.RecipeViewModel
import com.example.recipeapp.ViewState
import com.example.recipeapp.getIngredientsString
import com.example.recipeapp.ui.theme.RecipeAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(content: @Composable () -> Unit, searchScreen: () -> Unit, randomScreen: () -> Unit) {
    RecipeAppTheme {
        Scaffold(topBar = {

            TopAppBar(
                title = {
                    Text(
                        text = "Categories",
                        textAlign = TextAlign.Center,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer

                ),
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = searchScreen) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = randomScreen) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        }) {
            Surface(
                modifier = Modifier.padding(it),
            ) {
                content()

            }

        }

    }
}

@Composable
fun CategoryScreen(viewState: ViewState) {
    val list = viewState.categories ?: listOf(Category())
    LazyVerticalGrid(GridCells.Adaptive(150.dp)) {
        items(list) { category ->
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                CategoryItem(category = category) {}
            }
        }
    }
}


@Composable
fun CategoryItem(category: Category, block: (String) -> Unit) {

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(5.dp)
            .clickable { block(category.idCategory) }
            .clip(RoundedCornerShape(15.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.contentColorFor(MaterialTheme.colorScheme.surface)
        )

    ) {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(5.dp)
        ) {

            AsyncImage(
                category.strCategoryThumb,
                contentDescription = "null",
                contentScale = ContentScale.FillBounds,
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
fun RecipeList(viewState: ViewState) {
    val list = viewState.meals ?: listOf(
        Meal()
    )
    LazyColumn() {
        items(list) { meal ->
            MealItem(meal = meal)
        }
    }
}

@Composable
fun MealItem(meal: Meal) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.padding(10.dp)
    ) {
        Card(elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)) {
            AsyncImage(
                meal.strMealThumb,
                contentDescription = "null",
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
            )
            meal.strMeal?.let {
                Text(
                    text = it,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold

                )
            }
        }
    }
}


@Composable
fun DescriptionScreen(viewState: ViewState, recipeViewModel: RecipeViewModel) {
    val defaultText = "Recipe search here"
    var text by rememberSaveable { mutableStateOf(defaultText) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {


        AsyncImage(
            viewState.meal?.strMealThumb,
            contentDescription = "null",
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .fillMaxWidth()
                .weight(9f),
            contentScale = ContentScale.FillBounds,
            placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row() {
            OutlinedTextField(value = text,
                onValueChange = {
                    text = it
                },
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged {
                        if (it.isFocused && text == defaultText) {
                            // Clear default text when the text field is focused
                            text = ""
                        } else if (!it.isFocused && text != defaultText) {
                            text = ""
                        }
                    })
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Button(
                onClick = {
                    recipeViewModel.fetchRandomMeal()
                },
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "random recipe",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = {

                    if (!text.isNullOrBlank() && text != defaultText) {
                        recipeViewModel.fetchSearchMeal(text)
                    }
                },
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "search recipe",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = {
                    recipeViewModel.seeDetails()
                },
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = "see details",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }

        }

    }

}

@Composable
fun DetailsScreen(recipeViewModel: RecipeViewModel, viewState: State<ViewState>) {
        val ingredients = viewState.value.meal?.let { getIngredientsString(it) }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.verticalScroll(
            rememberScrollState())) {

            Card( elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .padding(15.dp).
                    fillMaxWidth().align(Alignment.CenterHorizontally)) {

                Box (contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()){
                    AsyncImage(
                        viewState.value.meal?.strMealThumb,
                        contentDescription = "null",
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                          .padding(5.dp),
                        contentScale = ContentScale.FillBounds,
                        placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
                    )
                }

            }
            Card( elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.padding(5.dp)) {
                Text(
                    text = "Instructions",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                viewState.value.meal?.strInstructions?.let { it ->
                    Text(
                        text = "$it \n $ingredients",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }


