@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui

import android.content.Context
import android.os.Vibrator
import android.util.Log
import androidx.compose.animation.*
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
import androidx.compose.ui.platform.LocalContext
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

@OptIn(ExperimentalAnimationApi::class)
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

    fun getRange(texture: Texture): List<Int>? {
        if (texture == Texture.UNDEFINED) return null
        val rangeList = ArrayList<Int>()
        val step = when (texture) {
            Texture.LIQUID, Texture.POUND -> 10
            Texture.UNIT -> 1
            else -> 0
        }
        val limit = when (texture) {
            Texture.LIQUID, Texture.POUND -> 1000
            Texture.UNIT -> 100
            else -> 0
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
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {

        Text(
            text = "Adicionar ingrediente",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 16.dp)
        )


        val targetEmoji = getIngredientEmoji(ingredientName.value)

        AnimatedContent(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            targetState = targetEmoji,
            transitionSpec = {
                EnterTransition.None with ExitTransition.None
            }) {
            Text(
                text = getIngredientEmoji(ingredientName.value),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier
                    .animateEnterExit(enter = scaleIn(), exit = scaleOut())
                    .background(
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .padding(8.dp)
                    .animateContentSize()
            )
        }



        TextField(
            value = ingredientName.value,
            onValueChange = { ingredientName.value = it },
            placeholder = {
                Text(
                    text = "Nome do ingrediente",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center)
                )
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.headlineSmall.copy(
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
            modifier = Modifier.wrapContentSize(),
        ) {

            val range = getRange(ingredientType.value.texture)
            range?.let {
                ListItemPicker(
                    value = quantity.value,
                    onValueChange = {
                        quantity.value = it
                    },
                    modifier = Modifier.fillMaxWidth(0.5f),
                    dividersColor = Color.Transparent,
                    list = range,
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.W800
                    )
                )
            }

            val ingredientTypes = IngredientType.values().toList().sortedBy { it.description }
            val context = LocalContext.current
            ListItemPicker(
                value = ingredientType.value.description,
                modifier = Modifier
                    .animateContentSize()
                    .fillMaxWidth(),
                label = { it.lowercase() },
                dividersColor = Color.Transparent,
                onValueChange = {
                    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(200)
                    val selectedIngredientType =
                        ingredientTypes.find { type -> type.description.equals(it, true) }
                    ingredientType.value = selectedIngredientType ?: ingredientTypes.random()
                    if (quantity.value > (getRange(ingredientType.value.texture)?.last() ?: 0)) {
                        quantity.value = getRange(ingredientType.value.texture)?.last() ?: 0
                    }
                    Log.i(
                        "IngredientSheet",
                        "IngredientSheet: selected type: $selectedIngredientType"
                    )
                    if (selectedIngredientType == IngredientType.TASTE) {
                        quantity.value = 0
                    }
                },
                list = ingredientTypes.map { it.description },
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
            )
        }

    }


    fun enableSaveButton(): Boolean {
        return ingredientName.value.isNotEmpty() && quantity.value > 0 || ingredientType.value == IngredientType.TASTE
    }


    Button(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(16.dp),
        enabled = enableSaveButton(),
        contentPadding = PaddingValues(16.dp),
        shape = RoundedCornerShape(defaultRadius),
        onClick = {
            newIngredient(
                Ingredient(
                    ingredientName.value,
                    quantity.value,
                    type = ingredientType.value
                )
            )
            ingredientName.value = ""
            quantity.value = 0
            ingredientType.value = IngredientType.KILOGRAMS
        }) { Text(text = "Adicionar ingrediente".uppercase()) }

}

@Preview(showBackground = true)
@Composable
fun IngredientSheetPreview() {
    IngredientSheet(newIngredient = {})
}