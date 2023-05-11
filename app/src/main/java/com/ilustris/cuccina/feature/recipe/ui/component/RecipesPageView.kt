@file:OptIn(ExperimentalFoundationApi::class)

package com.ilustris.cuccina.feature.recipe.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.ui.theme.Page

@Composable
fun RecipesPageView(page: Page.RecipeListPage, openRecipe: (String) -> Unit) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Text(
                text = page.title,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item(span = StaggeredGridItemSpan.FullLine) {
            Text(
                text = page.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(page.recipes.size) { index ->
            RecipeCard(
                recipe = page.recipes[index],
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) { recipe ->
                openRecipe(recipe.id)
            }
        }
    }
}