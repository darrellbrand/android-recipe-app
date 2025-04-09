package com.example.recipeapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.presentation.AppEvent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClick: (event: AppEvent) -> Unit, meal: Meal = Meal()
) {
    val cursive = FontFamily(Font(R.font.dancing_script))
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {

        AsyncImage(
            meal.strMealThumb,
            contentDescription = "null",
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = "Recipe King", style = TextStyle(
                color = MaterialTheme.colorScheme.primary, fontSize = 120.sp, fontFamily = cursive,
                textAlign = TextAlign.Center
            ), modifier = Modifier.align(Alignment.TopStart)
        )

    }

}

