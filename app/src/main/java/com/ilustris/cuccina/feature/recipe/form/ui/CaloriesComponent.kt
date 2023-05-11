package com.ilustris.cuccina.feature.recipe.domain.ui

import ai.atick.material.MaterialColor
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun CaloriesComponent(calories: Int? = null, newCalories: (Int) -> Unit) {

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically)
            .padding(8.dp)
    ) {

        val (caloriesText, caloriesBar) = createRefs()
        val calorieInput = remember { mutableStateOf("") }


        fun getProgress(): Float {
            val maxCalories = 1000
            val calory = calories ?: 0
            return calory.toFloat() / maxCalories.toFloat()
        }

        @Composable
        fun progressColor() =
            when (calories) {
                in 1..250 -> MaterialColor.Blue500
                in 251..500 -> MaterialColor.Green500
                in 501..750 -> MaterialColor.Yellow500
                in 751..1000 -> MaterialColor.Red500
                else -> MaterialTheme.colorScheme.onBackground
            }


        val progressAnimation by animateFloatAsState(
            targetValue = getProgress(),
            animationSpec = tween(1000, easing = FastOutLinearInEasing)
        )

        LaunchedEffect(Unit) {
            calories?.let {
                calorieInput.value = it.toString()
            }
        }

        CircularProgressIndicator(progress = progressAnimation,
            color = progressColor(),
            strokeWidth = 5.dp,
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .constrainAs(caloriesBar) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize(0.6f)
        )

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .constrainAs(caloriesText) {
                    top.linkTo(caloriesBar.top)
                    bottom.linkTo(caloriesBar.bottom)
                    end.linkTo(caloriesBar.end)
                    start.linkTo(caloriesBar.start)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .padding(8.dp)) {

            Text(
                text = "🔥",
                style = MaterialTheme.typography.displaySmall.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Normal
                ),
            )

            TextField(
                value = calories?.toString() ?: "",
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "0",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                            fontWeight = FontWeight.Normal
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    textAlign = TextAlign.Center,
                    color = progressColor(),
                    fontWeight = FontWeight.Black
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = {
                    if (calorieInput.value.isNotEmpty() && calorieInput.value.isNotBlank()) {
                        newCalories(calorieInput.value.toInt())
                    }

                }),
                onValueChange = { newValue ->
                    if (newValue.isEmpty() || newValue.isBlank()) {
                        newCalories(0)
                    } else {
                        val calorie = newValue.filter { it.isDigit() }.toIntOrNull()
                        calorie?.let {
                            if (it <= 1000) {
                                newCalories(it)
                            }
                        }
                    }


                })

            Text(
                text = "/1000kcal",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            )

        }
    }

}

@Preview(showBackground = true)
@Composable
fun CaloriesComponentPreview() {
    CaloriesComponent(calories = 700, newCalories = {})
}