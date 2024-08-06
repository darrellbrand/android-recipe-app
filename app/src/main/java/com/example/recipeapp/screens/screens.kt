package com.example.recipeapp.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.recipeapp.Category
import com.example.recipeapp.Meal
import com.example.recipeapp.R
import com.example.recipeapp.ViewState
import com.example.recipeapp.getIngredientsString
import com.example.recipeapp.ui.theme.RecipeAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(content: @Composable () -> Unit) {
    RecipeAppTheme {
        Scaffold(topBar = {

            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Categories",
                            textAlign = TextAlign.Center,
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(

                ),
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                /*actions = {
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
                },*/
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
fun CategoryScreen(viewState: ViewState,categories : List<Category>) {
    val list = categories
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
fun SearchList(list : List<Meal>, searchString : String, onValueChanged :  (String) -> Unit) {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextField(
                value = searchString,
                label = { Text(text = "search") },
                onValueChange = onValueChanged,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp
                )
            )
        LazyColumn() {
            items(list) { meal ->
                MealItem(meal = meal)
            }
        }
    }
}

@Composable
fun MealItem(meal: Meal) {

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .clickable { }
            .padding(10.dp)) {


        Column(
           horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                meal.strMealThumb,
                contentDescription = "null",
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))//.height(200.dp).width(200.dp)

            )

            meal.strMeal?.let {
                Text(
                    text = it,
                    modifier = Modifier
                        .padding(5.dp),
                    style = MaterialTheme.typography.headlineSmall

                )
            }
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
                style = MaterialTheme.typography.titleLarge,
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


