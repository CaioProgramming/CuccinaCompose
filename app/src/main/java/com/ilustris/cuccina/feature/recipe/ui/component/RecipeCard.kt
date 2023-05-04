@file:OptIn(ExperimentalAnimationApi::class)

package com.ilustris.cuccina.feature.recipe.ui.component

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.silent.ilustriscore.core.utilities.delayedFunction
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState

@Composable
fun RecipeCard(modifier: Modifier, recipe: Recipe, onClickRecipe: (Recipe) -> Unit) {

    var visibility by remember {
        mutableStateOf(true)
    }

    AnimatedVisibility(
        visible = visibility,
        enter = scaleIn() + fadeIn(),
        exit = fadeOut() + scaleOut()
    ) {
        Column(modifier = Modifier
            .padding(8.dp)
            .clickable {
                onClickRecipe(recipe)
            }) {

            GlideImage(
                imageModel = { recipe.photo },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                ), failure = {
                    Text(
                        text = "Foto nao encontrada",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(8.dp)
                    )
                },
                onImageStateChanged = {
                    if (it is GlideImageState.Success) {
                        visibility = true
                    } else if (it is GlideImageState.Failure) {
                        visibility = false
                        Log.e(
                            javaClass.simpleName,
                            "RecipeCard: Error loading image ${it.reason?.message}",
                        )
                    }
                },
                previewPlaceholder = R.drawable.ic_cherries,
                modifier = modifier
                    .clip(RoundedCornerShape(defaultRadius))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                        RoundedCornerShape(defaultRadius)
                    )
            )
            val category = Category.values().find { it.name == recipe.category } ?: Category.UNKNOW
            Text(
                text = "${recipe.name} • ${recipe.time.toInt()} min • ${category.title.lowercase()}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .width(300.dp)
                    .padding(8.dp)
            )

        }
    }

    LaunchedEffect(Unit) {
        delayedFunction(500) {
            visibility = true
        }
    }

}

@Preview
@Composable
fun recipePreview() {
    CuccinaTheme {
        RecipeCard(
            modifier = Modifier
                .fillMaxWidth()
                .size(200.dp), recipe = Recipe(
                name = "Bolo de Cenoura",
                photo = "https://www.receitasnestle.com.br/images/default-source/recipes/bolo-de-cenoura-com-cobertura-de-chocolate.jpg",
                description = "Bolo de cenoura com cobertura de chocolate",
                time = 60,
                portions = 8,
                author = "Silent",
                ingredients = emptyList(),
                category = Category.CANDY.name
            ), onClickRecipe = { }
        )
    }
}