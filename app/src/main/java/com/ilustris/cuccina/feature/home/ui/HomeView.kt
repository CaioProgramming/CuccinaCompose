@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.ilustris.cuccina.feature.home.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.category.ui.component.CategoryBadge
import com.ilustris.cuccina.feature.recipe.ui.RecipeGroupList
import com.ilustris.cuccina.ui.theme.CuccinaTheme

const val HOME_ROUTE = "HOME_ROUTE"

@Composable
fun HomeView() {

    val context = LocalContext.current
    Column {
        val categories = Category.values().toList().sortedBy { it.description }
        var selectedCategory by remember {
            mutableStateOf(categories.first())
        }
        LazyRow {

            items(categories.size) {
                CategoryBadge(category = categories[it], selectedCategory) { category ->
                    Toast.makeText(context, "open category ${category.title}", Toast.LENGTH_SHORT)
                        .show()
                    selectedCategory = category
                }
            }
        }
        RecipeGroupList(
            "Receitas para o fim de semana",
            orientation = RecyclerView.HORIZONTAL,
            count = 3
        )
        RecipeGroupList("Receitas fitness", orientation = RecyclerView.VERTICAL, count = 10)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun homePreview() {
    CuccinaTheme {
        HomeView()
    }
}