@file:OptIn(ExperimentalFoundationApi::class)

package com.ilustris.cuccina.feature.profile.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.ui.theme.CuccinaLoader
import com.ilustris.cuccina.ui.theme.Page
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage


@Composable
fun ChefsPageView(page: Page.OtherChefsPage, openChefPage: (String) -> Unit) {

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier
            .background(page.backColor ?: MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize()
    ) {

        item(span = StaggeredGridItemSpan.FullLine) {
            val textColor = page.textColor ?: MaterialTheme.colorScheme.onBackground
            Text(
                text = page.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme
                    .typography
                    .headlineLarge
                    .copy(textAlign = TextAlign.Center, color = textColor)
            )
        }

        items(page.chefs.size) {
            val user = page.chefs[it]
            GlideImage(
                imageModel = { user.photoUrl },
                imageOptions = ImageOptions(
                    alignment = Alignment.Center,
                    "",
                    contentScale = ContentScale.Crop
                ),
                loading = {
                    CuccinaLoader()
                },
                modifier = Modifier
                    .size(170.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .padding(8.dp)
                    .border(
                        width = 3.dp,
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
                    .clickable {
                        openChefPage(user.uid)
                    }
            )
        }

    }
}