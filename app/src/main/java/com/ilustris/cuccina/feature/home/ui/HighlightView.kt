@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalPagerApi::class,
    ExperimentalPagerApi::class
)

package com.ilustris.cuccina.feature.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.category.domain.mapper.CategoryMapper
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.feature.recipe.domain.model.Recipe
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlin.math.absoluteValue

@Composable
fun HighlightView(recipe: Recipe, graphicsModifier: Modifier) {

    val category = CategoryMapper.findCategory(recipe.category)
    val backColor = CategoryMapper.getCategoryColor(category)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = graphicsModifier
            .background(backColor)
            .padding(16.dp)

    ) {
        GlideImage(
            imageModel = { recipe.photo },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
            ), failure = {
                Text(
                    text = "Foto nao encontrada",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
            },
            previewPlaceholder = R.drawable.ic_cherries,
            modifier = Modifier
                .size(150.dp)
                .border(
                    3.dp,
                    MaterialTheme.colorScheme.onSurface,
                    CircleShape
                )
                .clip(CircleShape)
        )

        Text(
            text = recipe.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(8.dp)
        )

        Text(
            text = recipe.description,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
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

    ConstraintLayout {
        val (pager, indicator, closeButton) = createRefs()


        HorizontalPager(
            pageCount = recipes.size,
            modifier = Modifier
                .constrainAs(pager) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)

                }
                .fillMaxSize(),
            state = pagerState
        ) { page ->
            val graphicsModifier = Modifier
                .graphicsLayer {
                    val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
                    // translate the contents by the size of the page, to prevent the pages from sliding in from left or right and stays in the center
                    translationX = pageOffset * size.width
                    // apply an alpha to fade the current page in and the old page out
                    alpha = 1 - pageOffset.absoluteValue
                }
                .fillMaxSize()
            HighlightView(recipes[page], graphicsModifier)
        }

        IconButton(onClick = {
            closeHighlights()
        }, modifier = Modifier
            .constrainAs(closeButton) {
                top.linkTo(parent.top, margin = 10.dp)
                start.linkTo(parent.end, margin = 10.dp)
            }
            .size(64.dp)) {
            Icon(
                Icons.Default.Close,
                contentDescription = "fechar",
                modifier = Modifier
                    .padding(8.dp)
                    .border(
                        width = 1.dp,
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.onSurface
                    )
            )
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