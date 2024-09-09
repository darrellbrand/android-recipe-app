package com.example.recipeapp.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.domain.model.ViewState
import com.example.recipeapp.presentation.AppEvent
import com.example.recipeapp.util.getIngredientsString


@Composable
fun DetailsScreen(
    meal: Meal, onClick: (event: AppEvent) -> Unit, viewState: ViewState
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .verticalScroll(
                rememberScrollState()
            )
            .background(
                MaterialTheme.colorScheme.primaryContainer
            )
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
            contentAlignment = Alignment.Center, modifier = Modifier
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
        AnimatedVisibility(visible = viewState.isLoadingAiResponse) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(15.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    trackColor = MaterialTheme.colorScheme.primary,
                    strokeCap = StrokeCap.Round
                )
            }
        }
        AnimatedVisibility(visible = !viewState.isLoadingAiResponse) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Button(
                        onClick = { onClick(AppEvent.GenerateOpenAIRecipeEvent) },
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.pencil_icon),
                            modifier = Modifier.size(50.dp),
                            contentDescription = ""
                        )
                    }
                    Text(text = "Simplify with AI   ", style = MaterialTheme.typography.bodyMedium)
                }
            }
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