package com.example.recipeapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.presentation.AppEvent
import com.example.recipeapp.presentation.RecipeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClick: (event: AppEvent) -> Unit, meal: Meal = Meal()
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
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
          //  shape = CircleShape,
        ) {
            AsyncImage(
                meal.strMealThumb,
                contentDescription = "null",
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
                modifier = Modifier
                    .size(300.dp)
                    .padding(5.dp)
                 //   .clip(CircleShape)//.height(200.dp).width(200.dp)

            )
        }

        Spacer(modifier = Modifier.size(50.dp))
        Button(
            onClick = { onClick(AppEvent.UpdateCurrentScreenEvent(RecipeViewModel.CurrentScreen.Category())) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = "Categories", style = MaterialTheme.typography.headlineSmall)
        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = { onClick(AppEvent.UpdateCurrentScreenEvent(RecipeViewModel.CurrentScreen.Search())) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = "Search", style = MaterialTheme.typography.headlineSmall)
        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = { onClick(AppEvent.UpdateCurrentScreenEvent(RecipeViewModel.CurrentScreen.Generate())) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = "Generate", style = MaterialTheme.typography.headlineSmall)
        }
    }
}
