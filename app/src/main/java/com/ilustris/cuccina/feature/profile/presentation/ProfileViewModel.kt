package com.ilustris.cuccina.feature.profile.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.cuccina.feature.profile.domain.model.UserModel
import com.ilustris.cuccina.feature.profile.domain.service.UserService
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import com.ilustris.cuccina.ui.theme.Page
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ServiceResult
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val recipeService: RecipeService,
    override val service: UserService
) : BaseViewModel<UserModel>(application) {


    val user = MutableLiveData<UserModel>()
    val pages = MutableLiveData<ArrayList<Page>>()

    private fun updatePages(page: Page) {
        pages.value?.let {
            it.add(page)
            pages.postValue(it)
        } ?: kotlin.run {
            pages.postValue(ArrayList(listOf(page)))
        }
    }

    fun getUserRecipes(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val queryTask = recipeService.getRecipesByUser(userID)) {
                is ServiceResult.Error -> {
                    updatePages(
                        Page.SimplePage(
                            "Minhas receitas",
                            "Você ainda não tem receitas publicadas. \nQue tal começar agora mesmo?",
                            listOf("receitas")
                        )
                    )
                    Log.e(
                        javaClass.simpleName,
                        "getUserRecipes: error ${queryTask.errorException.code}"
                    )
                }
                is ServiceResult.Success -> {
                    updatePages(
                        Page.RecipeListPage(
                            "Minhas receitas",
                            "Desde que entrou no Cuccina você publicou ${queryTask.data.size} receitas.\nContinue assim!",
                            queryTask.data as List<Recipe>
                        )
                    )
                }
            }
        }
    }

    fun getUserFavoriteRecipes(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val queryTask = recipeService.getRecipesByUserLike(userID)) {
                is ServiceResult.Error -> {
                    updatePages(
                        Page.SimplePage(
                            "Receitas favoritas",
                            "Você ainda não tem receitas favoritas. \nQue tal dar uma olhada nas receitas e favoritar as que mais gostar?",
                            annotatedTexts = listOf("favoritas")
                        )
                    )
                    Log.e(
                        javaClass.simpleName,
                        "getUserFavoriteRecipes: error ${queryTask.errorException.code}",
                    )
                }
                is ServiceResult.Success -> {
                    updatePages(
                        Page.RecipeListPage(
                            "Receitas favoritas",
                            "Você tem ${queryTask.data.size} receitas favoritas. Esperamos que encontre mais receitas que goste!",
                            (queryTask.data as List<Recipe>).take(2)
                        )
                    )
                }
            }
        }
    }

    fun getUserData(userId: String?) {
        updateViewState(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            service.currentUser()?.let {
                val uid = if (userId.isNullOrEmpty()) it.uid else userId
                when (val userTask = service.getSingleData(uid)) {
                    is ServiceResult.Success -> {
                        delay(1000)
                        user.postValue(userTask.data as UserModel)
                        updatePages(Page.ProfilePage(userModel = userTask.data as UserModel))
                    }
                    is ServiceResult.Error -> {
                        updateViewState(ViewModelBaseState.ErrorState(userTask.errorException))
                    }
                }
                updateViewState(ViewModelBaseState.LoadCompleteState)
            }
        }
    }


}