package com.ilustris.cuccina.feature.profile.presentation

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ilustris.cuccina.feature.profile.domain.model.UserModel
import com.ilustris.cuccina.feature.profile.domain.service.UserService
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.domain.service.RecipeService
import com.silent.ilustriscore.core.model.BaseViewModel
import com.silent.ilustriscore.core.model.ServiceResult
import com.silent.ilustriscore.core.model.ViewModelBaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val recipeService: RecipeService,
    override val service: UserService
) : BaseViewModel<UserModel>(application) {


    val user = MutableLiveData<UserModel>()
    val recipes = MutableLiveData<List<Recipe>>()
    val favoriteRecipes = MutableLiveData<List<Recipe>>()


    fun getUserRecipes(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val queryTask = recipeService.getRecipesByUser(userID)) {
                is ServiceResult.Error -> {
                    recipes.postValue(emptyList())
                    Log.e(
                        javaClass.simpleName,
                        "getUserRecipes: error ${queryTask.errorException.code}"
                    )
                }
                is ServiceResult.Success -> {
                    recipes.postValue(queryTask.data as List<Recipe>)
                }
            }
        }
    }

    fun getUserFavoriteRecipes(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val queryTask = recipeService.getRecipesByUserLike(userID)) {
                is ServiceResult.Error -> {
                    Log.e(
                        javaClass.simpleName,
                        "getUserFavoriteRecipes: error ${queryTask.errorException.code}",
                    )
                    favoriteRecipes.postValue(emptyList())
                }
                is ServiceResult.Success -> {
                    favoriteRecipes.postValue(queryTask.data as List<Recipe>)
                }
            }
        }
    }

    fun getUserData() {
        updateViewState(ViewModelBaseState.LoadingState)
        viewModelScope.launch(Dispatchers.IO) {
            service.currentUser()?.let {
                when (val userTask = service.getSingleData(it.uid)) {
                    is ServiceResult.Success -> {
                        user.postValue(userTask.data as UserModel)
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