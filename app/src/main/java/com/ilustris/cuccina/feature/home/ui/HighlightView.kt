@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalPagerApi::class,
    ExperimentalPagerApi::class
)

package com.ilustris.cuccina.feature.home.ui

import ai.atick.material.MaterialColor
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun HighlightView(recipe: Recipe, graphicsModifier: Modifier) {

    Column(
        modifier = graphicsModifier.padding(16.dp)

    ) {
        Text(
            text = recipe.name,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = recipe.description,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(8.dp)
        )

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HighlightPager(
    recipes: List<Recipe>,
    closeHighlights: () -> Unit,
    openRecipe: (Recipe) -> Unit
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        val (background, indicator, openRecipeButton, pager) = createRefs()

        val currentPhoto = recipes[pagerState.currentPage].photo

        GlideImage(
            imageModel = { currentPhoto },
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
                .pagerFadeTransition(pagerState.currentPage, pagerState)
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize()
        )

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .constrainAs(indicator) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(16.dp)
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            repeat(recipes.size) {
                val color = if (pagerState.currentPage == it) {
                    MaterialTheme.colorScheme.onBackground
                } else {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(color)
                        .weight(1f)
                        .height(5.dp)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(it)
                            }
                        }
                )
            }
        }



        Button(shape = RoundedCornerShape(defaultRadius),
            elevation = ButtonDefaults.buttonElevation(0.dp),
            contentPadding = PaddingValues(16.dp),
            onClick = {
                openRecipe(recipes[pagerState.currentPage])
            },
            modifier = Modifier
                .constrainAs(openRecipeButton) {
                    bottom.linkTo(parent.bottom, 10.dp)
                    start.linkTo(parent.start, 10.dp)
                    end.linkTo(parent.end, 10.dp)
                }
                .padding(16.dp)
                .fillMaxWidth()) {
            Text(text = "Ver receita".uppercase())
        }


        HorizontalPager(
            pageCount = recipes.size,
            state = pagerState,
            modifier = Modifier
                .animateContentSize()
                .constrainAs(pager) {
                    bottom.linkTo(openRecipeButton.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .wrapContentHeight(align = Alignment.Bottom)
        ) { page ->

            HighlightView(recipes[page], Modifier.pagerFadeTransition(page, pagerState))
        }


    }

}

@Preview(showBackground = true)
@Composable
fun PreviewHighlightView() {
    HighlightView(
        recipe = Recipe(
            name = "Bolo de chocolate",
            description = "Bolo de chocolate com cobertura de morango",
            photo = "https://www.receitasnestle.com.br/sites/default/files/styles/recipe_detail_desktop/public/srh_recipes/9f412d6582c2ed971ecfe5295d353b68.webp?itok=2wBERhr6",
            category = Category.CANDY.name
        ),
        graphicsModifier = Modifier.fillMaxSize()
    )
}

// extension method for current page offset
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

fun Modifier.pagerFadeTransition(page: Int, pagerState: PagerState) =
    graphicsLayer {
        val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
        alpha = 1 - pageOffset.absoluteValue
    }