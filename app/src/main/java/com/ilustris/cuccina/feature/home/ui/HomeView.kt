@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package com.ilustris.cuccina.feature.home.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.cuccina.feature.home.presentation.HomeViewModel
import com.ilustris.cuccina.feature.home.ui.component.BannerCard
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.category.ui.component.CategoryBadge
import com.ilustris.cuccina.feature.recipe.domain.model.RecipeGroup
import com.ilustris.cuccina.feature.recipe.ui.RecipeGroupList
import com.ilustris.cuccina.feature.recipe.ui.component.StateComponent
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.silent.ilustriscore.core.model.ViewModelBaseState
import kotlinx.coroutines.launch

const val HOME_ROUTE = "HOME_ROUTE"

@Composable
fun HomeView(homeViewModel: HomeViewModel?) {
    val context = LocalContext.current
    val homeBaseState = homeViewModel?.viewModelState?.observeAsState()
    val homeList = homeViewModel?.homeList?.observeAsState()
    val highLights = homeViewModel?.highlightRecipes?.observeAsState()
    val categories = Category.values().toList().sortedBy { it.description }
    var selectedCategory = homeViewModel?.currentCategory?.observeAsState()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        homeViewModel?.loadHome()
    }
    @Composable
    fun getStateComponent(state: ViewModelBaseState) {


        val message = when (state) {
            ViewModelBaseState.RequireAuth -> "Você precisa estar logado para acessar essa funcionalidade"
            ViewModelBaseState.DataDeletedState -> "Receita deletada com sucesso"
            ViewModelBaseState.LoadingState -> "Carregando..."
            ViewModelBaseState.LoadCompleteState -> "Carregamento completo"
            is ViewModelBaseState.DataRetrievedState -> "Receita carregada com sucesso"
            is ViewModelBaseState.DataListRetrievedState -> "Receitas carregadas com sucesso"
            is ViewModelBaseState.DataSavedState -> "Dados salvos com sucesso"
            is ViewModelBaseState.DataUpdateState -> "Dados atualizados com sucesso"
            is ViewModelBaseState.FileUploadedState -> "Arquivos enviados com sucesso"
            is ViewModelBaseState.ErrorState -> "Ocorreu um erro inesperado(${state.dataException.code.message}"
        }

        val buttonText = when (state) {
            ViewModelBaseState.RequireAuth -> "Fazer login"
            ViewModelBaseState.DataDeletedState -> "Ok"
            is ViewModelBaseState.ErrorState -> "Tentar novamente"
            else -> null
        }

        val action = {
            when (state) {
                ViewModelBaseState.RequireAuth -> {
                    Log.e("HomeView", "getStateComponent: Login required")
                }
                else -> {
                    homeViewModel?.getAllData()
                }
            }
        }


        StateComponent(
            message = message,
            action = { action.invoke() },
            buttonText = buttonText
        )
    }


    ModalBottomSheetLayout(
        sheetState = sheetState,

        sheetShape = RoundedCornerShape(defaultRadius),
        sheetContent = {
            highLights?.value?.let {
                HighlightPager(recipes = it, closeHighlights = {
                    scope.launch {
                        sheetState.hide()
                    }
                }, openRecipe = {
                    scope.launch {
                        sheetState.hide()
                    }
                })
            }
        }) {
        LazyColumn {

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

            highLights?.value?.let { recipes ->
                item {
                    BannerCard(recipes.last()) {
                        scope.launch {
                            sheetState.show()
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
                            getStateComponent(state = it)
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
                        RecipeGroupList(recipeGroup = group, orientation = RecyclerView.HORIZONTAL)
                    }
                }
            } else {
                homeList?.value?.let {
                    items(it.size) { index ->
                        val group = it[index]
                        RecipeGroupList(recipeGroup = group, orientation = RecyclerView.HORIZONTAL)
                    }
                }
            }
        }
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun homePreview() {
    CuccinaTheme {
        HomeView(null)
    }
}