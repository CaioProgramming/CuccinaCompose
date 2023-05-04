@file:OptIn(
    ExperimentalMaterialApi::class
)

package com.ilustris.cuccina.feature.recipe.form.ui

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chargemap.compose.numberpicker.NumberPicker
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.category.ui.component.CategoryBadge
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.domain.ui.CaloriesComponent
import com.ilustris.cuccina.feature.recipe.form.presentation.viewmodel.NewRecipeViewModel
import com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui.IngredientItem
import com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui.IngredientSheet
import com.ilustris.cuccina.feature.recipe.step.presentation.ui.StepItem
import com.ilustris.cuccina.feature.recipe.step.presentation.ui.StepSheet
import com.ilustris.cuccina.feature.recipe.ui.component.StateComponent
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

const val NEW_RECIPE_ROUTE = "new_recipe"

@Composable
fun NewRecipeView(newRecipeViewModel: NewRecipeViewModel? = null) {

    val recipe = newRecipeViewModel?.recipe?.observeAsState()?.value

    val selectedImage = if (recipe?.photo?.isNotEmpty() == true) Uri.parse(recipe.photo) else null

    val ingredients = recipe?.ingredients?.toMutableList()

    val steps = recipe?.steps?.toMutableList()

    val time = recipe?.time?.toInt()

    val portions = recipe?.portions ?: 0

    val calories = recipe?.calories

    val recipeName = recipe?.name ?: ""

    val recipeDescription = recipe?.description ?: ""

    val selectedCategory = Category.values().find { it.name == recipe?.category }

    val formState = newRecipeViewModel?.viewModelState?.observeAsState()?.value

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        newRecipeViewModel?.updateRecipePhoto(it.toString())
    }
    val scope = rememberCoroutineScope()

    val sheetOption = remember {
        mutableStateOf("ingredient")
    }


    val bottomSheetState =
        androidx.compose.material.rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )

    Log.i("NewRecipeView", "NewRecipeView: current recipe $recipe")

    fun getTime(hour: Int, minute: Int): Long {
        val hours = hour * 60
        val time = hours + minute
        return time.toLong()
    }


    ModalBottomSheetLayout(
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            if (sheetOption.value == "ingredient") {
                IngredientSheet {
                    Log.i("NewRecipeView", "NewRecipeView: new ingredient $it")
                    newRecipeViewModel?.updateRecipeIngredients(it)
                    scope.launch {
                        bottomSheetState.hide()
                    }
                }
            } else {
                StepSheet(savedIngredients = ingredients ?: listOf()) {
                Log.i("NewRecipeView", "NewRecipeView: new step $it")
                newRecipeViewModel?.updateRecipeSteps(it)
                scope.launch {
                    bottomSheetState.hide()
                }
            }
        }
    }, sheetState = bottomSheetState) {

        val categories = Category.values().toList().sortedBy { it.description }


        var hours by remember {
            mutableStateOf(0)
        }

        var minutes by remember {
            mutableStateOf(0)
        }

        when (formState) {
            ViewModelBaseState.LoadingState -> {
                StateComponent(R.raw.cakerun, "Salvando receita...")
            }
            is ViewModelBaseState.DataSavedState -> {
                StateComponent(R.raw.cakerun, "Receita salva com sucesso!")
            }
            else -> {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                ) {

                    item {
                        GlideImage(
                            modifier = Modifier
                                .height(250.dp)
                                .fillMaxWidth()
                                .clickable {
                                    galleryLauncher.launch("image/*")
                                },
                            imageModel = { selectedImage },
                            imageOptions = ImageOptions(
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center,
                            ),
                            failure = {
                                Column(
                                    modifier = Modifier
                                        .wrapContentSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Image(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
                                        contentDescription = "Enviar foto da receita",
                                        modifier = Modifier
                                            .wrapContentSize()
                                            .clickable {
                                                galleryLauncher.launch("image/*")
                                            }
                                            .padding(16.dp)
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
                                            .clickable {
                                                galleryLauncher.launch("image/*")
                                            }
                                            .padding(vertical = 8.dp)
                                    )
                                }
                            },
                            previewPlaceholder = R.drawable.ic_cherries
                        )

                        LazyRow {
                            items(categories.size) {
                                CategoryBadge(
                                    category = categories[it],
                                    selectedCategory
                                ) { category ->
                                    newRecipeViewModel?.updateRecipeCategory(category.name)
                                }
                            }
                        }

                        TextField(
                            value = recipeName,
                            onValueChange = {
                                newRecipeViewModel?.updateRecipeName(it)
                            },
                            textStyle = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            modifier = Modifier
                                .background(Color.Transparent)
                                .padding(vertical = 8.dp)
                                .fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "Nome da receita",
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        color = MaterialTheme.colorScheme.onBackground.copy(
                                            alpha = 0.3f
                                        )
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        )

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                        )
                    }

                    item {
                        TextField(
                            value = recipeDescription,
                            onValueChange = {
                                newRecipeViewModel?.updateRecipeDescription(it)
                            },
                            textStyle = MaterialTheme.typography.bodyMedium,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(vertical = 16.dp)
                                .fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = "Descrição da receita",
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        )

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                        )

                    }

                    item {

                        Text(
                            text = "Tempo de preparo",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text = "Coloque o tempo médio que você leva para preparar essa receita.",
                            modifier = Modifier.padding(8.dp),
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

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                        )

                    }

                    item {
                        Text(
                            text = "Porções",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text = "Indique quantas porções essa receita rende.",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
                        )


                        TextField(value = portions.toString(),
                            onValueChange = {
                                if (it.isBlank()) {
                                    newRecipeViewModel?.updatePortions(0)
                                } else {
                                    val newPortions = it.filter { char -> char.isDigit() }.toInt()
                                    if (newPortions <= 100) {
                                        newRecipeViewModel?.updatePortions(newPortions)
                                    }
                                }
                            },
                            leadingIcon = { Text("🍽️") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface),
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

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                        )
                    }

                    item {
                        Text(
                            "Calorias",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.headlineSmall.copy(textAlign = TextAlign.Start)
                        )

                        Text(
                            text = "Informe quantas calorias essa receita possui aproximadamente.",
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
                        )

                        CaloriesComponent(calories = calories ?: 0, newCalories = {
                            newRecipeViewModel?.updateCalories(it)
                        })

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                        )


                    }

                    item {
                        Text(
                            text = "Ingredientes",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = "Coloque os melhores ingredientes para sua receita e deixe ela ainda mais saborosa. ",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(8.dp)
                        )

                        LazyRow(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            items(ingredients?.size ?: 0) { index ->
                                ingredients?.get(index)?.let { ingredient ->
                                    IngredientItem(
                                        ingredient = ingredient,
                                        longPress = { selecteIngredient ->
                                            newRecipeViewModel.removeRecipeIngredient(
                                                selecteIngredient
                                            )
                                        })
                                }

                            }

                            item {
                                IconButton(
                                    onClick = {
                                        sheetOption.value = "ingredient"
                                        scope.launch {
                                            bottomSheetState.show()
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
                                            .fillMaxSize()
                                            .padding(8.dp),
                                        tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                                    )
                                }
                            }
                        }

                        Divider(
                            Modifier
                                .height(1.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                        )

                    }

                    item {
                        Text(
                            text = "Passo a Passo",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        )

                        Text(
                            text = "Coloque cada etapa para fazer sua receita, de forma simples e objetiva.",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(8.dp)
                        )
                    }

                    items(steps?.size ?: 0) { index ->
                        steps?.get(index)?.let { step ->
                            StepItem(step = step, true) { selectedStep ->
                                newRecipeViewModel.updateRecipeStep(selectedStep)
                            }
                        }

                    }

                    val buttonModifier = Modifier
                        .fillMaxWidth()
                    val buttonShape = RoundedCornerShape(0.dp)
                    val buttonPaddingValues = PaddingValues(16.dp)
                    val buttonHorizontalArrangement = Arrangement.Start
                    val buttonVerticalAlignment = Alignment.CenterVertically

                    item {

                        Button(
                            onClick = {
                                sheetOption.value = "step"
                                scope.launch { bottomSheetState.show() }
                            },
                            shape = buttonShape,
                            modifier = buttonModifier,
                            contentPadding = buttonPaddingValues,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Row(
                                modifier = buttonModifier,
                                horizontalArrangement = buttonHorizontalArrangement,
                                verticalAlignment = buttonVerticalAlignment
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
                                    contentDescription = null,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                Text(text = "Adicionar etapa")
                            }

                        }

                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))
                        )
                    }

                    fun enableButton(recipe: Recipe?): Boolean {
                        return recipe != null &&
                                recipe.name.isNotBlank() &&
                                recipe.description.isNotBlank() &&
                                recipe.ingredients.isNotEmpty() &&
                                recipe.steps.isNotEmpty() &&
                                recipe.calories > 0 &&
                                recipe.portions > 0

                    }

                    item {
                        AnimatedVisibility(
                            visible = enableButton(recipe),
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Button(
                                onClick = {
                                    newRecipeViewModel?.saveRecipe(
                                        getTime(
                                            hours,
                                            minutes
                                        )
                                    )
                                },
                                contentPadding = buttonPaddingValues,
                                shape = buttonShape,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = buttonModifier.padding(horizontal = 10.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(text = "Publicar")
                                }
                            }
                        }
                    }

                }
            }
        }


    }


}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NewRecipeFormPreview() {
    CuccinaTheme {
        NewRecipeView()
    }
}