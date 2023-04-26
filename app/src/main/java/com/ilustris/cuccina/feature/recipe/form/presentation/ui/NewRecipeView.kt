@file:OptIn(
    ExperimentalCoilApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class
)

package com.ilustris.cuccina.feature.recipe.form.presentation.ui

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.category.ui.component.CategoryBadge
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.feature.recipe.form.presentation.viewmodel.NewRecipeViewModel
import com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui.IngredientItem
import com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui.IngredientSheet
import com.ilustris.cuccina.feature.recipe.step.presentation.ui.StepItem
import com.ilustris.cuccina.feature.recipe.step.presentation.ui.StepSheet
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius
import kotlinx.coroutines.launch

const val NEW_RECIPE_ROUTE = "NEW_RECIPE_ROUTE"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecipeView(newRecipeViewModel: NewRecipeViewModel? = null) {

    val recipe = newRecipeViewModel?.recipe?.observeAsState()?.value

    val selectedImage = recipe?.photo?.let { Uri.parse(it) }

    val ingredients = recipe?.ingredients?.toMutableList()

    val steps = recipe?.steps?.toMutableList()

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        newRecipeViewModel?.updateRecipePhoto(it.toString())
    }
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    val sheetOption = remember {
        mutableStateOf("ingredient")
    }


    val bottomSheetState =
        androidx.compose.material.rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)


    ModalBottomSheetLayout(sheetContent = {
        if (sheetOption.value == "ingredient") {
            IngredientSheet {
                Log.i("NewRecipeView", "NewRecipeView: new ingredient $it")
                newRecipeViewModel?.updateRecipeIngredients(it)
                scope.launch {
                    bottomSheetState.hide()
                }
            }
        } else {
            StepSheet {
                newRecipeViewModel?.updateRecipeSteps(it)
                scope.launch {
                    bottomSheetState.hide()
                }
            }
        }
    }, sheetState = bottomSheetState) {

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item {
                val categories = Category.values().toList().sortedBy { it.description }

                var recipeName by remember {
                    mutableStateOf("")
                }
                var recipeDescription by remember {
                    mutableStateOf("")
                }
                var selectedCategory: Category by remember {
                    mutableStateOf(categories.first())
                }
                val painter =
                    if (selectedImage != null) rememberImagePainter(selectedImage) else painterResource(
                        id = R.drawable.cherry
                    )

                Image(
                    painter,
                    contentDescription = "Enviar foto de receita",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(defaultRadius))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                            RoundedCornerShape(defaultRadius)
                        )
                        .clickable {
                            galleryLauncher.launch("image/*")
                        }
                )

                Text(
                    text = "Adicione uma foto da receita",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow {
                    items(categories.size) {
                        CategoryBadge(category = categories[it], selectedCategory) { category ->
                            selectedCategory = category
                        }
                    }
                }

                TextField(
                    value = recipeName,
                    onValueChange = {
                        recipeName = it
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
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )

                TextField(
                    value = recipeDescription,
                    onValueChange = {
                        recipeDescription = it
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
                        .background(Color.Transparent)
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                            RoundedCornerShape(
                                defaultRadius
                            )
                        )
                        .clip(RoundedCornerShape(defaultRadius))
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Descrição da receita",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
            }

            item {
                Text(
                    text = "Ingredientes",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                )
                Text(
                    text = "Coloque todos os ingredientes que você utiliza na sua receita(até os secretos :P).",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyRow {

                    item {

                        IconButton(
                            onClick = {
                                sheetOption.value = "ingredient"
                                scope.launch {
                                    bottomSheetState.show()
                                }
                            }, modifier = Modifier
                                .size(64.dp)
                                .padding(4.dp)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                                    shape = CircleShape
                                )
                                .clip(CircleShape)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
                                contentDescription = "Adicionar ingredientes",
                                modifier = Modifier.fillMaxSize(),
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                            )
                        }
                    }

                    items(ingredients?.size ?: 0) { index ->
                        ingredients?.get(index)?.let { ingredient ->
                            IngredientItem(
                                ingredient = ingredient,
                                longPress = { selecteIngredient ->
                                    newRecipeViewModel.removeRecipeIngredient(selecteIngredient)
                                })
                        }

                    }


                }
            }

            item {
                Text(
                    text = "Passo a Passo",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Coloque cada etapa para fazer sua receita, de forma simples e objetiva.",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(vertical = 8.dp)
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
                .padding(vertical = 10.dp)
            val buttonShape = RoundedCornerShape(0.dp)
            val buttonPaddingValues = PaddingValues(vertical = 4.dp)
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
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                    colors =
                    ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
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
            }

            fun enableButton(recipe: Recipe?) =
                recipe != null && recipe.name.isNotBlank() && recipe.description.isNotBlank() && recipe.ingredients.isNotEmpty() && recipe.steps.isNotEmpty()

            item {
                AnimatedVisibility(
                    visible = enableButton(recipe),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Button(
                        onClick = { newRecipeViewModel?.saveRecipe() },
                        contentPadding = buttonPaddingValues,
                        shape = buttonShape,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = buttonModifier.padding(horizontal = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Confirmar")
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