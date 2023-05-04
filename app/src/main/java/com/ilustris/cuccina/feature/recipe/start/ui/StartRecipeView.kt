@file:OptIn(ExperimentalPagerApi::class)

package com.ilustris.cuccina.feature.recipe.start.ui

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.start.presentation.StartRecipeViewModel
import com.ilustris.cuccina.feature.recipe.ui.component.StateComponent
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

const val START_RECIPE_ROUTE = "start_recipe/{recipeId}"
const val START_RECIPE_ROUTE_IMPL = "start_recipe/"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StartRecipeView(recipeId: String? = null, startRecipeViewModel: StartRecipeViewModel? = null) {

    val state = startRecipeViewModel?.viewModelState?.observeAsState()
    val recipe = remember {
        mutableStateOf<Recipe?>(null)
    }
    val pages = startRecipeViewModel?.pages?.observeAsState()
    var progress by remember { mutableStateOf(0f) }
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000)
    )

    Log.i("StartRecipeView", "StartRecipeView: current state -> ${state?.value}")
    Log.i("StartRecipeView", "StartRecipeView: recipe state -> ${recipe.value}")

    recipe.value?.let {
        if (pages?.value == null || pages.value?.isEmpty() == true) {
            Log.i("StarRecipeView", "StartRecipeView: building pages for -> $it")
            startRecipeViewModel?.getPages(it)
        }



        Log.i("StartView", "StartRecipeView: building view for -> $it")
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (title, pager, indicator, nextButton, progressBar) = createRefs()
            Text(
                text = it.name,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(title) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge
            )
            pages?.value?.let { pages ->


                fun getPageProgress(
                    currentPage: Int,
                    lastPage: Int
                ) = (currentPage) / lastPage.toFloat()

                val pagerState = rememberPagerState()
                val scope = rememberCoroutineScope()

                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect { page ->
                        progress = getPageProgress(page, pages.lastIndex)
                    }
                }
                HorizontalPager(
                    pageCount = pages.size,
                    state = pagerState,
                    modifier = Modifier.constrainAs(pager) {
                        top.linkTo(title.bottom)
                        bottom.linkTo(indicator.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }) { index ->
                    getPageView(page = pages[index])
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .constrainAs(indicator) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                        }
                        .padding(16.dp)
                        .wrapContentHeight()
                        .fillMaxWidth(0.2f)
                ) {
                    repeat(pages.size) { index ->
                        val isCurrentPage = pagerState.currentPage == index
                        val color = if (isCurrentPage) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        }
                        val weight = if (isCurrentPage) 1f else 0.1f
                        Box(
                            modifier = Modifier
                                .padding(1.dp)
                                .animateContentSize()
                                .clip(RoundedCornerShape(5.dp))
                                .background(color)
                                .weight(weight)
                                .height(5.dp)
                        )
                    }
                }

                IconButton(modifier = Modifier
                    .constrainAs(nextButton) {
                        top.linkTo(progressBar.top, 4.dp)
                        bottom.linkTo(progressBar.bottom, 4.dp)
                        start.linkTo(progressBar.start, 4.dp)
                        end.linkTo(progressBar.end, 4.dp)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints

                    }
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.onBackground, CircleShape),
                    onClick = {
                        scope.launch {
                            if (pagerState.currentPage != pages.lastIndex) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }) {
                    val icon = if (pagerState.currentPage == pages.lastIndex) {
                        Icons.Default.Check
                    } else {
                        Icons.Default.KeyboardArrowRight
                    }
                    Icon(
                        icon,
                        modifier = Modifier.animateContentSize(),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }


                CircularProgressIndicator(
                    progress = progress,
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 2.dp,
                    strokeCap = StrokeCap.Round,
                    modifier = Modifier
                        .constrainAs(progressBar) {
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        }
                        .size(70.dp)
                        .padding(8.dp)
                )
            }
        }
    } ?: run {
        Log.i("StarRecipeView", "StartRecipeView: querying recipe -> $recipeId")
        recipeId?.let { startRecipeViewModel?.getSingleData(it) }
    }

    state?.value?.run {
        when (this) {
            ViewModelBaseState.LoadingState -> StateComponent(message = "Carregando receita...")

            is ViewModelBaseState.DataRetrievedState -> {
                val foundedRecipe = this.data as Recipe
                recipe.value = foundedRecipe
            }
            is ViewModelBaseState.ErrorState -> {
                StateComponent(
                    message = this.dataException.code.message,
                    buttonText = "Tentar novamente",
                    action = {
                        recipeId?.let { startRecipeViewModel.getSingleData(it) }
                    })
            }
            else -> {}

        }
    }


}

