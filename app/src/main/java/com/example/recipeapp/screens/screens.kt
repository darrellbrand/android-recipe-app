package com.example.recipeapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.recipeapp.Category
import com.example.recipeapp.CategoryResponse
import com.example.recipeapp.DescriptionScreen
import com.example.recipeapp.DetailsScreen
import com.example.recipeapp.R
import com.example.recipeapp.RecipeViewModel
import com.example.recipeapp.ViewState
import com.example.recipeapp.ui.theme.RecipeAppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun MainScreen() {
    val recipeViewModel: RecipeViewModel = viewModel()
    val viewState by recipeViewModel.viewState


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(content: @Composable () -> Unit) {
    RecipeAppTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = { Text(text = "Recipes") }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        }) {
            Surface(modifier = Modifier.padding(it)) {
                content()
            }

        }

    }
}

@Preview
@Composable
fun CategoryScreen() {
    val recipeViewModel: RecipeViewModel = viewModel()
    val viewState by recipeViewModel.viewState
    val list: List<Category> = viewState.categories ?: listOf(Category(null, null, null, null))
    Column() {
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp)) {
            items(list) { category ->
                CategoryItem(category = category)
            }

        }
    }

}

@Composable
fun CategoryItem(category: Category) {
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(10.dp)
    ) {
        Card (elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)){
            AsyncImage(
                category.strCategoryThumb,
                contentDescription = "null",
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
            )
            category.strCategory?.let { Text(text = it,modifier = Modifier.align(Alignment.CenterHorizontally), fontWeight = FontWeight.Bold) }
        }
    }


}
