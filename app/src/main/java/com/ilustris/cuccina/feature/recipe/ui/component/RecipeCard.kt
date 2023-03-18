package com.ilustris.cuccina.feature.recipe.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.R
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius

@Composable
fun RecipeCard(cardModifier: Modifier) {

    Column(modifier = Modifier.padding(8.dp)) {
        Image(
            painterResource(id = R.drawable.pizza_placeholder),
            contentDescription = "recipe",
            contentScale = ContentScale.Crop,
            modifier = cardModifier.clip(RoundedCornerShape(defaultRadius))
        )
        Text(text = "Pizza calabresa • 20 min • Massas", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(8.dp))

    }
}

@Preview
@Composable
fun recipePreview() {
    CuccinaTheme {
        RecipeCard(cardModifier = Modifier.fillMaxWidth().size(200.dp))
    }
}