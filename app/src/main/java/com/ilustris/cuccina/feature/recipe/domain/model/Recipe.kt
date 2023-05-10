package com.ilustris.cuccina.feature.recipe.domain.model

import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step
import com.silent.ilustriscore.core.bean.BaseBean

data class Recipe(
    override var id: String = "",
    var name: String = "",
    var photo: String = "",
    var description: String = "",
    var time: Long = 0,
    var portions: Int = 0,
    var author: String = "",
    var userID: String = "",
    var ingredients: List<Ingredient> = emptyList(),
    var steps: List<Step> = emptyList(),
    var likes: ArrayList<String> = ArrayList(),
    var category: String = "",
    var calories: Int = 0,
    var publishDate: Long = 0L,
) : BaseBean(id)


data class RecipeGroup(
    val title: String,
    val recipes: List<Recipe>
)