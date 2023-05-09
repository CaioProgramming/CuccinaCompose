@file:OptIn(ExperimentalMaterial3Api::class)

package com.ilustris.cuccina.feature.recipe.step.presentation.ui

import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.R
import com.ilustris.cuccina.ui.theme.defaultRadius

@Composable
fun InstructionItem(
    instruction: String = "",
    savedIngredients: List<String> = emptyList(),
    count: Int,
    editable: Boolean,
    isLastItem: Boolean = false,
    icon: Int? = null,
    iconDescription: String? = "",
    onSelectInstruction: (String) -> Unit,
) {


    fun annotatedIngredient(instruction: String, color: Color) = buildAnnotatedString {
        append(instruction)
        Log.i(
            "InstructionItem",
            "annotatedIngredient: validating ingredients -> $savedIngredients on ($instruction)"
        )
        savedIngredients.forEach { ingredient ->
            if (instruction.contains(ingredient, true)) {
                val startIndex = instruction.indexOf(ingredient)
                val endIndex = instruction.indexOf(ingredient) + ingredient.length
                if (startIndex != -1 && endIndex != instruction.length) {
                    Log.i(javaClass.simpleName, "annotatedIngredient: adding style to $ingredient")
                    addStyle(
                        SpanStyle(
                            color = color,
                            fontWeight = FontWeight.Bold
                        ),
                        start = instruction.indexOf(ingredient),
                        end = (instruction.indexOf(ingredient) + ingredient.length)
                    )
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        val animatedSize by animateDpAsState(
            targetValue = 25.dp,
            animationSpec = tween(2500, easing = FastOutSlowInEasing)
        )


        val dividerModifier = Modifier
            .width(2.dp)
            .height(animatedSize)
            .background(MaterialTheme.colorScheme.onBackground, RoundedCornerShape(defaultRadius))
            .padding(horizontal = 16.dp)
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = (count).toString(),
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.onBackground, RoundedCornerShape(
                            defaultRadius
                        )
                    )
                    .padding(16.dp),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.background,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black
                ),
            )
            if (!isLastItem) {
                Divider(modifier = dividerModifier)
            }
        }

        val instructionValue = remember {
            mutableStateOf(instruction)
        }

        val instructionTextModifier = Modifier.fillMaxWidth()

        if (editable) {
            TextField(
                value = instructionValue.value,
                modifier = instructionTextModifier,
                trailingIcon = {
                    if (instructionValue.value.isNotEmpty()) {
                        icon?.let {
                            IconButton(onClick = {
                                onSelectInstruction(instructionValue.value)
                                instructionValue.value = ""
                            }, content = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = icon),
                                    contentDescription = iconDescription
                                )
                            })
                        }
                    }


                },
                placeholder = {
                    Text(
                        text = "Escreva a ${count}º instrução",
                        modifier = Modifier.wrapContentSize()
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                onValueChange = {
                    instructionValue.value = it
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start
                )
            )
        } else {
            Text(
                text = annotatedIngredient(
                    instructionValue.value,
                    MaterialTheme.colorScheme.primary
                ),
                modifier = instructionTextModifier.clickable {
                    onSelectInstruction(instructionValue.value)
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Start
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InstructionItemPreview() {
    Column {
        listOf(
            "Frite o bacon e reserve em um prato com papel toalha. ",
            "Seque a carne com papel toalha e tempere com sal e pimenta-do-reino. ",
            "Deixe descansar por 10 minutos.",
            "Seque a carne com papel toalha e tempere com sal e pimenta-do-reino. ",
        ).forEachIndexed { index, s ->
            InstructionItem(
                instruction = s,
                savedIngredients = listOf("bacon", "carne"),
                count = index + 1,
                editable = index % 2 == 0,
                isLastItem = index == 3,
                icon = R.drawable.iconmonstr_line_one_horizontal_lined,
            ) {

            }
        }
    }
}
