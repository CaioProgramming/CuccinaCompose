package com.ilustris.cuccina.ui.theme

import ai.atick.material.MaterialColor
import androidx.compose.ui.graphics.Color
import com.ilustris.cuccina.feature.profile.domain.model.UserModel
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step

sealed class Page(
    var title: String,
    var description: String,
    var backColor: Color? = null,
    var textColor: Color? = null
) {
    class SimplePage(
        title: String,
        description: String,
        val annotatedTexts: List<String> = emptyList(),
        backColor: Color? = null,
        textColor: Color? = null
    ) :
        Page(title, description, backColor, textColor)

    class HighlightPage(
        title: String,
        description: String,
        val backgroundImage: String,
        val recipeId: String
    ) : Page(title, description)

    class AnimatedTextPage(
        title: String, description: String, val texts: List<String>,
        backColor: Color? = null,
        textColor: Color? = null
    ) : Page(title, description, backColor, textColor)

    class ProfilePage(
        title: String = "",
        description: String = "",
        val userModel: UserModel
    ) : Page(
        title,
        description,
        backColor = MaterialColor.Orange800,
        textColor = MaterialColor.Black
    )

    class RecipeListPage(
        title: String,
        description: String,
        val recipes: List<Recipe>
    ) : Page(title, description)

    class RecipePage(
        title: String,
        description: String,
        val recipe: Recipe,
        val user: UserModel? = null
    ) :
        Page(title, description)

    class IngredientsPage(title: String, description: String, val ingredients: List<Ingredient>) :
        Page(title, description)

    class StepsPage(title: String, description: String, val steps: List<Step>) :
        Page(title, description)

    class StepPage(
        title: String,
        description: String,
        val step: Step,
        val ingredients: List<String>
    ) : Page(title, description)

    class SuccessPage(
        title: String,
        description: String,
        val actionText: String
    ) : Page(title, description)

    class OtherChefsPage(
        title: String,
        description: String,
        val chefs: List<UserModel>
    ) : Page(title, description)
}

