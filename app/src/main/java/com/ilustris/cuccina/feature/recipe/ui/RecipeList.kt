@file:OptIn(ExperimentalFoundationApi::class, ExperimentalFoundationApi::class)

package com.ilustris.cuccina.feature.recipe.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Orientation
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.ilustris.cuccina.feature.recipe.ui.component.RecipeCard
import com.ilustris.cuccina.ui.theme.CuccinaTheme

@Composable
fun RecipeGroupList(title: String? = null, orientation: Int, count: Int) {

    if (orientation == VERTICAL) {
        LazyColumn {
            title?.let {
                stickyHeader {
                    Text(text = it)
                }
            }

            items(count) {
                RecipeCard(cardModifier = Modifier.fillMaxWidth().height(200.dp))
            }
        }

    } else {
        LazyRow() {
            title?.let {
                stickyHeader {
                    Text(text = it)
                }
            }

            items(count) {
                RecipeCard(cardModifier = Modifier.size(200.dp, 150.dp))
            }
        }
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RecipeListPreview() {
    CuccinaTheme {
        RecipeGroupList("Receitas disponíveis", RecyclerView.VERTICAL, 3)
    }
}