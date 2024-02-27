package com.example.recipeapp

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

class RecipeViewModel : ViewModel() {
    private val _viewState = mutableStateOf(ViewState())
    val viewState: State<ViewState> = _viewState

    init {
        fetchRandomMeal()

    }

    fun fetchRandomMeal() {
        viewModelScope.launch {
            try {
                val response: MealResponse? = recipeService?.getRandomMeal()
                val meal: Meal? = response?.meals?.firstOrNull()
                _viewState.value = _viewState.value.copy(
                    loading = false,
                    error = null,
                    meal = response?.meals?.firstOrNull()
                )
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    loading = true,
                    error = e.toString()
                )
            }

        }
    }

    fun fetchSearchMeal(searchParam: String) {
        viewModelScope.launch {
            try {
                val response: MealResponse? = recipeService?.getSearchMeal(searchParam)
                _viewState.value = _viewState.value.copy(
                    loading = false,
                    error = null,
                    meal = response?.meals?.firstOrNull()
                )
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    loading = true,
                    error = e.toString()
                )
            }

        }
    }
    /*fun seeDetails(){
        try {
            _viewState.value = _viewState.value.copy(
                isDetails = true
            )
        } catch (e: Exception) {
            _viewState.value = _viewState.value.copy(
                loading = true,
                error = e.toString()
            )
        }

    }*/
   /* fun seeDescription(){
        try {
            _viewState.value = _viewState.value.copy(
                isDetails = false
            )
        } catch (e: Exception) {
            _viewState.value = _viewState.value.copy(
                loading = true,
                error = e.toString()
            )
        }
    }
*/


}