package com.example.recipeapp.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(5.dp)

        ) {
            Text(
                text = meal.strMeal ?: "",
                style = MaterialTheme.typography.headlineSmall.copy(fontFamily = FontFamily(Font(R.font.dancing_script)), color = MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                textAlign = TextAlign.Center
            )
            AsyncImage(
                meal.strMealThumb,
                contentDescription = "null",
                modifier = Modifier
                    .clip(RoundedCornerShape(15.dp))
                    .size(400.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
            )

            AnimatedVisibility(visible = viewState.isLoadingAiResponse) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(25.dp),
                    strokeCap = StrokeCap.Round
                )
            }

            AnimatedVisibility(
                visible = !viewState.isLoadingAiResponse,
            ) {
                meal.strInstructions?.let { it ->
                    val ingredients = getIngredientsString(meal)
                    Text(
                        text = "$it \n $ingredients",
                        style = MaterialTheme.typography.bodyLarge.copy(fontFamily = FontFamily(Font(R.font.dancing_script)), color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.padding(10.dp),
                        textAlign = TextAlign.Left
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !viewState.isLoadingAiResponse,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(5.dp)
        ) {
            FloatingActionButton(
                onClick = { onClick(AppEvent.GenerateOpenAIRecipeEvent) },
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Create,
                    modifier = Modifier.size(25.dp),
                    contentDescription = ""
                )
            }
        }
    }
}
