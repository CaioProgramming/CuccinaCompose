package com.ilustris.cuccina.feature.home.ui.component

import ai.atick.material.MaterialColor
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.ilustris.cuccina.R
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun BannerCard(backgroundImage: String, onClickBanner: () -> Unit) {

    ConstraintLayout(
        modifier = Modifier
            .wrapContentSize()
            .padding(12.dp)
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(defaultRadius))
            .clickable {
                onClickBanner()
            }
    ) {
        val (background, text) = createRefs()

        GlideImage(
            imageModel = { backgroundImage },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
            ), failure = {
                Text(
                    text = "Foto nao encontrada",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialColor.White,
                        shadow = Shadow(
                            color = MaterialColor.Black,
                            offset = Offset(1f, 1f),
                            blurRadius = 1f
                        )
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            },
            previewPlaceholder = R.drawable.ic_cherries,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .blur(1.dp, 1.dp, edgeTreatment = BlurredEdgeTreatment.Rectangle)
                .clip(RoundedCornerShape(defaultRadius))
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = "Vem conferir nossas melhores receitas",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialColor.White,
                shadow = Shadow(
                    color = MaterialColor.Black,
                    offset = Offset(1f, 1f),
                    blurRadius = 1f
                )
            ),
            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
                .fillMaxWidth()
                .padding(16.dp))
    }
}

@Preview
@Composable
fun BannerPreview() {
    CuccinaTheme {
        BannerCard("", {})
    }
}