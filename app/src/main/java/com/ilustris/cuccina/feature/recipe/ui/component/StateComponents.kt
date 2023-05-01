package com.ilustris.cuccina.feature.recipe.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ilustris.cuccina.R

@Composable
fun StateComponent(
    animation: Int = R.raw.cakerun,
    message: String,
    action: (() -> Unit)? = null,
    buttonText: String? = null
) {
    Column {
        val celebrateComposition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(animation)
        )

        val celebrateProgress by animateLottieCompositionAsState(
            celebrateComposition,
            isPlaying = true,
        )

        LottieAnimation(
            celebrateComposition,
            celebrateProgress,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
        )


        Text(
            text = message,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium
        )

        if (action != null && buttonText != null) {
            Button(onClick = action) {
                Text(text = buttonText)
            }
        }


    }

}


