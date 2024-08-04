package com.example.recipeapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement



import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.recipeapp.Category
import com.example.recipeapp.Meal
import com.example.recipeapp.R
import com.example.recipeapp.ViewState
import com.example.recipeapp.ui.theme.RecipeAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(content: @Composable () -> Unit) {
    RecipeAppTheme {
        Scaffold(topBar = {

            TopAppBar(


                title = {
                    Text(
                        text = "Recipe King",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer

                ),
                navigationIcon =
                {
                    IconButton({}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "radness"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {  }, enabled = true) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = "rad"
                        )
                    }
                }
            )
        }) {
            Surface(modifier = Modifier.padding(it)) {
                content()
            }

        }

    }
}


@Composable
fun CategoryScreen(viewState: ViewState) {
    val list = viewState.categories ?: listOf(Category())
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(list) { category ->
            CategoryItem(category = category) {}
        }
    }
}


@Composable
fun CategoryItem(category: Category, block: (String) -> Unit) {

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .padding(5.dp)
            .clickable { block(category.idCategory) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                category.strCategoryThumb,
                contentDescription = "null",
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
                modifier = Modifier
                    .padding(
                        5.dp
                    )
                    .weight(.5f)
            )

            Text(
                text = category.strCategory,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)

            )


            // Spacer(modifier = Modifier.weight(1f))

        }
    }
}

@Composable
fun RecipeList(viewState: ViewState) {
    val list = viewState.meals ?: listOf(
        Meal(
            null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, null,
            null, null, null, null, null,
            null, null, null, null, null,
            null, null, null, null, null,
            null, null, null, null, null,
            null, null, null, null
        )
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

