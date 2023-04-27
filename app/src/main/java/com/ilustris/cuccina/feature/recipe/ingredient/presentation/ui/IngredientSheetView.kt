@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui

import android.util.Log
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

    fun getRange(texture: Texture): List<Int> {
        val rangeList = ArrayList<Int>()
        val step = when (texture) {
            Texture.LIQUID, Texture.POUND -> 10
            Texture.UNIT -> 1
        }
        val limit = when (texture) {
            Texture.LIQUID, Texture.POUND -> 1000
            Texture.UNIT -> 100
        }
        for (i in 0..limit step step) {
            rangeList.add(i)
        }
        return rangeList

    }


    fun getIngredientEmoji(ingredientName: String) =
        IngredientMapper.getIngredientSymbol(ingredientName)

    Log.i(
        "IngredientSheet",
        "IngredientSheet: current ingredient -> ${ingredientName.value} = $quantity (type: ${ingredientType.value})"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 32.dp)
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
            placeholder = {
                Text(
                    text = "Nome do ingrediente",
                    modifier = Modifier.fillMaxWidth(),
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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            ListItemPicker(
                value = quantity.value,
                onValueChange = {
                    quantity.value = it
                },
                modifier = Modifier.fillMaxWidth(0.5f),
                dividersColor = Color.Transparent,
                list = getRange(ingredientType.value.texture),
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
            )

            ListItemPicker(
                value = ingredientType.value.abreviation,
                modifier = Modifier.fillMaxWidth(),
                label = { it.lowercase() },
                dividersColor = Color.Transparent,
                onValueChange = {
                    val selectedIngredientType =
                        IngredientType.values().find { type -> type.abreviation == it }!!
                    ingredientType.value = selectedIngredientType
                    if (quantity.value > getRange(ingredientType.value.texture).last()) {
                        quantity.value = getRange(ingredientType.value.texture).last()
                    }
                    Log.i(
                        "IngredientSheet",
                        "IngredientSheet: selected type: $selectedIngredientType"
                    )
                },
                list = IngredientType.values().toList().sortedBy { it.description }
                    .map { it.description },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
            )


        }


        fun enableSaveButton(): Boolean {
            return ingredientName.value.isNotEmpty() && quantity.value > 0
        }


        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = enableSaveButton(),
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

@Preview(showBackground = true)
@Composable
fun IngredientSheetPreview() {
    IngredientSheet(newIngredient = {})
}