package com.example.recipeapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.recipeapp.BuildConfig
import com.example.recipeapp.data.data_source.recipeService
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.CategoryResponse
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.domain.model.MealResponse
import com.example.recipeapp.domain.model.ViewState

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecipeViewModel : ViewModel() {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.apiKey
    )

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    private val _searchString = MutableStateFlow(String())
    val searchString = _searchString.asStateFlow()

    private val _categories = MutableStateFlow(listOf(Category()))
    val categories = _categories.asStateFlow()

    private val _meals = MutableStateFlow(listOf(Meal()))
    val meals = _meals.asStateFlow().combine(_searchString) { meals: List<Meal>, s: String ->
        if (s.isBlank()) {
            meals
        } else {
            meals.filter { it.strMeal?.contains(s, ignoreCase = true) == true }
        }
    }

    private val _meal = MutableStateFlow(Meal())
    val meal = _meal.asStateFlow()


    init {
        fetchCategories()
        CurrentScreen.Home().also { _viewState.value.currentScreen = it }
        fetchRandomMeal()
    }

    private fun fetchRandomMeal() {
        viewModelScope.launch {
            Log.i("RVM", "fetchRandomMeal")
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

    private fun fetchDetailMeals(meal: Meal) {
        viewModelScope.launch {
            Log.i("RVM", "fetchDetailMeals")
            try {
                val response: MealResponse? = meal.idMeal?.let { recipeService?.getDetailMeal(it) }
                _meal.value = response?.meals?.firstOrNull() ?: Meal()
                _viewState.value = _viewState.value.copy(
                    error = "",
                )
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    error = e.toString()
                )
            }
        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            Log.i("RVM", "fetchCategory")
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

    private fun fetchSearchMeals() {
        viewModelScope.launch {
            Log.i("RVM", "fetchSearchMeals")
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

    fun updateCurrentScreen(screen: CurrentScreen) {
        Log.i("RVM", "updateCurrentScreen " + screen.title)
        viewModelScope.launch {
            try {
                clearSearchIfNeeded(screen)
                _viewState.value = _viewState.value.copy(currentScreen = screen)
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    error = e.toString()
                )
            }
        }
    }

    private fun clearSearchIfNeeded(screen: CurrentScreen) {
        _searchString.value = when (screen) {
            is CurrentScreen.Category -> ""
            is CurrentScreen.Detail -> ""
            is CurrentScreen.Home -> ""
            is CurrentScreen.Search -> {
                clearList()
                _searchString.value
            }

            is CurrentScreen.Filter -> {
                _searchString.value
            }
        }
    }

    /**
     * callbacks for ui ///////////////////////////////////////////////////////////////////////////////////////////////
     */


    fun onValueChanged(search: String) {
        Log.i("RVM", "onValueChanged")
        viewModelScope.launch {
            try {
                _searchString.value = search
                _viewState.value = _viewState.value.copy(
                    error = "",
                )
                when (_viewState.value.currentScreen) {
                    is CurrentScreen.Category -> {}
                    is CurrentScreen.Detail -> {}
                    is CurrentScreen.Home -> {}
                    is CurrentScreen.Search -> fetchSearchMeals()
                    is CurrentScreen.Filter -> {}
                }
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    error = e.toString()
                )
            }
        }
    }

    fun loadListFromCategory(category: Category) {
        viewModelScope.launch {
            Log.i("RVM", "loadListFromCategory " + category.strCategory)
            try {
                val response: MealResponse? =
                    recipeService?.getSearchCategoryMeals(category.strCategory)
                response?.let { _meals.value = it.meals ?: listOf(Meal()) }
                updateCurrentScreen(CurrentScreen.Filter())
                _viewState.value = _viewState.value.copy(
                    error = "",
                )
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    error = e.toString()
                )
            }
        }
    }

    fun loadDetailFromMeal(meal: Meal) {
        Log.i("RVM", "loadDetailsFromMeal")
        viewModelScope.launch {
            try {
                fetchDetailMeals(meal)
                updateCurrentScreen(CurrentScreen.Detail())
                _viewState.value = _viewState.value.copy(
                    error = "",
                )
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    error = e.toString()
                )
            }
        }
    }

    fun clearList() {
        Log.i("RVM", "clearList")
        viewModelScope.launch {
            try {
                _meals.value = listOf(Meal())
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    error = e.toString()
                )
            }
        }
    }

    fun handleBackPress(navController: NavHostController) {
        val ret = navController.popBackStack()
        println(ret)
        navController.currentBackStackEntry?.destination?.route?.let {

            when (it) {
                CurrentScreen.Category().title -> {
                    updateCurrentScreen(CurrentScreen.Category())
                }

                CurrentScreen.Search().title -> {
                    updateCurrentScreen(CurrentScreen.Search())
                }

                CurrentScreen.Detail().title -> {
                    updateCurrentScreen(CurrentScreen.Detail())
                }

                CurrentScreen.Filter().title -> {
                    updateCurrentScreen(CurrentScreen.Filter())
                }

                CurrentScreen.Home().title -> {
                    updateCurrentScreen(CurrentScreen.Home())
                }
            }

        }
    }

    fun generateRecipe() {
        val prompt =
            "please create a simpler recipe that is an improvement on these instructions for "
        val oldRecipe = _meal.value.strInstructions
        val oldRecipeName = _meal.value.strMeal
        val finalPrompt =
            "$prompt $oldRecipeName $oldRecipe  please format your response cleanly with new lines and list all ingredients in one section at bottom"
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var res = ""
                //
                generativeModel.generateContentStream(finalPrompt)
                    .collect { response ->
                        res += response.text
                        _meal.value = _meal.value.copy(strInstructions = res)
                    }
                _meal.value = _meal.value.copy(strIngredient1 = null)
            }
        }

    }

    enum class Screens { CATEGORY, SEARCH, DETAIL, HOME, FILTER }
    sealed class CurrentScreen(val title: String, val screens: Screens) {
        class Category : CurrentScreen(Screens.CATEGORY.name, Screens.CATEGORY)
        class Search : CurrentScreen(Screens.SEARCH.name, Screens.SEARCH)
        class Detail : CurrentScreen(Screens.DETAIL.name, Screens.DETAIL)
        class Home : CurrentScreen(Screens.HOME.name, Screens.HOME)
        class Filter : CurrentScreen(Screens.FILTER.name, Screens.FILTER)
    }
}