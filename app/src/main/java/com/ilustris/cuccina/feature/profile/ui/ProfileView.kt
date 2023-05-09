@file:OptIn(ExperimentalFoundationApi::class)

package com.ilustris.cuccina.feature.profile.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ilustris.cuccina.feature.profile.presentation.ProfileViewModel
import com.ilustris.cuccina.feature.recipe.start.ui.START_RECIPE_ROUTE_IMPL
import com.ilustris.cuccina.feature.recipe.ui.component.RecipeCard
import com.ilustris.cuccina.feature.recipe.ui.component.StateComponent
import com.ilustris.cuccina.ui.theme.CuccinaLoader
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.distinctUntilChanged

const val PROFILE_ROUTE = "profile"


@Composable
fun ProfileView(profileViewModel: ProfileViewModel, navController: NavController) {

    val baseState = profileViewModel.viewModelState.observeAsState()
    val user = profileViewModel.user.observeAsState()
    val recipes = profileViewModel.recipes.observeAsState()
    val favorites = profileViewModel.favoriteRecipes.observeAsState()
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        profileViewModel.getUserData()
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
        enter = slideInVertically(),
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


    AnimatedVisibility(visible = user.value != null, enter = fadeIn(), exit = fadeOut()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            val userData = user.value!!

            item {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(0.dp, 0.dp, defaultRadius, defaultRadius)
                        )
                        .padding(16.dp)

                ) {
                    val (profilePic, username, infos) = createRefs()

                    GlideImage(
                        imageModel = { userData.photoUrl },
                        imageOptions = ImageOptions(
                            alignment = Alignment.Center,
                            "",
                            contentScale = ContentScale.Fit
                        ),
                        loading = {
                            CuccinaLoader()
                        },
                        modifier = Modifier
                            .constrainAs(profilePic) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .size(150.dp)
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .clip(CircleShape)
                    )

                    Text(
                        userData.name,
                        style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.onPrimary),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .constrainAs(username) {
                                top.linkTo(profilePic.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .constrainAs(infos) {
                                top.linkTo(username.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "${recipes.value?.size ?: 0}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Receitas",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                                )
                            )
                        }
                        Column(
                            modifier = Modifier.padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "${favorites.value?.size ?: 0}",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Curtidas",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                                )
                            )
                        }
                    }

                }


            }


            recipes.value?.let {
                if (it.isNotEmpty()) {
                    item {
                        Text(
                            text = "Receitas",
                            style = MaterialTheme.typography.headlineMedium,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Divider(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                        )
                    }
                    items(it.size) { index ->
                        RecipeCard(modifier = Modifier.fillMaxSize(),
                            recipe = it[index],
                            onClickRecipe = { recipe ->
                                openRecipe(recipe.id)
                            })
                    }
                } else {
                    item {
                        StateComponent(message = "Você ainda não possui receitas")
                    }
                }
            }

            favorites.value?.let {
                if (it.isNotEmpty()) {
                    item {
                        Text(text = "Favoritos", style = MaterialTheme.typography.headlineSmall)
                        Divider(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                        )
                    }
                    items(it.size) { index ->
                        RecipeCard(modifier = Modifier.fillMaxSize(),
                            recipe = it[index],
                            onClickRecipe = { recipe ->
                                openRecipe(recipe.id)
                            })
                    }
                } else {
                    item {
                        StateComponent(message = "Você ainda não possui receitas favoritas")
                    }
                }
            }


        }
    }


}