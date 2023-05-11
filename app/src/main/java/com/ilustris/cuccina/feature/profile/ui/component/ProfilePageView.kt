package com.ilustris.cuccina.feature.profile.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ilustris.cuccina.ui.theme.CuccinaLoader
import com.ilustris.cuccina.ui.theme.Page
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

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