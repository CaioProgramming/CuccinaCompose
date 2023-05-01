@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.ilustris.cuccina.feature.recipe.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.ui.component.RecipeCard
import com.ilustris.cuccina.ui.theme.CuccinaTheme

@Composable
fun RecipeGroupList(title: String? = null, orientation: Int, count: Int) {

    Column {
        title?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
    if (orientation == VERTICAL) {
        LazyColumn {
            items(count) {
                RecipeCard(
                    recipe = Recipe(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }

    } else {
        LazyRow {
            items(count) {
                RecipeCard(recipe = Recipe(), modifier = Modifier.size(250.dp, 100.dp))
            }
        }
    }
    
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
        .height(1.dp)
        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f)))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecipeListPreview() {
    CuccinaTheme {
        Column {
            RecipeGroupList("Receitas Novas", HORIZONTAL, 5)
            RecipeGroupList("Receitas disponíveis", VERTICAL, 3)
        }

    }
}