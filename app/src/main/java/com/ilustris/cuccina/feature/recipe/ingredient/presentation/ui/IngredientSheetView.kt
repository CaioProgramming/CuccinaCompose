@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.ListItemPicker
import com.chargemap.compose.numberpicker.NumberPicker
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.IngredientMapper
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.IngredientType
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Texture
import com.ilustris.cuccina.ui.theme.defaultRadius

@Composable
fun IngredientSheet(newIngredient: (Ingredient) -> Unit) {


    val ingredientName = remember {
        mutableStateOf("")
    }

    val ingredientType = remember {
        mutableStateOf(IngredientType.KILOGRAMS)
    }

    val quantity = remember {
        mutableStateOf(0)
    }

    fun getRange(texture: Texture): IntRange {
        return when (texture) {
            Texture.LIQUID -> {
                0..1000
            }
            Texture.SOLID -> {
                0..100
            }
        }
    }


    fun getIngredientEmoji(ingredientName: String) =
        IngredientMapper.getIngredientSymbol(ingredientName)

    Log.i(
        "IngredientSheet",
        "IngredientSheet: current ingredient -> ${ingredientName.value}(type: ${ingredientType.value})"
    )

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {

        Text(
            text = "Adicionar ingrediente",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = getIngredientEmoji(ingredientName.value),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .padding(8.dp)
        )

        TextField(
            value = ingredientName.value,
            onValueChange = { ingredientName.value = it },
            label = {
                Text(
                    text = "Nome do ingrediente",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center)
                )
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.W700
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .background(Color.Transparent)
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )

        val quantityFormatted = "${quantity.value} ${ingredientType.value.description}"
        Text(
            text = quantityFormatted,
            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.padding(4.dp)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            NumberPicker(
                range = getRange(ingredientType.value.texture),
                dividersColor = MaterialTheme.colorScheme.primary,
                value = quantity.value,
                onValueChange = {
                    quantity.value = it
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)

            )

            ListItemPicker(
                value = ingredientType.value.abreviation,
                label = { it },
                dividersColor = MaterialTheme.colorScheme.primary,
                onValueChange = {
                    val selectedIngredientType =
                        IngredientType.values().find { type -> type.abreviation == it }!!
                    ingredientType.value = selectedIngredientType
                    if (quantity.value > getRange(ingredientType.value.texture).last) {
                        quantity.value = getRange(ingredientType.value.texture).last
                    }
                    Log.i(
                        "IngredientSheet",
                        "IngredientSheet: selected type: $selectedIngredientType"
                    )
                },
                list = IngredientType.values().toList().map { it.abreviation },
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
            )
        }


        fun enableSaveButton(): Boolean {
            return ingredientName.value.isNotEmpty() && quantity.value > 0
        }

        AnimatedVisibility(
            visible = enableSaveButton(),
            enter = slideInVertically(),
            exit = fadeOut()
        ) {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(defaultRadius),
                onClick = {
                    newIngredient(
                        Ingredient(
                            ingredientName.value,
                            quantity.value,
                            type = ingredientType.value
                        )
                    )
                }) { Text(text = "Salvar ingrediente") }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun IngredientSheetPreview() {
    IngredientSheet(newIngredient = {})
}