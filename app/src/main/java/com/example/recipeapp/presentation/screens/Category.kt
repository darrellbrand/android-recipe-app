package com.example.recipeapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.presentation.AppEvent

@Composable
fun CategoryScreen(
    categories: List<Category>, onClick: (event: AppEvent) -> Unit
) {
    Box(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp)
        ) {
            items(categories, key = { it.idCategory }) { category ->
                CategoryItem(category = category) {
                    onClick(AppEvent.LoadListFromCategoryEvent(category))
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: Category, block: (Category) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.clickable { block(category) }
    ) {
        AsyncImage(
            category.strCategoryThumb,
            contentDescription = "null",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
            modifier = Modifier
                .padding(10.dp)
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


