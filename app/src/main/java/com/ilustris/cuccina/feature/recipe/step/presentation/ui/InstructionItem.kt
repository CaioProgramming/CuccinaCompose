@file:OptIn(ExperimentalMaterial3Api::class)

package com.ilustris.cuccina.feature.recipe.step.presentation.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ilustris.cuccina.R
import com.ilustris.cuccina.ui.theme.defaultRadius

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

        Text(
            text = (count).toString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.Black
            ),

            modifier = Modifier
                .constrainAs(instructionCountField) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(defaultRadius))
                .padding(8.dp)
                .wrapContentSize(
                    Alignment.Center
                )
                .padding(4.dp)


        )
        val instructionValue = remember {
            mutableStateOf(instruction)
        }

        fun enableIcon(instruction: String) = instruction.isNotEmpty()

        TextField(
            value = instructionValue.value,
            enabled = editable,
            label = { if (editable) Text(text = "Escreva a ${count}º instrução") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .constrainAs(instructionText) {
                    start.linkTo(instructionCountField.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(instructionIcon.start)
                }
                .padding(horizontal = 20.dp, vertical = 4.dp),
            onValueChange = {
                instructionValue.value = it
            }, textStyle = MaterialTheme.typography.bodyMedium
        )

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

@Preview
@Composable
fun InstructionItemPreview() {
    InstructionItem(
        instruction = "Frite o bacon",
        count = 1,
        icon = R.drawable.baseline_add_24,
        editable = true,
        iconDescription = "Adicionar instrução"
    ) {

    }
}