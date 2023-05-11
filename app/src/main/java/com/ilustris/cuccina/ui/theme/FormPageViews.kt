@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)

package com.ilustris.cuccina.ui.theme

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.category.ui.component.CategoryIcon
import com.ilustris.cuccina.feature.recipe.domain.ui.CaloriesComponent
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui.IngredientItem
import com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui.IngredientSheet
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step
import com.ilustris.cuccina.feature.recipe.step.presentation.ui.StepItem
import com.ilustris.cuccina.feature.recipe.step.presentation.ui.StepSheet
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch


val columnModifier = Modifier
    .fillMaxSize()
    .padding(16.dp)

@Composable
fun FormPage(formPage: FormPage) {
    val backColor = formPage.backColor ?: MaterialTheme.colorScheme.background
    val textColor = formPage.textColor ?: MaterialTheme.colorScheme.onBackground
    val formText = remember {
        mutableStateOf("")
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = columnModifier.background(backColor)
    ) {

        Text(
            text = formPage.title,
            color = textColor,
            style = MaterialTheme.typography.headlineMedium
        )



        TextField(value = formText.value,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = formPage.description,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light,
                    color = textColor.copy(alpha = 0.5f)
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            onValueChange = {
                formText.value = it
            })

        Button(
            enabled = formText.value.isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                formPage.sendData(formText.value)
            }) {
            Text(text = formPage.actionText, color = textColor)
        }

    }
}

@Composable
fun ImageForm(formPage: FormPage.ImageFormPage) {
    val backColor = formPage.backColor ?: MaterialTheme.colorScheme.background
    val textColor = formPage.textColor ?: MaterialTheme.colorScheme.onBackground

    val selectedImage = remember {
        mutableStateOf("")
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImage.value = it.toString()
        }
    }

    Log.i("ImageForm", "ImageForm: $selectedImage")

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = columnModifier.background(backColor)
    ) {

        Text(
            text = formPage.title,
            color = textColor,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = formPage.description,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )



        GlideImage(
            modifier = Modifier
                .height(250.dp)
                .clip(RoundedCornerShape(defaultRadius))
                .fillMaxWidth()
                .clickable {
                    galleryLauncher.launch("image/*")
                },
            imageModel = { selectedImage.value },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
            ),
            failure = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp)
                        .background(
                            MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                defaultRadius
                            )
                        )
                        .clickable {
                            galleryLauncher.launch("image/*")
                        },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_cherries),
                        contentDescription = "Enviar foto da receita",
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(16.dp)

                    )
                    Text(
                        text = "Enviar foto da receita",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground.copy(
                                alpha = 0.3f
                            ), textAlign = TextAlign.Center
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            },
            previewPlaceholder = R.drawable.ic_cherries
        )

        Button(
            enabled = selectedImage.value != null,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                selectedImage.value.let { formPage.sendData(it) }
            }) {
            Text(text = formPage.actionText, color = textColor)
        }

    }
}

@Composable
fun TimeForm(formPage: FormPage.TimeFormPage) {
    val backColor = formPage.backColor ?: MaterialTheme.colorScheme.background
    val textColor = formPage.textColor ?: MaterialTheme.colorScheme.onBackground

    var hours by remember {
        mutableStateOf(0)
    }

    var minutes by remember {
        mutableStateOf(0)
    }

    fun getTime(hour: Int, minute: Int): Long {
        val hours = hour * 60
        val time = hours + minute
        return time.toLong()
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = columnModifier.background(backColor)

    ) {

        Text(
            text = formPage.title,
            color = textColor,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = formPage.description,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )



        Row(modifier = Modifier.fillMaxWidth()) {
            NumberPicker(
                value = hours,
                label = {
                    val hourFormatted = if (it <= 1) "hora" else "horas"
                    "$it $hourFormatted "
                },
                onValueChange = {
                    hours = it
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.onBackground),
                range = 0..23,
                dividersColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth(0.5f)
            )

            NumberPicker(
                value = minutes,
                label = {
                    val minuteFormatted = if (it <= 1) "minuto" else "minutos"
                    "$it $minuteFormatted"
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    minutes = it
                }, dividersColor = Color.Transparent, range = 0..59
            )
        }


        Button(
            enabled = hours != 0 || minutes != 0,
            modifier = Modifier.fillMaxWidth(0.5f),
            onClick = {
                formPage.sendData(getTime(hours, minutes))
            }) {
            Text(text = formPage.actionText, color = textColor)
        }

    }
}

@Composable
fun CaloriesForm(formPage: FormPage.CaloriesFormPage) {
    val backColor = formPage.backColor ?: MaterialTheme.colorScheme.background
    val textColor = formPage.textColor ?: MaterialTheme.colorScheme.onBackground

    val calories = remember {
        mutableStateOf(0)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = columnModifier.background(backColor)

    ) {

        Text(
            text = formPage.title,
            color = textColor,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = formPage.description,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )



        CaloriesComponent(calories = calories.value, newCalories = {
            calories.value = it
        })


        Button(
            enabled = calories.value != 0,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                formPage.sendData(calories.value)
            }) {
            Text(text = formPage.actionText, color = textColor)
        }

    }
}

@Composable
fun IngredientsForm(formPage: FormPage.IngredientsFormPage) {
    val backColor = formPage.backColor ?: MaterialTheme.colorScheme.background
    val textColor = formPage.textColor ?: MaterialTheme.colorScheme.onBackground
    val scope = rememberCoroutineScope()
    val sheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val ingredients = remember {
        mutableStateListOf<Ingredient>()
    }

    ModalBottomSheetLayout(modifier = Modifier.fillMaxSize(),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetState = sheetState, sheetContent = {
            IngredientSheet(newIngredient = {
                ingredients.add(it)
                scope.launch {
                    sheetState.hide()
                }
            })
        }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = columnModifier.background(backColor)

        ) {
            Text(
                text = formPage.title,
                color = textColor,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = formPage.description,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium
            )


            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(ingredients.size) { index ->
                    ingredients[index].run {
                        IngredientItem(
                            ingredient = this,
                            longPress = { selectedIngredient ->
                                ingredients.remove(selectedIngredient)
                            })
                    }

                }

                item {
                    IconButton(
                        onClick = {
                            scope.launch {
                                Log.i("IngredientForm", "IngredientsForm: showing sheet")
                                sheetState.show()
                            }
                        }, modifier = Modifier
                            .size(64.dp)
                            .background(
                                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                CircleShape
                            )
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
                            contentDescription = "Adicionar ingredientes",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                        )
                    }
                }
            }


            Button(
                enabled = ingredients.isNotEmpty(),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    formPage.sendData(ingredients)
                }) {
                Text(text = formPage.actionText, color = textColor)
            }

        }

    }


}

@Composable
fun StepsForm(formPage: FormPage.StepsFormPage) {
    val backColor = formPage.backColor ?: MaterialTheme.colorScheme.background
    val textColor = formPage.textColor ?: MaterialTheme.colorScheme.onBackground
    val scope = rememberCoroutineScope()
    val sheetState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    )
    val steps = remember {
        mutableStateListOf<Step>()
    }

    ModalBottomSheetLayout(modifier = Modifier.fillMaxSize(),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetState = sheetState,
        sheetContent = {
            StepSheet(savedIngredients = formPage.currentIngredients, newStep = {
                steps.add(it)
                scope.launch {
                    sheetState.hide()
                }
            })
        }) {

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = columnModifier.background(backColor)

        ) {

            Text(
                text = formPage.title,
                color = textColor,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = formPage.description,
                color = textColor,
                style = MaterialTheme.typography.bodyMedium
            )


            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                items(steps.size) { index ->
                    steps[index].run {
                        StepItem(step = this, true) { selectedStep ->
                            steps.add(selectedStep)
                        }
                    }

                }

                item {
                    Button(
                        onClick = {
                            scope.launch { sheetState.show() }
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                RoundedCornerShape(defaultRadius)
                            )
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
                                contentDescription = null,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Text(text = "Adicionar etapa")
                        }

                    }
                }

                item {
                    Button(
                        enabled = steps.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            formPage.sendData(steps)
                        }) {
                        Text(text = formPage.actionText, color = textColor)
                    }
                }
            }


        }

    }


}

@Composable
fun CategoryForm(formPage: FormPage.CategoryFormPage) {
    val backColor = formPage.backColor ?: MaterialTheme.colorScheme.background
    val textColor = formPage.textColor ?: MaterialTheme.colorScheme.onBackground
    val selectedCategory = remember {
        mutableStateOf<Category?>(null)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = columnModifier.background(backColor)

    ) {

        Text(
            text = formPage.title,
            color = textColor,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = formPage.description,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            val categories = Category.values()
            items(categories.size) {
                CategoryIcon(
                    category = categories[it],
                    selectedCategory = selectedCategory.value,
                    categorySelected = { newCategory ->
                        selectedCategory.value = newCategory
                    }
                )
            }
        }

        Button(
            enabled = selectedCategory.value != null,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                selectedCategory.value?.let {
                    formPage.sendData(it.name)
                }
            }) {
            Text(text = formPage.actionText, color = textColor)
        }

    }
}

@Composable
fun PortionsForm(formPage: FormPage.PortionsFormPage) {
    val backColor = formPage.backColor ?: MaterialTheme.colorScheme.background
    val textColor = formPage.textColor ?: MaterialTheme.colorScheme.onBackground
    val portions = remember {
        mutableStateOf("")
    }
    Column(
        modifier = columnModifier.background(backColor),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            text = formPage.title,
            color = textColor,
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = formPage.description,
            color = textColor,
            style = MaterialTheme.typography.bodyMedium
        )


        TextField(
            value = portions.value,
            onValueChange = {
                if (it.isEmpty()) {
                    portions.value = ""
                    return@TextField
                }
                val newPortions = it.filter { char -> char.isDigit() }.toInt()
                if (newPortions <= 100) {
                    portions.value = newPortions.toString()
                }
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            placeholder = {
                Text(
                    text = "Quantas porções?",
                    modifier = Modifier.fillMaxWidth()
                )
            })

        Button(
            enabled = portions.value.isNotEmpty() && portions.value.toInt() > 0,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                formPage.sendData(portions.value.toInt())
            }) {
            Text(text = formPage.actionText, color = textColor)
        }

    }
}

@Composable
fun getFormView(formPage: FormPage) {
    when (formPage) {
        is FormPage.CaloriesFormPage -> CaloriesForm(formPage = formPage)
        is FormPage.CategoryFormPage -> CategoryForm(formPage = formPage)
        is FormPage.DescriptionFormPage -> FormPage(formPage = formPage)
        is FormPage.ImageFormPage -> ImageForm(formPage = formPage)
        is FormPage.IngredientsFormPage -> IngredientsForm(formPage = formPage)
        is FormPage.NameFormPage -> FormPage(formPage = formPage)
        is FormPage.PortionsFormPage -> PortionsForm(formPage = formPage)
        is FormPage.StepsFormPage -> StepsForm(formPage = formPage)
        is FormPage.TimeFormPage -> TimeForm(formPage = formPage)
    }
}

