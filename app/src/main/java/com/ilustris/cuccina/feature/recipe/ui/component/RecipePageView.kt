package com.ilustris.cuccina.feature.recipe.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.category.ui.component.CategoryBadge
import com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui.IngredientItem
import com.ilustris.cuccina.feature.recipe.step.presentation.ui.StepItem
import com.ilustris.cuccina.ui.theme.CuccinaLoader
import com.ilustris.cuccina.ui.theme.Page
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.silent.ilustriscore.core.utilities.DateFormats
import com.silent.ilustriscore.core.utilities.format
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import java.util.*

@Composable
fun RecipePageView(page: Page.RecipePage) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        val recipe = page.recipe
        val formattedDate = Calendar.getInstance().apply {
            timeInMillis = recipe.publishDate
        }.time.format(DateFormats.DD_OF_MM_FROM_YYYY)

        item {

            GlideImage(
                modifier = Modifier
                    .height(250.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(defaultRadius))
                    .fillMaxWidth(),
                imageModel = { recipe.photo },
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
                        Text(
                            text = "Foto não encontrada",
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

            page.user?.let {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    GlideImage(
                        imageModel = { it.photoUrl },
                        imageOptions = ImageOptions(
                            alignment = Alignment.Center,
                            "",
                            contentScale = ContentScale.Crop
                        ),
                        failure = {
                            Image(
                                modifier = Modifier
                                    .size(70.dp)
                                    .clip(CircleShape)
                                    .padding(16.dp)
                                    .border(
                                        2.dp,
                                        MaterialTheme.colorScheme.onBackground,
                                        CircleShape
                                    ),
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_cherries),
                                contentDescription = page.user.name
                            )
                        },
                        loading = {
                            CuccinaLoader()
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface, CircleShape)
                            .border(
                                width = 2.dp,
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.primary,
                                        MaterialTheme.colorScheme.secondary,
                                        MaterialTheme.colorScheme.tertiary
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    Text(
                        text = "${it.name} • $formattedDate",
                        modifier = Modifier.padding(horizontal = 8.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }


            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    val recipeCategory = Category.values().find { it.name == recipe.category }
                    recipeCategory?.let {
                        CategoryBadge(
                            category = it,
                            selectedCategory = recipeCategory,
                            categorySelected = {})
                    }
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .wrapContentSize(align = Alignment.CenterStart)
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                    defaultRadius
                                )
                            )
                            .padding(8.dp)
                    ) {
                        val contentColor = MaterialTheme.colorScheme.onBackground
                        Icon(
                            Icons.Default.Favorite,
                            tint = contentColor,
                            contentDescription = "Favoritos",
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp)
                        )
                        Text(
                            text = ("${recipe.likes.size}").uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                            color = contentColor
                        )
                    }
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .wrapContentSize(align = Alignment.CenterStart)
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                    defaultRadius
                                )
                            )
                            .padding(8.dp)
                    ) {
                        val contentColor = MaterialTheme.colorScheme.onBackground
                        Image(
                            painterResource(id = R.drawable.baseline_local_fire_department_24),
                            colorFilter = ColorFilter.tint(contentColor),
                            contentDescription = "Calorias",
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp)
                        )
                        Text(
                            text = ("${recipe.calories}kcal").uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                            color = contentColor
                        )
                    }
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .wrapContentSize(align = Alignment.CenterStart)
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                    defaultRadius
                                )
                            )
                            .padding(8.dp)
                    ) {
                        val contentColor = MaterialTheme.colorScheme.onBackground
                        Image(
                            painterResource(id = R.drawable.baseline_access_time_24),
                            colorFilter = ColorFilter.tint(contentColor),
                            contentDescription = "Tempo de preparo",
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp)
                        )
                        Text(
                            text = ("${recipe.time} min").uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                            color = contentColor
                        )
                    }
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .wrapContentSize(align = Alignment.CenterStart)
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colorScheme.surface, RoundedCornerShape(
                                    defaultRadius
                                )
                            )
                            .padding(8.dp)
                    ) {
                        val contentColor = MaterialTheme.colorScheme.onBackground
                        Image(
                            painterResource(id = R.drawable.round_restaurant_menu_24),
                            colorFilter = ColorFilter.tint(contentColor),
                            contentDescription = "Porções",
                            modifier = Modifier
                                .size(24.dp)
                                .padding(4.dp)
                        )
                        Text(
                            text = ("${recipe.portions} porções").uppercase(),
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
                            color = contentColor
                        )
                    }
                }
            }



            Text(
                text = recipe.name.capitalize(Locale.current),
                style = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
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
                text = recipe.description,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(defaultRadius)
                    )
                    .padding(16.dp)

            )

        }

        item {
            Text(
                text = "Ingredientes",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            )
            Text(
                text = "Confira os ingredientes para essa receita",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(16.dp)
            )

            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                val ingredients = recipe.ingredients
                items(ingredients.size) { index ->
                    IngredientItem(ingredient = ingredients[index], longPress = { })

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
                    .padding(16.dp)
            )

            Text(
                text = "Estas são as etapa para fazer sua receita, de forma simples e objetiva.",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(16.dp)
            )
        }

        val steps = recipe.steps
        items(steps.size) { index ->
            StepItem(step = steps[index], false) { }
        }

    }
}