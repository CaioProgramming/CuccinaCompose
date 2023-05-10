@file:OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)

package com.ilustris.cuccina.feature.recipe.start.ui

import ai.atick.material.MaterialColor
import android.util.Log
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.category.ui.component.CategoryBadge
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.Ingredient
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.IngredientMapper
import com.ilustris.cuccina.feature.recipe.ingredient.domain.model.IngredientType
import com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui.HorizontalIngredientItem
import com.ilustris.cuccina.feature.recipe.ingredient.presentation.ui.IngredientItem
import com.ilustris.cuccina.feature.recipe.start.domain.model.Page
import com.ilustris.cuccina.feature.recipe.step.domain.model.Step
import com.ilustris.cuccina.feature.recipe.step.presentation.ui.InstructionItem
import com.ilustris.cuccina.feature.recipe.step.presentation.ui.StepItem
import com.ilustris.cuccina.feature.recipe.ui.component.RecipeCard
import com.ilustris.cuccina.ui.theme.CuccinaLoader
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

fun annotatedPage(text: String, annotations: List<String>, color: Color) = buildAnnotatedString {
    append(text)
    Log.i("PageAnnotation", "annotatedPage: validating annotations -> $annotations on ($text)")
    annotations.forEach { annotation ->
        if (text.contains(annotation, true)) {
            val startIndex = text.indexOf(annotation)
            val endIndex = text.indexOf(annotation) + annotation.length
            if (startIndex != -1 && endIndex != text.length) {
                Log.i(javaClass.simpleName, "annotation: adding style to $annotation")
                addStyle(
                    SpanStyle(
                        color = color,
                        fontWeight = FontWeight.Bold
                    ),
                    start = text.indexOf(annotation),
                    end = (text.indexOf(annotation) + annotation.length)
                )
            }
        }
    }
}

@Composable
fun SimplePageView(page: Page.SimplePage) {

    Column(
        modifier = Modifier
            .background(page.backColor ?: MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize(), verticalArrangement = Arrangement.Center
    ) {
        val textColor = page.textColor ?: MaterialTheme.colorScheme.onBackground
        Text(
            text = page.title,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme
                .typography
                .headlineLarge
                .copy(textAlign = TextAlign.Center, color = textColor)
        )
        Text(
            text = annotatedPage(
                page.description,
                page.annotatedTexts,
                MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            style = MaterialTheme
                .typography
                .bodyMedium
                .copy(textAlign = TextAlign.Center, color = textColor)
        )
    }
}

@Composable
fun AnimatedTextPage(page: Page.AnimatedTextPage) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(page.backColor ?: MaterialTheme.colorScheme.background)
    ) {
        var texts = page.texts.toString().replace("[", "").replace("]", "").replace(",", "")
        val textColor = page.textColor ?: MaterialTheme.colorScheme.onBackground

        repeat(3 * page.texts.size) {
            texts += texts
        }

        Text(
            texts,
            letterSpacing = 3.sp,
            lineHeight = 50.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .graphicsLayer {
                    val scale = 2.5f
                    scaleX = scale
                    scaleY = scale
                }
                .fillMaxSize()
        )



        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = page.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme
                    .typography
                    .headlineLarge
                    .copy(
                        textAlign = TextAlign.Center,
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = MaterialColor.Black,
                            offset = Offset(1f, 1f),
                            blurRadius = 1.3f
                        )
                    )
            )
            Text(
                text = page.description,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme
                    .typography
                    .bodyMedium
                    .copy(
                        textAlign = TextAlign.Center,
                        shadow = Shadow(
                            color = MaterialColor.Black,
                            offset = Offset(1f, 1f),
                            blurRadius = 1.3f
                        ),
                        color = textColor
                    )
            )

        }


    }
}

@Composable
fun HighLightPage(page: Page.HighlightPage, openRecipe: (String) -> Unit) {

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (background, info, button) = createRefs()

        GlideImage(
            imageModel = { page.backgroundImage },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                colorFilter = ColorFilter.tint(
                    MaterialColor.Black.copy(alpha = 0.3f),
                    BlendMode.SrcAtop
                ),
            ), failure = {
                Text(
                    text = "Foto nao encontrada",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            },
            previewPlaceholder = R.drawable.ic_cherries,
            modifier = Modifier
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize()
        )

        Column(modifier = Modifier
            .constrainAs(info) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
                height = Dimension.fillToConstraints
            }
            .fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(
                text = page.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialColor.White
                )
            )
            Text(
                text = page.description,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium.copy(
                    textAlign = TextAlign.Center,
                    color = MaterialColor.White
                )
            )
        }

        Button(shape = RoundedCornerShape(defaultRadius),
            elevation = ButtonDefaults.buttonElevation(0.dp),
            contentPadding = PaddingValues(16.dp),
            onClick = {
                openRecipe(page.recipeId)
            },
            modifier = Modifier
                .constrainAs(button) {
                    bottom.linkTo(parent.bottom, 10.dp)
                    start.linkTo(parent.start, 10.dp)
                    end.linkTo(parent.end, 10.dp)
                }
                .padding(16.dp)
                .fillMaxWidth()) {
            Text(text = "Ver Receita", fontWeight = FontWeight.Bold)
        }


    }

}

@Composable
fun RecipePageView(page: Page.RecipePage) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        val recipe = page.recipe

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

@Composable
fun StepsPageView(page: Page.StepsPage) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        item {
            Text(
                text = page.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Center)
            )
            Text(
                text = page.description,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center)
            )
        }

        items(page.steps.size) { index ->
            StepItem(
                step = page.steps[index],
                canEdit = false,
                ingredients = emptyList(),
                updateStep = {})
        }

    }
}

@Composable
fun IngredientsPageView(page: Page.IngredientsPage) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        item {
            Text(
                text = page.title,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge.copy(textAlign = TextAlign.Center)
            )
            Text(
                text = page.description,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelMedium.copy(textAlign = TextAlign.Center)
            )
        }

        items(page.ingredients.size) { index ->
            HorizontalIngredientItem(ingredient = page.ingredients[index])
        }

    }
}

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

@Composable
fun ProfilePageView(page: Page.ProfilePage) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                page.backColor ?: MaterialTheme.colorScheme.background,
                RoundedCornerShape(
                    topStart = 0.dp,
                    topEnd = 0.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .padding(16.dp)
    ) {
        val (profilePic, username) = createRefs()
        val user = page.userModel
        GlideImage(
            imageModel = { user.photoUrl },
            imageOptions = ImageOptions(
                alignment = Alignment.Center,
                "",
                contentScale = ContentScale.Fit
            ),
            loading = {
                CuccinaLoader()
            },
            modifier = Modifier
                .constrainAs(profilePic) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .size(150.dp)
                .clip(CircleShape)
                .padding(16.dp)
                .border(
                    width = 5.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    ),
                    shape = CircleShape
                )
                .clip(CircleShape)
        )

        Text(
            user.name,
            style = MaterialTheme.typography.headlineMedium.copy(
                color = page.textColor ?: MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Black
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(username) {
                    top.linkTo(profilePic.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
        )

    }
}

@Composable
fun RecipesPageView(page: Page.RecipeListPage, openRecipe: (String) -> Unit) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Text(
                text = page.title,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item(span = StaggeredGridItemSpan.FullLine) {
            Text(
                text = page.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(page.recipes.size) { index ->
            RecipeCard(
                recipe = page.recipes[index],
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) { recipe ->
                openRecipe(recipe.id)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PagePreview() {
    CuccinaTheme {
        val pages = listOf(
            Page.SimplePage("Vamos cozinhar?", "Prepare sua cozinha e vamos começar!"),
            Page.IngredientsPage(
                "Ingredientes",
                "estes são os ingredientes ncessários para sua receita ficar perfeita",
                listOf(Ingredient("Contra filé", 500, type = IngredientType.POUNDS))
            ),
            Page.StepsPage(
                "Passo a passo?", "Estas são as etapas para fazer sua receita",
                listOf(Step("Passo 1", ArrayList(listOf("Instrução 1", "Instrução 2"))))
            ),
            Page.StepPage(
                "Passo 1",
                "Estas são as etapas para fazer sua receita",
                Step("Passo 1", ArrayList(listOf("Instrução 1", "Instrução 2"))),
                emptyList()
            ),
            Page.AnimatedTextPage(
                "Vamos cozinhar?",
                description = "Prepare sua cozinha e vamos começar!",
                IngredientMapper.emojiList().take(3)
            )
        )
        getPageView(pages.last()) {

        }
    }

}

@Composable
fun PageIndicators(
    count: Int,
    currentPage: Int,
    modifier: Modifier,
    enableAutoSwipe: Boolean = false,
    onFinishPageLoad: (Int) -> Unit,
    onSelectIndicator: (Int) -> Unit
) {


    fun isCurrentPage(index: Int) = currentPage == index

    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(count) { index ->

            val delay = if (!isCurrentPage(index)) 500 else 10000
            var progressTarget by remember {
                mutableStateOf(0f)
            }

            val animateProgress by animateFloatAsState(
                targetValue = progressTarget,
                animationSpec = tween(
                    durationMillis = delay,
                    delayMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            )

            val weight = 1f / count

            progressTarget = if (isCurrentPage(index)) {
                1f
            } else {
                0f
            }

            LinearProgressIndicator(
                trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                color = MaterialTheme.colorScheme.secondary,
                strokeCap = StrokeCap.Round,
                progress = animateProgress,
                modifier = Modifier
                    .padding(1.dp)
                    .clip(RoundedCornerShape(5.dp))
                    .weight(weight)
                    .height(5.dp)
                    .clickable {
                        onSelectIndicator(index)
                    }
            )

            LaunchedEffect(animateProgress) {
                snapshotFlow { animateProgress }.collect { progress ->
                    if (progress == 1f && enableAutoSwipe) {
                        onFinishPageLoad(index)
                    }
                }
            }
        }
    }
}


@Composable
fun getPageView(page: Page, openRecipe: (String) -> Unit) {
    return when (page) {
        is Page.SimplePage -> SimplePageView(page)
        is Page.StepsPage -> StepsPageView(page)
        is Page.IngredientsPage -> IngredientsPageView(page)
        is Page.StepPage -> StepPageView(page)
        is Page.RecipePage -> RecipePageView(page)
        is Page.HighlightPage -> HighLightPage(page = page, openRecipe = openRecipe)
        is Page.AnimatedTextPage -> AnimatedTextPage(page = page)
        is Page.ProfilePage -> ProfilePageView(page = page)
        is Page.RecipeListPage -> RecipesPageView(page = page, openRecipe = openRecipe)
    }
}


