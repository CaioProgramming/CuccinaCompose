@file:OptIn(ExperimentalFoundationApi::class)

package com.ilustris.cuccina.feature.profile.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.ui.theme.CuccinaLoader
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun ProfileView() {

    LazyColumn(modifier = Modifier.padding(16.dp)) {


        item {
            GlideImage(
                imageModel = { /*TODO*/ },
                imageOptions = ImageOptions(
                    alignment = Alignment.Center,
                    "",
                    contentScale = ContentScale.Fit,
                    requestSize = IntSize(150, 150)
                ),
                loading = {
                    CuccinaLoader()
                },
                modifier = Modifier
                    .size(150.dp)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(2.dp, MaterialTheme.colorScheme.primary)
                    .clip(CircleShape)
            )
        }

        stickyHeader {
            Text("username", style = MaterialTheme.typography.headlineMedium)
        }

        item {
            Row(horizontalArrangement = Arrangement.Center) {
                Column {
                    Text(text = "15")
                    Text(text = "Receitas")
                }
                Column {
                    Text(text = "15")
                    Text(text = "Curtidas")
                }
            }
        }

        item {
            Text(text = "Receitas", style = MaterialTheme.typography.headlineSmall)
            Divider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            )
        }

        items(5) {

        }

        item {
            Text(text = "Curtidas", style = MaterialTheme.typography.headlineSmall)
            Divider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            )
        }

    }

}