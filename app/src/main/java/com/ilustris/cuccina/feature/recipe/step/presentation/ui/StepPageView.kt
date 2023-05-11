package com.ilustris.cuccina.feature.recipe.step.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.ui.theme.Page

@Composable
fun StepPageView(page: Page.StepPage) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        item {
            Text(
                text = page.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Center)
            )
            Text(
                text = page.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                style = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center)
            )
        }

        val instructions = page.step.instructions
        items(instructions.size) { index ->
            InstructionItem(
                instruction = instructions[index],
                savedIngredients = page.ingredients.map { it.lowercase() },
                count = index + 1,
                editable = false,
                onSelectInstruction = {},
                isLastItem = index == instructions.lastIndex
            )
        }

    }
}