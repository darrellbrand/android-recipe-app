package com.example.recipeapp.presentation.screens

import android.view.RoundedCorner
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
   val  cursive = FontFamily(Font(R.font.dancing_script))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
          //  .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "Recipe King",
            style = TextStyle(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 90.sp,
                fontFamily = cursive
            ) )
        Image(
            painter = painterResource(id = R.drawable.page_1),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        Card(modifier =  Modifier.background(color = MaterialTheme.colorScheme.primary).padding(10.dp)) {
            AsyncImage(
                meal.strMealThumb,
                contentDescription = "null",
                contentScale = ContentScale.Fit,
                placeholder = painterResource(id = R.drawable.excerpt_lazy_load),
            )
        }
    }

}
