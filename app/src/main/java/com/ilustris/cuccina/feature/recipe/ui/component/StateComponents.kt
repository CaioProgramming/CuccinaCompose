package com.ilustris.cuccina.feature.recipe.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                .fillMaxHeight(0.3f)
        )


        Text(
            text = message,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium
        )

        if (action != null && buttonText != null) {
            Button(onClick = action, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                Text(text = buttonText)
            }
        }


    }

}


