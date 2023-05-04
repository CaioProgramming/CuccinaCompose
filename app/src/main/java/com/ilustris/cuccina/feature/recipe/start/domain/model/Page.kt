package com.ilustris.cuccina.feature.recipe.start.domain.model

import androidx.compose.ui.graphics.Color
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step

sealed class Page(
    var title: String,
    var description: String,
    var backColor: Color? = null,
    var textColor: Color? = null
) {
    class SimplePage(title: String, description: String) : Page(title, description)
    class RecipePage(title: String, description: String, val recipe: Recipe) :
        Page(title, description)

    class IngredientsPage(title: String, description: String, val ingredients: List<Ingredient>) :
        Page(title, description)

    class StepsPage(title: String, description: String, val steps: List<Step>) :
        Page(title, description)

    class StepPage(title: String, description: String, val step: Step) : Page(title, description)
}

