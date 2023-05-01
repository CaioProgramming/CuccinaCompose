package com.ilustris.cuccina.feature.recipe.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun RecipeCard(modifier: Modifier, recipe: Recipe) {

    Column(modifier = Modifier.padding(8.dp)) {

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
            previewPlaceholder = R.drawable.ic_cherries,
            modifier = modifier.clip(RoundedCornerShape(defaultRadius))
        )
        val category = Category.values().find { it.name == recipe.category } ?: Category.UNKNOW
        Text(
            text = "${recipe.name} • ${recipe.time.toInt()} min • ${category.title.lowercase()}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )

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
            )
        )
    }
}