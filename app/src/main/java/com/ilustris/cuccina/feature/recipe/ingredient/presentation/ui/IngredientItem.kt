package com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.IngredientMapper
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.IngredientType
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius

@Composable
fun IngredientItem(
    ingredient: Ingredient,
    showName: Boolean = true,
    longPress: (Ingredient) -> Unit
) {


    ConstraintLayout(modifier = Modifier.padding(8.dp)) {
        val (emojiText, ingredientText, quantityText) = createRefs()
        val emoji = IngredientMapper.getIngredientSymbol(ingredient.name)
        Text(
            text = emoji,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .constrainAs(emojiText) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), CircleShape)
                .padding(16.dp)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            longPress(ingredient)
                        })
                }
        )

        fun clipIngredientName(ingredientName: String): String {
            return if (ingredientName.length > 15) {
                ingredientName.substring(0, 15) + "..."
            } else {
                ingredientName
            }
        }

        AnimatedVisibility(visible = showName,
            enter = expandVertically(),
            exit = fadeOut(),
            modifier = Modifier.constrainAs(ingredientText) {
                top.linkTo(emojiText.bottom)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
        ) {

            Text(text = clipIngredientName(ingredient.name), modifier = Modifier.padding(8.dp))

        }
        val quantityFormatted = "${ingredient.quantity}${ingredient.type.abreviation}"
        AnimatedVisibility(visible = ingredient.quantity > 0,
            enter = expandVertically(),
            exit = fadeOut(),
            modifier = Modifier
                .constrainAs(quantityText) {
                    end.linkTo(emojiText.end)
                    bottom.linkTo(emojiText.bottom)
                }) {
            Text(
                text = quantityFormatted,
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.background),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(4.dp)
            )
        }

    }

}

@Composable
fun HorizontalIngredientItem(ingredient: Ingredient, showName: Boolean = true) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(defaultRadius))

    ) {
        val (emojiText, name, quantity) = createRefs()
        val emoji = IngredientMapper.getIngredientSymbol(ingredient.name)
        Text(
            text = emoji,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .constrainAs(emojiText) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(16.dp)
        )

        Text(
            text = ingredient.name,
            modifier = Modifier
                .constrainAs(name) {
                    start.linkTo(emojiText.end)
                    end.linkTo(quantity.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
                .padding(16.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        val quantityText =
            if (ingredient.type == IngredientType.TASTE) "" else ingredient.quantity.toString()
        val formattedDescription =
            if (ingredient.quantity > 1) ingredient.type.description.replace("(", "")
                .replace(")", "") else ingredient.type.description.replace("(s)", "")
                .replace("(es)", "")
        val quantityFormatted = "$quantityText $formattedDescription"
        Text(
            text = quantityFormatted,
            modifier = Modifier
                .constrainAs(quantity) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .padding(16.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.End
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IngredientItemPreview() {
    CuccinaTheme {
        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalIngredientItem(
                ingredient = Ingredient(
                    "Alcatra",
                    300,
                    IngredientType.KILOGRAMS
                )
            )
            IngredientItem(Ingredient("Alcatra", 300, IngredientType.KILOGRAMS)) {}

        }

    }
}