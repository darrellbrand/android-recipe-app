package com.example.recipeapp.presentation.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.recipeapp.R
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.domain.model.ViewState
import com.example.recipeapp.navigation.AppRoute
import com.example.recipeapp.presentation.AppEvent
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged


@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun SearchList(
    viewState: ViewState, list: List<Meal>, searchString: String, onClick: (event: AppEvent) -> Unit
) {
     var query by rememberSaveable { mutableStateOf("") }
     var focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        snapshotFlow { query }
            .debounce(2500) // Wait for 500ms of no typing
            .distinctUntilChanged()
            .collect { debouncedQuery ->
                Log.i("screens", "search = $searchString meals= ${list.size}")
                onClick(AppEvent.OnValueChangedEvent(debouncedQuery))
            }

    }

    Column(
        verticalArrangement = Arrangement.SpaceAround,
    ) {

        val text =
            if (viewState.currentScreen is AppRoute.Search) "Search" else "Filter"
        TextField(
            value = query,
            label = {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily(Font(R.font.dancing_script)),
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            },
            onValueChange = {
                query = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            textStyle = TextStyle(
                textAlign = TextAlign.Start, fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.dancing_script))
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search, contentDescription = null
                )
            },
            maxLines = 1,
        )
        if (list.isNotEmpty() && list[0].idMeal != null ) {
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 125.dp)) {
                items(list.size, key = { list[it].idMeal!!}) { meal ->
                    MealItem(meal = list[meal]) { onClick(AppEvent.LoadDetailFromMealEvent(list[meal])) }
                }
            }
        } else if (viewState.currentScreen is AppRoute.Search) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column {
                    Text(
                        text = "Type in search field to see recipes",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily(
                                Font(R.font.dancing_script)
                            ), color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Image(
                        painter = painterResource(id = R.drawable.page_1),
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Composable
fun MealItem(meal: Meal, onClick: (meal: Meal) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(meal) },
    ) {
        AsyncImage(
            meal.strMealThumb + "/preview",
            contentDescription = "null",
            contentScale = ContentScale.FillBounds,
            placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
            modifier = Modifier
                .size(125.dp)
            //   .clip(RoundedCornerShape(10.dp))//.height(200.dp).width(200.dp)

        )
        Spacer(modifier = Modifier.weight(1f))
        //  Column(horizontalAlignment = Alignment.CenterHorizontally) {
        meal.strMeal?.let {
            Text(
                text = it,
                modifier = Modifier.padding(5.dp),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily(Font(R.font.dancing_script)),
                    color = MaterialTheme.colorScheme.primary
                ),
                textAlign = TextAlign.Center

            )
        }
    }
}
