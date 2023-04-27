package com.ilustris.cuccina.feature.recipe.form.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import com.ilustris.cuccina.feature.recipe.form.presentation.viewstate.NewRecipeViewState
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step
import com.silent.ilustriscore.core.model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewRecipeViewModel @Inject constructor(
    application: Application,
    override val service: RecipeService
) : BaseViewModel<Recipe>(application) {

    val viewState = MutableLiveData<NewRecipeViewState>()
    val recipe = MutableLiveData(Recipe())


    fun saveRecipe() {
        viewState.postValue(NewRecipeViewState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            viewState.postValue(NewRecipeViewState.Loading)
            recipe.value?.let {
                service.addData(it)
                viewState.postValue(NewRecipeViewState.Success)
            } ?: kotlin.run {
                viewState.postValue(NewRecipeViewState.Error)
            }
        }

    }

    fun updateRecipeName(name: String) {
        recipe.postValue(recipe.value?.copy(name = name))
    }

    fun updateRecipeDescription(description: String) {
        recipe.postValue(recipe.value?.copy(description = description))
    }

    fun updateRecipeTime(time: Long) {
        recipe.postValue(recipe.value?.copy(time = time))
    }

    fun updateRecipePortions(portions: Int) {
        recipe.postValue(recipe.value?.copy(portions = portions))
    }

    fun updateRecipeIngredients(ingredient: Ingredient) {
        Log.i(javaClass.simpleName, "updateRecipeIngredients: adding ingredient -> $ingredient")
        recipe.postValue(
            recipe.value?.copy(
                ingredients = recipe.value?.ingredients?.plus(ingredient) ?: listOf(ingredient)
            )
        )
    }

    fun removeRecipeIngredient(ingredient: Ingredient) {
        recipe.postValue(
            recipe.value?.copy(
                ingredients = recipe.value?.ingredients?.minus(
                    ingredient
                ) ?: listOf(ingredient)
            )
        )
    }

    fun updateRecipePhoto(photo: String) {
        recipe.postValue(recipe.value?.copy(photo = photo))
    }

    fun updateRecipeSteps(step: Step) {
        Log.i(javaClass.simpleName, "updateRecipeSteps: adding step -> $step")
        recipe.postValue(
            recipe.value?.copy(
                steps = recipe.value?.steps?.plus(step) ?: listOf(step)
            )
        )
    }

    fun updateRecipeStep(step: Step) {
        recipe.postValue(
            recipe.value?.copy(
                steps = recipe.value?.steps?.minus(step)?.plus(step) ?: listOf(step)
            )
        )
    }
}