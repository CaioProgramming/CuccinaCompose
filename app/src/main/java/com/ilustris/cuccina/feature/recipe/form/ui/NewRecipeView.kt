@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class
)

package com.ilustris.cuccina.feature.recipe.form.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import com.ilustris.cuccina.feature.home.ui.HOME_ROUTE
import com.ilustris.cuccina.feature.recipe.form.presentation.viewmodel.NewRecipeViewModel
import com.ilustris.cuccina.feature.recipe.ui.component.getStateComponent
import com.ilustris.cuccina.ui.theme.getFormView
import com.silent.ilustriscore.core.model.ViewModelBaseState

const val NEW_RECIPE_ROUTE = "new_recipe"

@Composable
fun NewRecipeView(newRecipeViewModel: NewRecipeViewModel, navController: NavController) {

    val recipe = newRecipeViewModel.recipe.observeAsState().value
    val pages = newRecipeViewModel.pages.observeAsState().value
    val baseState = newRecipeViewModel.viewModelState.observeAsState().value
    val pagerState = rememberPagerState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Log.i("NewRecipeView", "NewRecipeView: current recipe $recipe")

    val showPages =
        pages?.isNotEmpty() == true && baseState != ViewModelBaseState.LoadingState && baseState !is ViewModelBaseState.DataSavedState

    AnimatedVisibility(visible = showPages, enter = fadeIn(), exit = fadeOut()) {
        val pageList = pages!!
        HorizontalPager(pageCount = pageList.size, state = pagerState) {
            getFormView(formPage = pageList[it])
        }
    }

    AnimatedVisibility(visible = !showPages, enter = fadeIn(), exit = fadeOut()) {
        baseState?.let {
            getStateComponent(state = it, action = {
                if (it is ViewModelBaseState.DataSavedState) {
                    navController.navigate(HOME_ROUTE)
                }
            })
        }
    }

    LaunchedEffect(recipe) {
        Log.i("NewRecipeView", "NewRecipeView: recipe changed $recipe")
        keyboardController?.hide()
        pagerState.animateScrollToPage(pagerState.currentPage + 1)
    }

    LaunchedEffect(Unit) {
        newRecipeViewModel.buildFirstPage()
    }

}
