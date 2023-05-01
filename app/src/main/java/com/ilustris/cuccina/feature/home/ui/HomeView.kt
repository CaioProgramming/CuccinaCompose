@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.ilustris.cuccina.feature.home.ui

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.ilustris.cuccina.MainViewModel
import com.ilustris.cuccina.feature.home.presentation.HomeViewModel
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.category.ui.component.CategoryBadge
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.ui.component.RecipeCard
import com.ilustris.cuccina.feature.recipe.ui.component.StateComponent
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.silent.ilustriscore.core.model.ViewModelBaseState

const val HOME_ROUTE = "HOME_ROUTE"

@Composable
fun HomeView(homeViewModel: HomeViewModel?) {
    val context = LocalContext.current
    val homeState = homeViewModel?.viewModelState?.observeAsState()
    val mainViewModel: MainViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        homeViewModel?.getAllData()
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
                    mainViewModel.checkUser()
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


    LazyColumn {

        item {
            val categories = Category.values().toList().sortedBy { it.description }
            var selectedCategory: Category? by remember {
                mutableStateOf(null)
            }
            LazyRow {

                items(categories.size) {
                    CategoryBadge(category = categories[it], selectedCategory) { category ->
                        Toast.makeText(
                            context,
                            "open category ${category.title}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        selectedCategory = category
                    }
                }
            }
        }

        homeState?.value?.let {
            if (it is ViewModelBaseState.DataListRetrievedState) {
                val recipes = it.dataList as List<Recipe>
                items(recipes.size) { index ->
                    val recipe = recipes[index]
                    RecipeCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.3f), recipe = recipe
                    )

                }
            } else {
                item {
                    getStateComponent(state = it)
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