package com.example.recipeapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
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
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            ),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = R.drawable.page_1),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.size(50.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { onClick(AppEvent.UpdateCurrentScreenEvent(RecipeViewModel.CurrentScreen.Category())) },
                    modifier = Modifier
                        .padding(5.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.list_round_bullet_icon),
                        contentDescription = "categories",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Spacer(modifier = Modifier.size(5.dp))
                Text(
                    text = "Categories",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { onClick(AppEvent.UpdateCurrentScreenEvent(RecipeViewModel.CurrentScreen.Search())) },
                    modifier = Modifier

                        .padding(5.dp)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.binoculars_icon),
                        contentDescription = "search",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Text(
                    text = "Search",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.size(5.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { onClick(AppEvent.UpdateCurrentScreenEvent(RecipeViewModel.CurrentScreen.Generate())) },
                    modifier = Modifier
                        .padding(5.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.pencil_icon),
                        contentDescription = "generate",
                        modifier = Modifier.size(50.dp)
                    )
                }
                Text(
                    text = "Generate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.bottom_graphic),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        )
    }

}
