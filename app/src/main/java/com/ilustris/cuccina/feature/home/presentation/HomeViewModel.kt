package com.ilustris.cuccina.feature.home.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.domain.model.RecipeGroup
import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    override val service: RecipeService
) : BaseViewModel<Recipe>(application) {

    sealed class HomeState {
        data class HomeListState(val recipes: List<RecipeGroup>) : HomeState()
    }

    val homeList = MutableLiveData<List<RecipeGroup>>()

    val highlightRecipes = MutableLiveData<List<Recipe>>()

    fun loadHome() {
        updateViewState(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            val data = service.getAllData()
            if (data.isSuccess) {
                val recipes = data.success.data as List<Recipe>
                highlightRecipes.postValue(recipes.sortedByDescending { it.publishDate }.take(3))
                val categoriesRecipes = recipes.groupBy { it.category }.map {
                    val category = Category.values().find { category -> category.name == it.key }
                        ?: Category.UNKNOW
                    RecipeGroup(category.title, it.value)
                }.sortedBy { it.title }
                homeList.postValue(categoriesRecipes)
            } else {
                updateViewState(ViewModelBaseState.ErrorState(data.error.errorException))
            }
            updateViewState(ViewModelBaseState.LoadCompleteState)
        }
    }

    fun searchRecipe(it: String) {
        TODO("Not yet implemented")
    }

}