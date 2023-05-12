package com.ilustris.cuccina.feature.recipe.start.presentation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.cuccina.feature.profile.domain.model.UserModel
import com.ilustris.cuccina.feature.profile.domain.service.UserService
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.IngredientMapper
import com.ilustris.cuccina.ui.theme.Page
import com.silent.ilustriscore.core.model.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartRecipeViewModel @Inject constructor(
    application: Application,
    override val service: RecipeService,
    val userService: UserService
) : BaseViewModel<Recipe>(application) {

    val pages = MutableLiveData<List<Page>>()
    val isFavorite = MutableLiveData(false)
    val isUserRecipe = MutableLiveData(false)

    fun getPages(recipe: Recipe) = viewModelScope.launch(Dispatchers.IO) {
        isFavorite.postValue(recipe.likes.contains(service.currentUser()!!.uid))
        isUserRecipe.postValue(recipe.userID == service.currentUser()!!.uid)
        val user = try {
            userService.getSingleData(recipe.userID).success.data as UserModel
        } catch (e: java.lang.Exception) {
            null
        }
        val users = userService.getAllData()
        val pageList = ArrayList<Page>()
        val stepPlural = if (recipe.steps.size > 1) "s" else ""
        val categoryRecipes = service.getRecipesByCategory(recipe.category)
        pageList.run {
            add(
                Page.RecipePage(
                    recipe.name,
                    recipe.description,
                    recipe,
                    user
                )
            )
            add(
                Page.IngredientsPage(
                    "Ingredientes",
                    "Reúna os ingredientes, você vai precisar de ${recipe.ingredients.size} itens.\nQuando estiver pronto só precisa clicar em continuar}",
                    recipe.ingredients
                )
            )

            var stepsDescription = ""

            recipe.steps.forEachIndexed { index, step ->
                stepsDescription += when (index) {
                    0 -> {
                        "A primeira etapa é ${step.title}, e possui ${step.instructions.size} instruções."
                    }
                    recipe.steps.lastIndex -> {
                        "A última etapa é ${step.title}, e possui ${step.instructions.size} instruções."
                    }
                    else -> {
                        "A próxima etapa é ${step.title}, e possui ${step.instructions.size} instruções."
                    }
                }
            }
            add(
                Page.SimplePage(
                    "Hora de cozinhar",
                    "Essa receita tem ${recipe.steps.size} etapa$stepPlural.$stepsDescription\nvamos começar?"
                )
            )
            recipe.steps.forEachIndexed { index, step ->
                val emojis = ArrayList<String>()
                recipe.ingredients.shuffled().forEach {
                    emojis.add(IngredientMapper.getIngredientSymbol(it.name))
                }
                pageList.add(
                    Page.AnimatedTextPage(
                        step.title,
                        description = "Fique atento as instruções",
                        texts = emojis.distinct().filter { it != "❓" }.take(3)
                    )
                )
                step.instructions.forEachIndexed { index, instruction ->
                    pageList.add(
                        Page.SimplePage(
                            "${index + 1}º Passo",
                            instruction,
                            recipe.ingredients.map { it.name.lowercase() }
                        )
                    )
                }

            }

            add(
                Page.SuccessPage(
                    "Parabéns! Você concluiu a receita.",
                    "Agora é só aproveitar! Que tal compartilhar uma receita própria também?",
                    "Publicar receita"
                )
            )

            if (categoryRecipes.isSuccess) {
                val filterdList =
                    (categoryRecipes.success.data as List<Recipe>).filter { it.id != recipe.id }
                if (filterdList.isNotEmpty()) {
                    add(
                        Page.RecipeListPage(
                            "Outras receitas que você pode gostar...",
                            "Que tal experimentar outras receitas?",
                            filterdList
                        )
                    )
                }

            }


            if (users.isSuccess) {
                val filterdList =
                    (users.success.data as List<UserModel>).filter { it.uid != recipe.userID && it.uid != service.currentUser()!!.uid }
                if (filterdList.isNotEmpty()) {
                    add(
                        Page.OtherChefsPage(
                            "Outros cozinheiros que você pode gostar...",
                            "Que tal conhecer outros cozinheiros?",
                            filterdList
                        )
                    )
                }
            }


            pages.postValue(pageList)
        }
    }

    fun favoriteRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            val uid = service.currentUser()!!.uid
            val isFavorite = recipe.likes.contains(uid)
            if (isFavorite) {
                recipe.likes.remove(uid)
            } else {
                recipe.likes.add(uid)
            }
            editData(recipe)
        }
    }

    fun checkFavorite(recipe: Recipe) {
        isFavorite.postValue(recipe.likes.contains(service.currentUser()!!.uid))
    }
}