package com.example.recipeapp.presentation.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.domain.model.ViewState
import com.example.recipeapp.presentation.AppEvent
import com.example.recipeapp.presentation.RecipeViewModel


@Composable
fun SearchList(
    viewState: ViewState, list: List<Meal>, searchString: String, onClick: (event: AppEvent) -> Unit
) {


    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxHeight()
    ) {
        //Log.i("screens", "search = $searchString meals= ${list.size} = $list")
        val text =
            if (viewState.currentScreen is RecipeViewModel.CurrentScreen.Search) "Search" else "Filter"
        TextField(
            value = searchString,
            label = { Text(text = text, style = MaterialTheme.typography.titleMedium) },
            onValueChange = {
                onClick(
                    AppEvent.OnValueChangedEvent(it)
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                textAlign = TextAlign.Start, fontSize = 20.sp
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = null
                )
            },
            maxLines = 1
        )
        if (list.isNotEmpty() && list.firstOrNull()?.idMeal?.isNotEmpty() == true) {
            LazyColumn() {
                items(list, key = { it.idMeal ?: "fail: no meal id" }) { meal ->
                    MealItem(meal = meal) { onClick(AppEvent.LoadDetailFromMealEvent(meal)) }
                }
            }
        } else if( viewState.currentScreen is RecipeViewModel.CurrentScreen.Search) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Type in search field to see recipes")
            }
        }
    }
}

@Composable
fun MealItem(meal: Meal, onClick: (meal: Meal) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .clickable { onClick(meal) }
            .padding(1.dp),
        // .clip(RoundedCornerShape(15.dp)),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            AsyncImage(
                meal.strMealThumb + "/preview",
                contentDescription = "null",
                contentScale = ContentScale.FillBounds,
                placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(10.dp))//.height(200.dp).width(200.dp)

            )
            Spacer(modifier = Modifier.weight(1f))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                meal.strMeal?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(5.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center

                    )
                }
                meal.strInstructions?.let {
                    Text(
                        text = it,
                        modifier = Modifier.padding(2.dp),
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis

                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
