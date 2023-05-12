@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class,
    ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class
)

package com.ilustris.cuccina.feature.home.ui

import ai.atick.material.MaterialColor
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.recyclerview.widget.RecyclerView
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ilustris.cuccina.feature.home.presentation.HomeViewModel
import com.ilustris.cuccina.feature.home.ui.component.BannerCard
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.category.ui.component.CategoryBadge
import com.ilustris.cuccina.feature.recipe.domain.model.RecipeGroup
import com.ilustris.cuccina.feature.recipe.form.ui.NEW_RECIPE_ROUTE
import com.ilustris.cuccina.feature.recipe.start.ui.START_RECIPE_ROUTE_IMPL
import com.ilustris.cuccina.feature.recipe.ui.RecipeGroupList
import com.ilustris.cuccina.ui.theme.CuccinaLoader
import com.ilustris.cuccina.ui.theme.Page
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.ilustris.cuccina.ui.theme.getStateComponent
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.*

const val HOME_ROUTE = "home"

@Composable
fun HomeView(homeViewModel: HomeViewModel?, navController: NavHostController) {
    val homeBaseState = homeViewModel?.viewModelState?.observeAsState()
    val homeList = homeViewModel?.homeList?.observeAsState()
    val highLights = homeViewModel?.highlightRecipes?.observeAsState()
    val categories = Category.values().toList().sortedBy { it.description }
    val selectedCategory = homeViewModel?.currentCategory?.observeAsState()
    val systemUiController = rememberSystemUiController()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val scope = rememberCoroutineScope()
    var query by remember {
        mutableStateOf("")
    }


    LaunchedEffect(Unit) {
        homeViewModel?.loadHome()
    }

    LaunchedEffect(sheetState) {
        snapshotFlow { sheetState.currentValue }.distinctUntilChanged().collect {
            systemUiController.isStatusBarVisible = it == ModalBottomSheetValue.Hidden
        }
    }

    fun navigateToRecipe(recipeId: String) {
        navController.navigate("${START_RECIPE_ROUTE_IMPL}${recipeId}")
    }


    ModalBottomSheetLayout(
        modifier = Modifier.animateContentSize(tween(500)),
        sheetState = sheetState,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            highLights?.value?.let {
                HighLightSheet(pages = it, autoSwipe = sheetState.isVisible, closeButton = {
                    scope.launch {
                        sheetState.hide()
                    }
                }, openRecipe = { id ->
                    navigateToRecipe(id)
                }, openNewRecipe = {
                    navController.navigate(NEW_RECIPE_ROUTE)
                }, openChefPage = { chefId -> })
            }
        }) {

        BackHandler {
            if (sheetState.isVisible) {
                scope.launch {
                    sheetState.hide()
                }
            } else {
                navController.popBackStack()
            }
        }

        fun isLoading() = homeBaseState?.value == ViewModelBaseState.LoadingState

        AnimatedVisibility(
            visible = isLoading(),
            enter = fadeIn(tween(500)),
            exit = scaleOut(tween(500))
        ) {
            CuccinaLoader()
        }

        AnimatedVisibility(
            visible = !isLoading(),
            enter = fadeIn(
                tween(1500)
            ),
            exit = fadeOut(tween(500))
        ) {


            LazyColumn(
                modifier = Modifier
                    .animateContentSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {

                stickyHeader {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painterResource(id = com.ilustris.cuccina.R.drawable.cherry),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                                    contentDescription = "Cuccina",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .padding(4.dp)
                                )
                                Text(
                                    text = "Cuccina",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Black
                                    )
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                    )
                }

                item {
                    TextField(
                        value = query,
                        leadingIcon = {
                            Icon(Icons.Rounded.Search, contentDescription = "Pesquisar")
                        },
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = query.isNotEmpty(),
                                enter = fadeIn() + scaleIn(),
                                exit = fadeOut()
                            ) {
                                Icon(
                                    Icons.Sharp.Close,
                                    contentDescription = "fechar",
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .clickable {
                                            keyboardController?.hide()
                                            focusManager.clearFocus()
                                            query = ""
                                            homeViewModel?.searchRecipe(query)
                                        }
                                )
                            }

                        },
                        onValueChange = {
                            query = it
                        },
                        placeholder = {
                            Text(
                                text = "Busque por receitas ou ingredientes...",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        },
                        shape = RoundedCornerShape(defaultRadius),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Search,
                            autoCorrect = true
                        ),
                        keyboardActions = KeyboardActions(onSearch = {
                            focusManager.clearFocus()
                            keyboardController?.hide()
                            homeViewModel?.searchRecipe(query)
                        }),
                        maxLines = 1,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    )
                }

                item {

                    AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
                        LazyRow(modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)) {

                            items(categories.size) {
                                CategoryBadge(
                                    category = categories[it],
                                    selectedCategory?.value
                                ) { category ->
                                    homeViewModel?.updateCategory(category)
                                }
                            }
                        }
                    }
                }

                highLights?.value?.let { highLights ->
                    item {
                        val highLightPage =
                            highLights.find { it is Page.HighlightPage } as? Page.HighlightPage
                        highLightPage?.let { page ->
                            BannerCard(page.backgroundImage) {
                                scope.launch {
                                    sheetState.show()
                                }
                            }
                        }

                    }
                }

                homeBaseState?.value?.let {
                    when (it) {
                        ViewModelBaseState.LoadCompleteState -> {
                            Log.i(javaClass.simpleName, "HomeView: Load complete")
                        }
                        else -> {
                            item {
                                getStateComponent(state = it) { state ->
                                    if (state is ViewModelBaseState.ErrorState) {
                                        homeViewModel.loadHome()
                                    }
                                }
                            }
                        }
                    }
                }

                Log.i(javaClass.simpleName, "HomeView: ${homeList?.value} ")
                Log.i(javaClass.simpleName, "HomeView: current category ${selectedCategory?.value}")


                fun getHomeList(): List<RecipeGroup> {
                    return homeList?.value?.filter { it.title == selectedCategory?.value?.title }
                        ?: emptyList()
                }

                if (selectedCategory?.value != null) {
                    getHomeList().run {
                        items(size) { index ->
                            val group = this@run[index]
                            RecipeGroupList(
                                recipeGroup = group,
                                orientation = RecyclerView.HORIZONTAL
                            ) {
                                navigateToRecipe(it.id)
                            }
                        }
                    }
                } else {
                    homeList?.value?.let {
                        items(it.size) { index ->
                            val group = it[index]
                            RecipeGroupList(
                                recipeGroup = group,
                                orientation = RecyclerView.HORIZONTAL
                            ) { recipe ->
                                navigateToRecipe(recipe.id)
                            }
                        }
                    }
                }

                if (homeList?.value?.isNotEmpty() == true) {
                    item {
                        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                        Text(
                            text = "Todas as receitas foram obtidas através de sites públicos e não possuem fins lucrativos.",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = "Desenvolvido por ilustris em 2019 - $currentYear",
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontStyle = FontStyle.Italic,
                                color = MaterialColor.LightBlueA100
                            )
                        )
                    }
                }

            }
        }


    }


}
