@file:OptIn(
    ExperimentalFoundationApi::class, ExperimentalPagerApi::class,
    ExperimentalPagerApi::class, ExperimentalFoundationApi::class
)

package com.ilustris.cuccina.feature.home.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.pager.ExperimentalPagerApi
import com.ilustris.cuccina.ui.theme.Page
import com.ilustris.cuccina.ui.theme.PageIndicators
import com.ilustris.cuccina.ui.theme.getPageView
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue


@Composable
fun HighLightSheet(
    pages: List<Page>,
    autoSwipe: Boolean = false,
    closeButton: () -> Unit,
    openRecipe: (String) -> Unit,
    openNewRecipe: () -> Unit,
    openChefPage: (String) -> Unit,
) {
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
            getPageView(page = pages[index], openRecipe, openChefPage, openNewRecipe)
        }

        PageIndicators(
            count = pages.size,
            currentPage = pagerState.currentPage,
            enableAutoSwipe = autoSwipe,
            modifier = Modifier
                .constrainAs(indicators) {
                    top.linkTo(closeButton.top)
                    bottom.linkTo(closeButton.bottom)
                    start.linkTo(closeButton.end)
                    end.linkTo(parent.end)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
                .padding(16.dp), onSelectIndicator = {
                scope.launch {
                    pagerState.animateScrollToPage(it)
                }
            }, onFinishPageLoad = {
                scope.launch {
                    if (pagerState.currentPage < pages.size - 1)
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            })


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