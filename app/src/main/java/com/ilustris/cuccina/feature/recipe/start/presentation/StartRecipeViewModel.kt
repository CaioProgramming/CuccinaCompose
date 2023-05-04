package com.ilustris.cuccina.feature.recipe.start.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import com.ilustris.cuccina.feature.recipe.start.domain.model.Page
import com.silent.ilustriscore.core.model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartRecipeViewModel @Inject constructor(
    application: Application,
    override val service: RecipeService
) : BaseViewModel<Recipe>(application) {

    val pages = MutableLiveData<List<Page>>()

    fun getPages(recipe: Recipe) = viewModelScope.launch(Dispatchers.IO) {
        val pageList = ArrayList<Page>()
        val plural = if (recipe.ingredients.size > 1) "s" else ""
        pageList.add(Page.RecipePage(recipe.name, recipe.description, recipe))
        pageList.add(
            Page.IngredientsPage(
                "Ingredientes",
                "Reúna os ingredientes, você vai precisar de ${recipe.ingredients.size}.Quando estiver pronto só precisa clicar em continuar}",
                recipe.ingredients
            )
        )
        pageList.add(
            Page.StepsPage(
                "Modo de Preparo",
                "Essa receita tem ${recipe.steps.size} etapa$plural, vamos começar?",
                recipe.steps
            )
        )
        recipe.steps.forEach {
            pageList.add(Page.StepPage(it.title, description = "Fique atento as instruções", it))
        }

        pages.postValue(pageList)
    }


}