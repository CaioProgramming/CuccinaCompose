@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.ilustris.cuccina.feature.recipe.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.ilustris.cuccina.feature.recipe.domain.model.RecipeGroup
import com.ilustris.cuccina.feature.recipe.ui.component.RecipeCard
import com.ilustris.cuccina.ui.theme.CuccinaTheme

@Composable
fun RecipeGroupList(recipeGroup: RecipeGroup, orientation: Int) {

    Column {
        Text(
            text = recipeGroup.title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        if (orientation == VERTICAL) {
            recipeGroup.recipes.forEach {
                RecipeCard(
                    recipe = it,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .padding(8.dp)
                )
            }
        } else {
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp
            LazyRow {

                items(recipeGroup.recipes.size) {
                    RecipeCard(
                        recipe = recipeGroup.recipes[it],
                        modifier = Modifier
                            .width(300.dp)
                            .height(150.dp)
                    )
                }
            }
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
        )

    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecipeListPreview() {
    CuccinaTheme {
        Column {
            RecipeGroupList(RecipeGroup("Receitas Novas", emptyList()), HORIZONTAL)
            RecipeGroupList(RecipeGroup("Receitas disponíveis", emptyList()), VERTICAL)
        }

    }
}