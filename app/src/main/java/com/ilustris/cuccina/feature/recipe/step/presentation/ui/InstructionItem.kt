@file:OptIn(ExperimentalMaterial3Api::class)

package com.ilustris.cuccina.feature.recipe.step.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ilustris.cuccina.R

@Composable
fun InstructionItem(
    instruction: String = "",
    count: Int,
    icon: Int? = null,
    iconDescription: String,
    editable: Boolean,
    onSelectIcon: (String) -> Unit
) {

    ConstraintLayout {
        val (instructionCountField, instructionText, instructionIcon) = createRefs()
        val instructionValue = remember {
            mutableStateOf(instruction)
        }
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .constrainAs(instructionText) {
                    start.linkTo(instructionCountField.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(if (icon != null) instructionIcon.start else parent.end)
                }
                .padding(16.dp)
                .fillMaxWidth()) {


            Text(
                text = (count).toString(),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
                style = MaterialTheme.typography.headlineSmall.copy(
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black
                ),
            )
            TextField(
                value = instructionValue.value,
                enabled = editable,
                label = {
                    if (editable) Text(
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
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
            )
        }



        fun enableIcon(instruction: String) = instruction.isNotEmpty()

        AnimatedVisibility(
            visible = enableIcon(instructionValue.value),
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.constrainAs(instructionIcon) {
                end.linkTo(parent.end)
                top.linkTo(instructionText.top)
                bottom.linkTo(instructionText.bottom)
            }
        ) {
            icon?.let {
                Icon(
                    imageVector = ImageVector.vectorResource(id = icon),
                    contentDescription = iconDescription,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onSelectIcon(instructionValue.value)
                            instructionValue.value = ""
                        }
                )
            }

        }


    }


}

@Preview(showBackground = true)
@Composable
fun InstructionItemPreview() {
    InstructionItem(
        instruction = "Frite o bacon e reserve em um prato com papel toalha. ",
        count = 1,
        icon = R.drawable.baseline_add_24,
        editable = true,
        iconDescription = "Adicionar instrução"
    ) {

    }
}