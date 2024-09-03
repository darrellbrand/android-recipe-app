package com.example.recipeapp.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.recipeapp.domain.model.ViewState
import com.example.recipeapp.presentation.AppEvent
import com.example.recipeapp.presentation.Ingredients

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenerateScreen(
    viewState: ViewState,
    selectedList: List<String> = emptyList(),
    block: (appEvent: AppEvent) -> Unit = {},
    generatedRecipe: String = " recipe here"
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        Card {
            Text(
                text = "Meat",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            FlowRow {
                Ingredients.MEAT_LIST.forEach {
                    FilterChip(selected = selectedList.contains(it), onClick = {
                        block(
                            AppEvent.ToggleIngredientEvent(it)
                        )
                    }, label = { Text(text = it) }, modifier = Modifier.padding(2.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            // containerColor = MaterialTheme.colorScheme.primaryContainer,
                            //   selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
            HorizontalDivider()
            Text(
                text = "Dairy",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            FlowRow {
                Ingredients.DAIRY_LIST.forEach {
                    FilterChip(
                        selected = selectedList.contains(it),
                        onClick = { block(AppEvent.ToggleIngredientEvent(it)) },
                        label = { Text(text = it) },
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
            HorizontalDivider()
            Text(
                text = "Vegetable",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            FlowRow {
                Ingredients.VEGETABLE_LIST.forEach {
                    FilterChip(
                        selected = selectedList.contains(it),
                        onClick = { block(AppEvent.ToggleIngredientEvent(it)) },
                        label = { Text(text = it) },
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
            HorizontalDivider()
            Text(
                text = "Fruit",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            )
            FlowRow {
                Ingredients.FRUIT_LIST.forEach {
                    FilterChip(
                        selected = selectedList.contains(it),
                        onClick = { block(AppEvent.ToggleIngredientEvent(it)) },
                        label = { Text(text = it) },
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
            HorizontalDivider()
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
                    Button(
                        onClick = { block(AppEvent.GenerateOpenAICustomRecipeEvent) },
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(
                            text = "Create Custom Recipe with AI   ",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "")
                    }
                }
            }
            HorizontalDivider()
            if (generatedRecipe.isNotEmpty()) {
                Text(
                    text = "Instructions",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = generatedRecipe,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(25.dp),
                    textAlign = TextAlign.Left
                )
            }
        }
    }

}