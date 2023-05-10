@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.tooling.preview.Preview
import com.ilustris.cuccina.feature.recipe.form.presentation.viewmodel.NewRecipeViewModel
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.getFormView

const val NEW_RECIPE_ROUTE = "new_recipe"

@Composable
fun NewRecipeView(newRecipeViewModel: NewRecipeViewModel? = null) {

    val recipe = newRecipeViewModel?.recipe?.observeAsState()?.value
    val pages = newRecipeViewModel?.pages?.observeAsState()?.value
    val pagerState = rememberPagerState()

    Log.i("NewRecipeView", "NewRecipeView: current recipe $recipe")

    AnimatedVisibility(visible = pages?.isNotEmpty() == true, enter = fadeIn(), exit = fadeOut()) {
        val pageList = pages!!
        HorizontalPager(pageCount = pageList.size, state = pagerState) {
            getFormView(formPage = pageList[it])
        }
    }

    LaunchedEffect(recipe) {
        snapshotFlow { recipe }.collect {
            Log.i("NewRecipeView", "NewRecipeView: recipe changed $it")
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
        pagerState.animateScrollToPage(pagerState.currentPage + 1)
    }

    LaunchedEffect(Unit) {
        newRecipeViewModel?.buildFirstPage()
    }


}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NewRecipeFormPreview() {
    CuccinaTheme {
        NewRecipeView()
    }
}