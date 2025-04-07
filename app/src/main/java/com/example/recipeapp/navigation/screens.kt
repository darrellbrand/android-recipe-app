package com.example.recipeapp.navigation

enum class ScreenEnum { CATEGORY, SEARCH, DETAIL, HOME, FILTER, GENERATE, GENERATED_RECIPE }
sealed class AppRoute(val title: String, val screens: ScreenEnum) {
    class Category : AppRoute(ScreenEnum.CATEGORY.name, ScreenEnum.CATEGORY)
    class Search : AppRoute(ScreenEnum.SEARCH.name, ScreenEnum.SEARCH)
    class Detail : AppRoute(ScreenEnum.DETAIL.name, ScreenEnum.DETAIL)
    class Home : AppRoute(ScreenEnum.HOME.name, ScreenEnum.HOME)
    class Filter : AppRoute(ScreenEnum.FILTER.name, ScreenEnum.FILTER)
    class Generate : AppRoute(ScreenEnum.GENERATE.name, ScreenEnum.GENERATE)
    class GeneratedRecipe :
        AppRoute(ScreenEnum.GENERATED_RECIPE.name, ScreenEnum.GENERATED_RECIPE)
}