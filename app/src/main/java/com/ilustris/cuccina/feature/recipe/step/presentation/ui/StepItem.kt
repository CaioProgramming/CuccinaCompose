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
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step

@Composable
fun StepItem(step: Step, canEdit: Boolean = false, updateStep: (Step) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = step.title, style = MaterialTheme.typography.headlineMedium)
        step.instructions.forEachIndexed { index, instruction ->
            InstructionItem(
                instruction = instruction,
                count = (index + 1),
                icon = if (canEdit) R.drawable.iconmonstr_line_one_horizontal_lined else null,
                iconDescription = if (canEdit) "Remover" else "",
                editable = false,
                onSelectIcon = {
                    step.instructions.remove(it)
                    updateStep(step)
                }
            )
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
        )

    }
}