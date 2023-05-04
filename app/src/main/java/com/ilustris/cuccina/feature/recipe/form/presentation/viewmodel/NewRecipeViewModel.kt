package com.ilustris.cuccina.feature.recipe.form.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.DataException
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.delayedFunction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class NewRecipeViewModel @Inject constructor(
    application: Application,
    override val service: RecipeService
) : BaseViewModel<Recipe>(application) {

    val recipe = MutableLiveData(Recipe())


    fun saveRecipe(time: Long) {
        updateViewState(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            recipe.value?.let {
                val saveTask =
                    service.addData(
                        it.copy(
                            author = service.currentUser()?.displayName ?: "",
                            time = time,
                            publishDate = Calendar.getInstance().timeInMillis
                        )
                    )
                if (saveTask.isSuccess) {
                    updateViewState(ViewModelBaseState.DataSavedState(it))
                } else {
                    updateViewState(ViewModelBaseState.ErrorState(saveTask.error.errorException))
                }
                clearRecipe()
                delayedFunction(2000) {
                    updateViewState(ViewModelBaseState.LoadCompleteState)

                }
            } ?: kotlin.run {
                updateViewState(ViewModelBaseState.ErrorState(DataException.UNKNOWN))
            }
        }

    }

    fun clearRecipe() {
        recipe.postValue(Recipe())
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

    fun updateRecipeCategory(category: String) {
        recipe.postValue(recipe.value?.copy(category = category))
    }

    fun updateCalories(calories: Int) {
        recipe.postValue(recipe.value?.copy(calories = calories))
    }

    fun updatePortions(portions: Int) {
        recipe.postValue(recipe.value?.copy(portions = portions))
    }
}