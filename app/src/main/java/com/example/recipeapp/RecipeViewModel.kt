package com.example.recipeapp

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {
    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()
    // fun getValue() = viewState

    private val _searchString = MutableStateFlow(String())
    val searchString = _searchString.asStateFlow()

    private val _categories = MutableStateFlow(listOf(Category()))
    val categories = _categories.asStateFlow()


    private val _meals = MutableStateFlow(listOf(Meal()))
    val meals = _meals.asStateFlow().combine(_searchString) { meals: List<Meal>, s: String ->
        if (s.isBlank()) {
            meals
        } else {
            meals.filter { it.strMeal?.contains(s) == true }
        }
    }

    private val _meal = MutableStateFlow(Meal())
    val meal = _meal.asStateFlow()


    init {
        fetchRandomMeal()
        fetchCategories()
        fetchSearchMeals()

    }

    fun fetchRandomMeal() {
        viewModelScope.launch {
            try {
                val response: MealResponse? = recipeService?.getRandomMeal()
                val meal: Meal? = response?.meals?.firstOrNull()
                _viewState.value = _viewState.value.copy(
                    error = "",
                )
                _meal.value = meal ?: Meal()
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    error = e.toString()
                )
            }

        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response: CategoryResponse? = recipeService?.getCategories()
                _viewState.value = _viewState.value.copy(
                    error = "",
                )
                response?.let { _categories.value = it.categories }


            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    error = e.toString()
                )
            }

        }

    }

    fun fetchSearchMeals() {
        viewModelScope.launch {
            try {
                val response: MealResponse? = recipeService?.getSearchMeals(searchString.value)
                _viewState.value = _viewState.value.copy(
                    error = "",
                )
                response?.let { _meals.value = it.meals ?: listOf(Meal()) }
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    error = e.toString()
                )
            }

        }
    }

    /**
     * callbacks for ui
     */
    fun randomScreen() {

    }

    fun searchScreen() {

    }

    fun displayCategory() {
        //implement the request to network
        _viewState.value.currentScreen = CurrentScreen.Search()
    }
    fun onValueChanged(search : String){
        _searchString.value = search
    }

    enum class Screens { CATEGORY, SEARCH, DETAIL }
    sealed class CurrentScreen(val title: String, val screens: Screens) {
        class Category : CurrentScreen(Screens.CATEGORY.name, Screens.CATEGORY)
        class Search : CurrentScreen(Screens.SEARCH.name, Screens.SEARCH)
        class Detail : CurrentScreen(Screens.DETAIL.name, Screens.DETAIL)
    }


}