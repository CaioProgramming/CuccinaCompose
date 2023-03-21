@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.ilustris.cuccina.feature.home.ui

import android.widget.Toast
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.category.ui.component.CategoryBadge
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.ui.RecipeGroupList

const val HOME_ROUTE = "HOME_ROUTE"
@Composable
fun HomeView() {

    val context = LocalContext.current
    Column {
        LazyRow() {
            val categories = Category.values().toList().sortedBy { it.description }
            items(categories.size) {
                CategoryBadge(category = categories[it]) { category ->
                    Toast.makeText(context, "open category ${category.title}", Toast.LENGTH_SHORT)
                        .show()
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