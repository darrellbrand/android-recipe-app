package com.example.recipeapp.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.toColor
import coil.compose.AsyncImage
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.ViewState
import com.example.recipeapp.presentation.AppEvent
import com.example.recipeapp.presentation.Ingredients

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GenerateScreen(
    viewState: ViewState,
    block: (appEvent: AppEvent) -> Unit = {},

    ) {
    val scrollState = rememberScrollState()
    var selectedList by rememberSaveable { mutableStateOf(emptyList<String>()) }

    fun toggleIngredient(ingredient: String) {
        selectedList =
            if (selectedList.contains(ingredient)) selectedList - ingredient else selectedList + ingredient
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = !viewState.isLoadingAiResponse) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                Text(
                    text = "Meat",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.dancing_script)),
                    )
                )
                FlowRow {
                    Ingredients.MEAT_LIST.forEach {

                        FilterChip(
                            selected = selectedList.contains(it), onClick = {
                                toggleIngredient(it)
                            }, label = {
                                Text(
                                    text = it,
                                    style = TextStyle(fontFamily = FontFamily(Font(R.font.dancing_script))),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }, modifier = Modifier.padding(2.dp), leadingIcon = {
                                if (selectedList.contains(it)) {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "chip selected"
                                    )
                                }
                            }, colors = FilterChipDefaults.filterChipColors()
                        )
                    }
                }
                Text(
                    text = "Dairy",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.dancing_script)),
                    )
                )
                FlowRow {
                    Ingredients.DAIRY_LIST.forEach {
                        FilterChip(
                            selected = selectedList.contains(it),
                            onClick = {
                                toggleIngredient(it)
                            },
                            label = {
                                Text(
                                    text = it,
                                    style = TextStyle(fontFamily = FontFamily(Font(R.font.dancing_script))),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            },
                            leadingIcon = {
                                if (selectedList.contains(it)) {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "chip selected"
                                    )
                                }
                            },
                            modifier = Modifier.padding(2.dp),
                        )
                    }
                }
                Text(
                    text = "Vegetable",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.dancing_script)),
                    )
                )
                FlowRow {
                    Ingredients.VEGETABLE_LIST.forEach {
                        FilterChip(
                            selected = selectedList.contains(it), onClick = {
                                toggleIngredient(it)

                            }, label = {
                                Text(
                                    text = it,
                                    style = TextStyle(fontFamily = FontFamily(Font(R.font.dancing_script))),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }, leadingIcon = {
                                if (selectedList.contains(it)) {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "chip selected"
                                    )
                                }
                            }, modifier = Modifier.padding(2.dp)
                        )
                    }
                }
                Text(
                    text = "Fruit",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 30.sp,
                        fontFamily = FontFamily(Font(R.font.dancing_script))
                    )
                )
                FlowRow {
                    Ingredients.FRUIT_LIST.forEach {
                        FilterChip(
                            selected = selectedList.contains(it),
                            onClick = {
                                toggleIngredient(it)
                            },
                            label = {
                                Text(
                                    text = it,
                                    style = TextStyle(fontFamily = FontFamily(Font(R.font.dancing_script)), color = MaterialTheme.colorScheme.primary)
                                )
                            },
                            leadingIcon = {
                                if (selectedList.contains(it)) {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "chip selected"
                                    )
                                }
                            },
                            modifier = Modifier.padding(2.dp)
                        )
                    }
                }
            }
        }
        AnimatedVisibility(
            visible = viewState.isLoadingAiResponse, modifier = Modifier.align(Alignment.Center)
        ) {
            Column() {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .height(30.dp),
                    strokeCap = StrokeCap.Round
                )
                Image(
                    painter = painterResource(id = R.drawable.page_1),
                    contentDescription = "",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Fit
                )
            }
        }

        AnimatedVisibility(
            visible = !viewState.isLoadingAiResponse, modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            FloatingActionButton(
                onClick = { block(AppEvent.GenerateOpenAICustomRecipeEvent(selectedList)) },
                modifier = Modifier
                    .padding(10.dp)
                    .systemBarsPadding()
                    .imePadding()
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    modifier = Modifier.size(25.dp)
                )
            }

        }

    }

}


