@file:OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)

package com.ilustris.cuccina.ui.theme

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.feature.home.ui.component.HighLightPage
import com.ilustris.cuccina.feature.profile.ui.component.ChefsPageView
import com.ilustris.cuccina.feature.profile.ui.component.ProfilePageView
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.IngredientMapper
import com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui.IngredientsPageView
import com.ilustris.cuccina.feature.recipe.step.presentation.ui.StepPageView
import com.ilustris.cuccina.feature.recipe.step.presentation.ui.StepsPageView
import com.ilustris.cuccina.feature.recipe.ui.component.RecipePageView
import com.ilustris.cuccina.feature.recipe.ui.component.RecipesPageView

@Preview(showBackground = true)
@Composable
fun PagePreview() {
    CuccinaTheme {
        val pages = listOf(
            Page.SimplePage("Vamos cozinhar?", "Prepare sua cozinha e vamos começar!"),
            Page.AnimatedTextPage(
                "Vamos cozinhar?",
                description = "Prepare sua cozinha e vamos começar!",
                IngredientMapper.emojiList().take(3)
            )
        )
        getPageView(pages.last(), {}, {}, {})
    }

}

@Composable
fun PageIndicators(
    count: Int,
    currentPage: Int,
    modifier: Modifier,
    enableAutoSwipe: Boolean = false,
    onFinishPageLoad: (Int) -> Unit,
    onSelectIndicator: (Int) -> Unit
) {


    fun isCurrentPage(index: Int) = currentPage == index

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(count) { index ->

            val delay = if (!isCurrentPage(index)) 500 else 10000
            var progressTarget by remember {
                mutableStateOf(0f)
            }

            val animateProgress by animateFloatAsState(
                targetValue = progressTarget,
                animationSpec = tween(
                    durationMillis = delay,
                    delayMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            )

            val weight = 1f / count

            progressTarget = if (isCurrentPage(index)) {
                1f
            } else {
                0f
            }

            LinearProgressIndicator(
                trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                color = MaterialTheme.colorScheme.secondary,
                strokeCap = StrokeCap.Round,
                progress = animateProgress,
                modifier = Modifier
                    .padding(1.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .weight(weight)
                    .height(5.dp)
                    .clickable {
                        onSelectIndicator(index)
                    }
            )

            LaunchedEffect(animateProgress) {
                snapshotFlow { animateProgress }.collect { progress ->
                    if (progress == 1f && enableAutoSwipe) {
                        onFinishPageLoad(index)
                    }
                }
            }
        }
    }
}


@Composable
fun getPageView(
    page: Page,
    openRecipe: (String) -> Unit,
    openChefPage: (String) -> Unit,
    navigateToNewRecipe: () -> Unit
) {
    return when (page) {
        is Page.SimplePage -> SimplePageView(page)
        is Page.StepsPage -> StepsPageView(page)
        is Page.IngredientsPage -> IngredientsPageView(page)
        is Page.StepPage -> StepPageView(page)
        is Page.RecipePage -> RecipePageView(page)
        is Page.HighlightPage -> HighLightPage(page = page, openRecipe = openRecipe)
        is Page.AnimatedTextPage -> AnimatedTextPage(page = page)
        is Page.ProfilePage -> ProfilePageView(page = page)
        is Page.RecipeListPage -> RecipesPageView(page = page, openRecipe = openRecipe)
        is Page.SuccessPage -> SuccessPageView(page = page, navigateToNewRecipe)
        is Page.OtherChefsPage -> ChefsPageView(page = page, openChefPage = openChefPage)
        else -> {}
    }
}


