package com.example.recipeapp.presentation

import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.recipeapp.domain.model.Category
import com.example.recipeapp.domain.model.CategoryResponse
import com.example.recipeapp.domain.model.Meal
import com.example.recipeapp.domain.model.MealResponse
import com.example.recipeapp.domain.model.ViewState
import com.example.recipeapp.navigation.AppRoute
import com.example.recipeapp.repository.RecipeRepository
import com.example.recipeapp.util.getIngredientsString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(private val recipeRepository: RecipeRepository) :
    ViewModel() {

    private val androidId = Settings.Secure.ANDROID_ID
    private var apiKey = "";

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

    private val _selectedList = MutableStateFlow<List<String>>(emptyList())
    val selectedList = _selectedList.asStateFlow()

    private val _generatedRecipe = MutableStateFlow("")
    val generatedRecipe = _generatedRecipe.asStateFlow()

    init {
        init()
        AppRoute.Home().let { _viewState.value.currentScreen = it }
        fetchRandomMeal()

    }

    private fun init() {
        getApiKey()
        fetchCategories()
    }

    fun processEvent(event: AppEvent) {
        when (event) {
            AppEvent.GenerateOpenAIRecipeEvent -> {
                generateOpenAiRecipe()
            }

            is AppEvent.HandleBackPressEvent -> {
                handleBackPress(event.navController)
            }

            is AppEvent.LoadDetailFromMealEvent -> {
                loadDetailFromMeal(event.meal)
            }

            is AppEvent.LoadListFromCategoryEvent -> {
                loadListFromCategory(event.category)
            }

            is AppEvent.OnValueChangedEvent -> {
                onValueChanged(event.search)
            }

            is AppEvent.UpdateCurrentScreenEvent -> {
                updateCurrentScreen(event.screen)
            }

            is AppEvent.InitEvent -> {
                init()
            }

            is AppEvent.GenerateOpenAICustomRecipeEvent -> {
                generateOpenAiCustomRecipe(event.selectedList)
            }
        }

    }

    private fun getApiKey() {
        viewModelScope.launch {
            Log.i("RVM", "getApiKey")
            try {
                val res = recipeRepository.getApiKey(androidId)
                apiKey = res.apiKey.toString()
                clearApiKeyNetworkError()
                Log.i("RVM", "got api key")
            } catch (e: Exception) {
                addApiKeyNetworkError(e)
                Log.i("RVM", " getApiKey " + e.stackTraceToString())
            }
        }
    }

    private fun fetchRandomMeal() {
        viewModelScope.launch {
            Log.i("RVM", "fetchRandomMeal")
            try {
                val response: MealResponse = recipeRepository.getRandomMeal()
                val meal: Meal? = response.meals?.firstOrNull()
                clearNetworkError()
                _meal.value = meal ?: Meal()
            } catch (e: Exception) {
                addNetworkError(e)
            }
        }
    }

    private fun fetchDetailMeals(meal: Meal) {
        viewModelScope.launch {
            Log.i("RVM", "fetchDetailMeals")
            meal.idMeal?.let {
                try {
                    val response: MealResponse =
                        meal.idMeal.let { recipeRepository.getDetailMeal(it) }
                    _meal.value = response.meals?.firstOrNull() ?: Meal()
                    clearNetworkError()
                } catch (e: Exception) {
                    addNetworkError(e)
                }
            } ?: "Fail meal id null"

        }
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            Log.i("RVM", "fetchCategory")
            try {
                val response: CategoryResponse = recipeRepository.getCategories()
                response.let { _categories.value = it.categories }
                clearNetworkError()
            } catch (e: Exception) {
                addNetworkError(e)
            }

        }
    }

    private fun fetchSearchMeals() {
        viewModelScope.launch {
            Log.i("RVM", "fetchSearchMeals")
            try {
                val response: MealResponse = recipeRepository.getSearchMeals(searchString.value)
                response.let { _meals.value = it.meals ?: listOf(Meal()) }
                clearNetworkError()
            } catch (e: Exception) {
                addNetworkError(e)
            }
        }
    }


    private fun clearSearchIfNeeded(screen: AppRoute) {
        Log.i("RVM", "clearSearchIfNeeded " + screen.title)
        _searchString.value = when (screen) {
            is AppRoute.Category -> ""
            is AppRoute.Detail -> ""
            is AppRoute.Home -> ""
            is AppRoute.Generate -> ""
            is AppRoute.GeneratedRecipe -> ""
            is AppRoute.Search -> {
                clearList()
                _searchString.value
            }

            is AppRoute.Filter -> {
                _searchString.value
            }


        }
    }

    private fun clearList() {
        Log.i("RVM", "clearList")
        _meals.value = listOf(Meal())
    }

    /**
     * callbacks for ui  will be processed as events ///////////////////////////////////////////////////////////////////////////////////////////////
     */
    private fun updateCurrentScreen(screen: AppRoute) {
        Log.i("RVM", "updateCurrentScreen " + screen.title)
        clearSearchIfNeeded(screen)
        _viewState.value = _viewState.value.copy(currentScreen = screen)


    }


    private fun onValueChanged(search: String) {
        Log.i("RVM", "onValueChanged $search")
        _searchString.value = search
        viewModelScope.launch {
            when (_viewState.value.currentScreen) {
                is AppRoute.Category -> {}
                is AppRoute.Detail -> {}
                is AppRoute.Home -> {}
                is AppRoute.Search -> fetchSearchMeals()
                is AppRoute.Filter -> {}
                is AppRoute.Generate -> {}
                is AppRoute.GeneratedRecipe -> {}
            }
        }
    }

    private fun loadListFromCategory(category: Category) {
        viewModelScope.launch {
            Log.i("RVM", "loadListFromCategory " + category.strCategory)
            try {
                val response: MealResponse =
                    recipeRepository.getSearchCategoryMeals(category.strCategory)
                response.let { _meals.value = it.meals ?: listOf(Meal()) }
                updateCurrentScreen(AppRoute.Filter())
                clearNetworkError()
            } catch (e: Exception) {
                addNetworkError(e)
            }
        }
    }

    private fun loadDetailFromMeal(meal: Meal) {
        Log.i("RVM", "loadDetailsFromMeal")
        viewModelScope.launch {
            try {
                fetchDetailMeals(meal)
                updateCurrentScreen(AppRoute.Detail())
                clearNetworkError()
            } catch (e: Exception) {
                addNetworkError(e)
            }
        }
    }


    private fun handleBackPress(navController: NavHostController) {
        Log.i("RVM", "handleBackPress")
        val ret = navController.popBackStack()
        println(ret)
        navController.currentBackStackEntry?.destination?.route?.let {

            when (it) {
                AppRoute.Category().title -> {
                    updateCurrentScreen(AppRoute.Category())
                }

                AppRoute.Search().title -> {
                    updateCurrentScreen(AppRoute.Search())
                }

                AppRoute.Detail().title -> {
                    updateCurrentScreen(AppRoute.Detail())
                }

                AppRoute.Filter().title -> {
                    updateCurrentScreen(AppRoute.Filter())
                }

                AppRoute.Home().title -> {
                    updateCurrentScreen(AppRoute.Home())
                }

                AppRoute.Generate().title -> {
                    updateCurrentScreen(AppRoute.Generate())
                }

                AppRoute.GeneratedRecipe().title -> {
                    updateCurrentScreen(AppRoute.GeneratedRecipe())
                }
            }

        }
    }

    /*
    was used for the old gemini sdk
     *//* fun generateRecipe() {
         val prompt =
             "please create a simpler recipe that is an improvement on these instructions for "
         val oldRecipe = _meal.value.strInstructions
         val oldRecipeName = _meal.value.strMeal
         val finalPrompt =
             "$prompt $oldRecipeName $oldRecipe  please format your response  with new lines after each step and list all ingredients after instructions "
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
 */
    private fun generateOpenAiRecipe() {
        Log.i("RVM", "generateOpenAiRecipe")
        val prompt =
            "please create a simpler recipe that is an improvement on these instructions for "
        val oldRecipe =
            _meal.value.strInstructions + " using these ingredients. " + getIngredientsString(_meal.value)
        val oldRecipeName = _meal.value.strMeal
        val finalPrompt =
            "$prompt $oldRecipeName $oldRecipe  please format your response  with new lines after each step and list all ingredients at end of recipe and not at beginning"
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(
                isLoadingAiResponse = true
            )
            try {
                val res = recipeRepository.getOpenAIRecipe(
                    androidId = androidId, apiKey = apiKey, message = finalPrompt
                )
                Log.i("RVM", "got ai response")
                //  Log.i("RVM", " $res")
                _meal.value =
                    _meal.value.copy(strInstructions = res.generate, strIngredient1 = null)
                clearApiKeyNetworkError()
            } catch (e: Exception) {
                addApiKeyNetworkError(e)
            }
            _viewState.value = _viewState.value.copy(isLoadingAiResponse = false)
        }

    }

    private fun generateOpenAiCustomRecipe(selectedList : List<String>) {
        Log.i("RVM", "generateOpenAiCustomRecipe")
        val prompt =
            "You are a helpful ai assisting in creating recipes for people with limited food resources. We call it struggle meals in america. " +
                    "please create a simple recipe that uses the following ingredients  " + selectedList.joinToString(
                ", "
            ) + " ."


        val finalPrompt =
            "$prompt  please format your response  with new lines after each step and list all ingredients at end of recipe and not at beginning"
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(
                isLoadingAiResponse = true
            )
            try {
                val res = recipeRepository.getOpenAIRecipe(
                    androidId = androidId, apiKey = apiKey, message = finalPrompt
                )
                Log.i("RVM", "got ai response custom recipe")
                //  Log.i("RVM", " $res")
                _generatedRecipe.value = res.generate ?: "failed to fetch recipe"
                clearApiKeyNetworkError()
            } catch (e: Exception) {
                addApiKeyNetworkError(e)
            }
            _viewState.value = _viewState.value.copy(isLoadingAiResponse = false)
            updateCurrentScreen(AppRoute.GeneratedRecipe())
        }

    }

    private fun clearApiKeyNetworkError() {
        Log.i("RVM", "clearApiKeyNetworkError")
        _viewState.value =
            _viewState.value.copy(appErrors = _viewState.value.appErrors.filter { it !is AppError.ApiKeyNetworkError })
    }

    private fun clearNetworkError() {
        Log.i("RVM", "clearNetworkError")
        _viewState.value =
            _viewState.value.copy(appErrors = _viewState.value.appErrors.filter { it !is AppError.NetworkError })
    }

    private fun addNetworkError(e: Exception) {
        Log.i("RVM", "addNetworkError")
        val errors = _viewState.value.appErrors.toMutableList()
        errors.add(AppError.NetworkError(e.stackTraceToString()))
        _viewState.value = _viewState.value.copy(
            appErrors = errors
        )
    }

    private fun addApiKeyNetworkError(e: Exception) {
        Log.i("RVM", "addApiKeyNetworkError")
        val errors = _viewState.value.appErrors.toMutableList()
        errors.add(AppError.ApiKeyNetworkError(e.stackTraceToString()))
        _viewState.value = _viewState.value.copy(
            appErrors = errors
        )
    }

}