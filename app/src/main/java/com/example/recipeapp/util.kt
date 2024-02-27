package com.example.recipeapp

import java.lang.StringBuilder

fun getIngredientsString(meal: Meal): String {

    var map = HashMap<String, String>()

    map[meal.strIngredient1 ?: ""] = meal.strMeasure1 ?: ""
    map[meal.strIngredient2 ?: ""] = meal.strMeasure2 ?: ""
    map[meal.strIngredient3 ?: ""] = meal.strMeasure3 ?: ""
    map[meal.strIngredient4 ?: ""] = meal.strMeasure4 ?: ""
    map[meal.strIngredient5 ?: ""] = meal.strMeasure5 ?: ""
    map[meal.strIngredient6 ?: ""] = meal.strMeasure6 ?: ""
    map[meal.strIngredient7 ?: ""] = meal.strMeasure7 ?: ""
    map[meal.strIngredient8 ?: ""] = meal.strMeasure8 ?: ""
    map[meal.strIngredient9 ?: ""] = meal.strMeasure9 ?: ""
    map[meal.strIngredient10 ?: ""] = meal.strMeasure10 ?: ""
    map[meal.strIngredient11 ?: ""] = meal.strMeasure11 ?: ""
    map[meal.strIngredient12 ?: ""] = meal.strMeasure12 ?: ""
    map[meal.strIngredient13 ?: ""] = meal.strMeasure13 ?: ""
    map[meal.strIngredient14 ?: ""] = meal.strMeasure14 ?: ""
    map[meal.strIngredient15 ?: ""] = meal.strMeasure15 ?: ""
    map[meal.strIngredient16 ?: ""] = meal.strMeasure16 ?: ""
    map[meal.strIngredient17 ?: ""] = meal.strMeasure17 ?: ""
    map[meal.strIngredient18 ?: ""] = meal.strMeasure18 ?: ""
    map[meal.strIngredient19 ?: ""] = meal.strMeasure19 ?: ""
    map[meal.strIngredient20 ?: ""] = meal.strMeasure20 ?: ""

    val list = map.filterKeys { k -> k.isNotEmpty() }
    val builder = StringBuilder()
    list.forEach { (t, u) ->
        builder.append(" $t : $u \n")
    }

    return  "\n" + builder.toString()


}
