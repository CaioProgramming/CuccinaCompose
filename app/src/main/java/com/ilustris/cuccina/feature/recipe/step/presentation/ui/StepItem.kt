package com.ilustris.cuccina.feature.recipe.step.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step

@Composable
fun StepItem(
    step: Step,
    canEdit: Boolean = false,
    ingredients: List<Ingredient> = emptyList(),
    updateStep: (Step) -> Unit
) {


    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = step.title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        step.instructions.forEachIndexed { index, instruction ->
            InstructionItem(
                instruction = instruction,
                savedIngredients = ingredients.map { it.name },
                count = (index + 1),
                icon = if (canEdit) R.drawable.iconmonstr_line_one_horizontal_lined else null,
                iconDescription = if (canEdit) "Remover" else "",
                editable = false,
                isLastItem = index == step.instructions.size - 1,
                onSelectInstruction = {
                    step.instructions.remove(it)
                    updateStep(step)
                }
            )
        }

        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .padding(vertical = 4.dp)
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
        )
    }
}

@Preview
@Composable
fun StepItemPreview() {
    StepItem(step = Step(
        "Preparo da massa",
        ArrayList(listOf("Misture os ingredientes", "Deixe descansar por 30 minutos"))
    ), updateStep = {})
}