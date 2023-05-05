@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalPagerApi::class,
    ExperimentalPagerApi::class
)

package com.ilustris.cuccina.feature.home.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ilustris.cuccina.feature.recipe.start.domain.model.Page
import com.ilustris.cuccina.feature.recipe.start.ui.getPageView
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@Composable
fun HighLightSheet(pages: List<Page>, closeButton: () -> Unit, openRecipe: (String) -> Unit) {
    ConstraintLayout {
        val pagerState = rememberPagerState()
        val scope = rememberCoroutineScope()
        val (pager, indicators, closeButton) = createRefs()

        HorizontalPager(
            pageCount = pages.size,
            state = pagerState,
            modifier = Modifier
                .constrainAs(pager) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxSize()
        ) { index ->
            getPageView(page = pages[index], openRecipe)
        }

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .constrainAs(indicators) {
                    top.linkTo(closeButton.top)
                    bottom.linkTo(closeButton.bottom)
                    start.linkTo(closeButton.end)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .padding(16.dp)
        ) {
            repeat(pages.size) { index ->
                val isCurrentPage = pagerState.currentPage == index
                val color = if (isCurrentPage) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                }
                val weight = if (isCurrentPage) 1f else 0.1f
                Box(
                    modifier = Modifier
                        .padding(1.dp)
                        .animateContentSize(tween(500))
                        .clip(RoundedCornerShape(5.dp))
                        .background(color)
                        .weight(weight)
                        .height(5.dp)
                        .clickable {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                )
            }
        }

        IconButton(modifier = Modifier.constrainAs(closeButton) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
        }, onClick = {
            closeButton()
        }) {
            Icon(
                Icons.Rounded.Close,
                contentDescription = "Fechar",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }

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