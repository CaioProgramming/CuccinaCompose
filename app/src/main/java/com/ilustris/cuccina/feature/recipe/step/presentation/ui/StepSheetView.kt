@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.ilustris.cuccina.feature.recipe.step.presentation.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius


@Composable
fun StepSheet(savedIngredients: List<Ingredient>, newStep: (Step) -> Unit) {

    CuccinaTheme {
        val stepTitle = remember {
            mutableStateOf("")
        }

        val instructions = remember {
            mutableStateListOf<String>()
        }

        LazyColumn(
            modifier = Modifier
                .animateContentSize(tween(500))
                .padding(16.dp)
        ) {
            Log.i("StepSheet", "saved instructions ->  ${instructions.toList()}")

            item {
                Text(
                    text = "Adicionar etapa",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                )

                TextField(
                    value = stepTitle.value,
                    onValueChange = { stepTitle.value = it },
                    label = {
                        Text(
                            text = "Nome da etapa",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                    textStyle = MaterialTheme.typography.headlineMedium.copy(color = MaterialTheme.colorScheme.secondary),
                )

                fun getInstructionCount() = instructions.size + 1

                InstructionItem(
                    count = getInstructionCount(),
                    icon = R.drawable.baseline_add_24,
                    iconDescription = "Adicionar instrução",
                    editable = true
                ) {
                    instructions.add(it)
                }
            }

            items(instructions.size) { index ->
                InstructionItem(
                    instruction = instructions[index],
                    savedIngredients = savedIngredients.map { it.name },
                    count = index + 1,
                    editable = true,
                    icon = R.drawable.iconmonstr_line_one_horizontal_lined,
                    iconDescription = "Remover instrução",
                    onSelectInstruction = {
                        instructions.removeAt(index)
                    })
            }

            item {
                fun enableSave() = stepTitle.value.isNotEmpty() && instructions.isNotEmpty()

                AnimatedVisibility(
                    visible = enableSave(),
                    enter = slideInVertically(),
                    exit = fadeOut()
                ) {
                    Button(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(
                                defaultRadius
                            )
                        ), onClick = {
                        val step = Step(stepTitle.value, ArrayList(instructions.toList()))
                        Log.i("StepSheet", "StepSheet: saving steps -> $step")
                        newStep(Step(stepTitle.value, ArrayList(instructions.toList())))
                        stepTitle.value = ""
                        instructions.clear()
                    }) { Text(text = "Salvar etapa") }
                }
            }
        }
    }


}

@Preview(showBackground = true)
@Composable
fun StepSheetPreview() {
    StepSheet(newStep = {}, savedIngredients = emptyList())
}