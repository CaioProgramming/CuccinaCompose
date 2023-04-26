package com.ilustris.cuccina.feature.recipe.step.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step

@Composable
fun StepItem(step: Step, canEdit: Boolean = false, updateStep: (Step) -> Unit) {
    Column {
        Text(text = step.title, style = MaterialTheme.typography.headlineMedium)
        step.instructions.forEachIndexed { index, instruction ->
            InstructionItem(
                count = index,
                icon = if (canEdit) R.drawable.iconmonstr_line_one_horizontal_lined else null,
                iconDescription = if (canEdit) "Remover" else "",
                editable = false,
                onSelectIcon = {
                    step.instructions.remove(it)
                    updateStep(step)
                }
            )
        }

    }
}