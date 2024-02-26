package com.example.recipeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.recipeapp.ui.theme.RecipeAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            RecipeAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen("Android")
                }
            }
        }
    }
}

@Composable
fun MainScreen(name: String, modifier: Modifier = Modifier) {
    val recipeViewModel: RecipeViewModel = viewModel()
    val viewState by recipeViewModel.viewState
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Gray)
    ) {

        when {
            viewState.loading -> {
                CircularProgressIndicator()
            }

            viewState.error != null -> {
                Text("ERROR OCCURRED")
            }

            !viewState.isDetails -> {
                DescriptionScreen(
                    viewState = viewState, recipeViewModel = recipeViewModel
                )
            }
            else -> {
                DetailsScreen(recipeViewModel = recipeViewModel, viewState = viewState)
            }

        }


    }
}

@Composable
fun DescriptionScreen(viewState: ViewState, recipeViewModel: RecipeViewModel) {
    val defaultText = "Recipe search here"
    var text by rememberSaveable { mutableStateOf(defaultText) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {


        AsyncImage(
            viewState.meal?.strMealThumb,
            contentDescription = "null",
            modifier = Modifier
                .clip(RoundedCornerShape(30.dp))
                .fillMaxWidth()
                .weight(9f),
            contentScale = ContentScale.FillBounds,
            placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(value = text, onValueChange = {
            text = it
        }, textStyle = androidx.compose.ui.text.TextStyle(fontSize = 30.sp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (it.isFocused && text == defaultText) {
                        // Clear default text when the text field is focused
                        text = ""
                    } else if (!it.isFocused && text != defaultText) {
                        text = ""
                    }
                })
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Gray)
                .height(IntrinsicSize.Min)
        ) {
            Button(
                onClick = {
                    recipeViewModel.fetchRandomMeal()
                },
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(text = "random recipe")
            }
            Button(
                onClick = {

                    if (!text.isNullOrBlank() && text != defaultText) {
                        recipeViewModel.fetchSearchMeal(text)
                    }
                },
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(text = "search recipe")
            }
            Button(
                onClick = {

                    if (!text.isNullOrBlank()) {
                        recipeViewModel.seeDetails()
                    }
                },
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(text = "see details")
            }

        }

    }

}

@Composable
fun DetailsScreen(recipeViewModel: RecipeViewModel, viewState: ViewState) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

       val ingredients =  viewState.meal?.let { getIngredientsString(it) }
        viewState.meal?.strInstructions?.let { it ->
            Text(
                text = "$it \n\n\n $ingredients",
                Modifier
                    .weight(8f)
                    .verticalScroll(
                        rememberScrollState()
                    ),
                fontSize = 25.sp, color = Color.Blue
            )
        }
        Button(
            onClick = {

                recipeViewModel.seeDescription()
            },
            Modifier
                .weight(.5f)
                .fillMaxWidth()
        ) {
            Text(text = "back")
        }
    }
}
