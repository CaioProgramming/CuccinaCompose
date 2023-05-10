@file:OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)

package com.ilustris.cuccina.feature.recipe.start.ui

import ai.atick.material.MaterialColor
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
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
fun StartRecipeView(
    recipeId: String? = null,
    startRecipeViewModel: StartRecipeViewModel? = null,
    navController: NavHostController
) {

    val state = startRecipeViewModel?.viewModelState?.observeAsState()
    val recipe = remember {
        mutableStateOf<Recipe?>(null)
    }
    val isFavorite = startRecipeViewModel?.isFavorite?.observeAsState()
    val pages = startRecipeViewModel?.pages?.observeAsState()
    var progress by remember { mutableStateOf(0f) }
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000)
    )



    recipe.value?.let {
        if (pages?.value == null || pages.value?.isEmpty() == true) {
            startRecipeViewModel?.getPages(it)
        }

        ConstraintLayout(
            modifier = Modifier
                .animateContentSize(tween(1000))
                .fillMaxSize()
        ) {
            val (title, pager, indicator, nextButton, progressBar, backButton, favoriteButton) = createRefs()

            pages?.value?.let { pages ->


                fun getPageProgress(
                    currentPage: Int,
                    lastPage: Int
                ) = (currentPage) / lastPage.toFloat()

                val pagerState = rememberPagerState()
                val scope = rememberCoroutineScope()

                val isComplete = pagerState.currentPage == pages.lastIndex

                val iconColor =
                    if (isComplete) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.background
                val backColor =
                    if (isComplete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                val icon = if (isComplete) Icons.Default.Check else Icons.Default.KeyboardArrowRight

                val iconColorAnimation by animateColorAsState(
                    targetValue = iconColor,
                    animationSpec = tween(1000)
                )
                val backColorAnimation by animateColorAsState(
                    targetValue = backColor,
                    animationSpec = tween(1000)
                )



                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect { page ->
                        progress = getPageProgress(page, pages.lastIndex)
                    }
                }

                BackHandler {
                    if (pagerState.currentPage > 0) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    } else {
                        navController.popBackStack()
                    }
                }



                HorizontalPager(
                    pageCount = pages.size,
                    userScrollEnabled = false,
                    state = pagerState,
                    modifier = Modifier.constrainAs(pager) {
                        top.linkTo(title.bottom)
                        bottom.linkTo(indicator.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }) { index ->
                    getPageView(page = pages[index]) {}
                }

                Text(
                    text = it.name,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .constrainAs(title) {
                            top.linkTo(parent.top)
                            start.linkTo(backButton.end)
                            end.linkTo(favoriteButton.start)
                            width = Dimension.fillToConstraints
                        }
                        .padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        shadow = Shadow(
                            color = MaterialColor.Black,
                            offset = Offset(1f, 1f),
                            blurRadius = 1.3f
                        )
                    )
                )

                IconButton(modifier = Modifier
                    .constrainAs(backButton) {
                        top.linkTo(title.top)
                        bottom.linkTo(title.bottom)
                        start.linkTo(parent.start)
                        height = Dimension.fillToConstraints
                    }
                    .animateContentSize(), onClick = {
                    if (pagerState.currentPage > 0) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    } else {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Voltar",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }

                IconButton(modifier = Modifier.constrainAs(favoriteButton) {
                    top.linkTo(title.top)
                    bottom.linkTo(title.bottom)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                }, onClick = {
                    startRecipeViewModel.favoriteRecipe(it)
                }) {
                    val favoriteIcon =
                        if (isFavorite?.value == true) Icons.Default.Favorite else Icons.Default.FavoriteBorder
                    val color =
                        if (isFavorite?.value == true) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground
                    val description = if (isFavorite?.value == true) "Desfavoritar" else "Favoritar"
                    Icon(favoriteIcon, tint = color, contentDescription = description)
                }

                PageIndicators(
                    count = pages.size,
                    currentPage = pagerState.currentPage,
                    modifier = Modifier
                        .constrainAs(indicator) {
                            bottom.linkTo(nextButton.bottom)
                            top.linkTo(nextButton.top)
                            start.linkTo(parent.start)
                            end.linkTo(nextButton.start)
                            width = Dimension.fillToConstraints
                        }
                        .padding(horizontal = 16.dp)
                        .wrapContentHeight(),
                    onSelectIndicator = {
                        scope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    },
                    onFinishPageLoad = {
                        scope.launch {
                            if (pagerState.currentPage != pages.lastIndex) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }
                )


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
                    .background(backColorAnimation, CircleShape),
                    onClick = {
                        scope.launch {
                            if (pagerState.currentPage != pages.lastIndex) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    }) {
                    AnimatedContent(targetState = icon, transitionSpec = {
                        EnterTransition.None with ExitTransition.None
                    }) { target ->
                        Icon(
                            target,
                            modifier = Modifier.animateEnterExit(
                                enter = scaleIn(),
                                exit = scaleOut()
                            ),
                            contentDescription = if (!isComplete) "Voltar" else "Finalizar Receita",
                            tint = iconColorAnimation
                        )
                    }
                }


                CircularProgressIndicator(
                    progress = progressAnimation,
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
            } ?: kotlin.run {
                StateComponent(message = "Carregando...")
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

            is ViewModelBaseState.DataUpdateState -> {
                val updatedRecipe = this.data as Recipe
                recipe.value = updatedRecipe
                startRecipeViewModel.checkFavorite(updatedRecipe)
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

