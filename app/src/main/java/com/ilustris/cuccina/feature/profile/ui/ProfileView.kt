@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalPagerApi::class,
    ExperimentalAnimationApi::class
)

package com.ilustris.cuccina.feature.profile.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.VerticalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ilustris.cuccina.feature.profile.presentation.ProfileViewModel
import com.ilustris.cuccina.feature.recipe.start.ui.START_RECIPE_ROUTE_IMPL
import com.ilustris.cuccina.feature.recipe.ui.component.StateComponent
import com.ilustris.cuccina.ui.theme.CuccinaLoader
import com.ilustris.cuccina.ui.theme.getPageView
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

const val PROFILE_ROUTE = "profile/{userId}"
const val PROFILE_ROUTE_IMPL = "profile/"


@Composable
fun ProfileView(
    userId: String? = null,
    profileViewModel: ProfileViewModel,
    navController: NavController
) {

    val baseState = profileViewModel.viewModelState.observeAsState()
    val user = profileViewModel.user.observeAsState()
    val pages = profileViewModel.pages.observeAsState()
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        profileViewModel.getUserData(userId?.replace("{userId}", ""))
    }

    LaunchedEffect(user) {
        snapshotFlow { user.value }.distinctUntilChanged().collect {
            it?.let { user ->
                profileViewModel.getUserRecipes(user.uid)
                profileViewModel.getUserFavoriteRecipes(user.uid)
                systemUiController.isStatusBarVisible = false
            }
        }
    }

    fun openRecipe(recipeId: String) {
        navController.navigate("$START_RECIPE_ROUTE_IMPL${recipeId}")
    }

    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = baseState.value == ViewModelBaseState.LoadingState,
        enter = scaleIn(),
        exit = fadeOut(tween(1500))
    ) {
        CuccinaLoader()
    }

    AnimatedVisibility(
        visible = baseState.value is ViewModelBaseState.ErrorState,
        enter = fadeIn()
    ) {
        val error = (baseState.value as ViewModelBaseState.ErrorState).dataException.code.message
        StateComponent(message = "Ocorreu um erro ao carregar suas informações\n($error)")
    }


    AnimatedVisibility(visible = pages.value != null, enter = fadeIn(), exit = fadeOut()) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(tween(500))
        ) {

            val pagerState = rememberPagerState()
            val (pager, nextButton, nextTitle) = createRefs()
            val scope = rememberCoroutineScope()

            val isComplete = pagerState.currentPage == pages.value!!.lastIndex

            val iconColor =
                if (isComplete) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
            val backColor =
                if (pagerState.currentPage == 0) MaterialTheme.colorScheme.primary else Color.Transparent
            val icon =
                if (isComplete) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown

            val iconColorAnimation by animateColorAsState(
                targetValue = iconColor,
                animationSpec = tween(500)
            )
            val backColorAnimation by animateColorAsState(
                targetValue = backColor,
                animationSpec = tween(250, easing = EaseInOut)
            )

            val nextPageTitle = if (!isComplete) {
                pages.value!![pagerState.currentPage + 1].title
            } else {
                ""
            }

            VerticalPager(
                count = pages.value!!.size,
                state = pagerState,
                modifier = Modifier.constrainAs(pager) {
                    if (isComplete) {
                        bottom.linkTo(parent.bottom)
                    } else {
                        bottom.linkTo(nextTitle.top)
                    }
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }) {
                getPageView(page = pages.value!![it], openRecipe = { id ->
                    openRecipe(id)
                },{}, {})
            }


            AnimatedVisibility(visible = !isComplete,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .constrainAs(nextTitle) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(16.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxWidth()) {
                Text(
                    text = nextPageTitle,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(8.dp)
                )
            }

            IconButton(modifier = Modifier
                .constrainAs(nextButton) {
                    bottom.linkTo(parent.bottom, margin = 36.dp)
                    start.linkTo(pager.start)
                    end.linkTo(pager.end)
                }
                .padding(16.dp)
                .size(90.dp, 50.dp)
                .background(backColorAnimation, CircleShape),
                onClick = {
                    scope.launch {
                        if (pagerState.currentPage != pages.value!!.lastIndex) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                }) {
                AnimatedContent(targetState = icon, transitionSpec = {
                    EnterTransition.None with ExitTransition.None
                }) { target ->
                    Icon(
                        target,
                        modifier = Modifier
                            .animateEnterExit(
                                enter = scaleIn(),
                                exit = scaleOut()
                            )
                            .size(32.dp),
                        contentDescription = if (isComplete) "Voltar" else "Avançar",
                        tint = iconColorAnimation
                    )
                }
            }

        }

    }

}