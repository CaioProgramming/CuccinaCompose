package com.ilustris.cuccina.ui.theme

import ai.atick.material.MaterialColor
import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.ui.component.StateComponent
import com.silent.ilustriscore.core.model.ViewModelBaseState


@Composable
fun appColors() =
    listOf(
        MaterialTheme.colorScheme.primary,
        MaterialColor.DeepOrange600,
        MaterialColor.PinkA700,
    )

@Composable
fun CuccinaLoader() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val infiniteTransition = rememberInfiniteTransition()
        val offsetAnimation = infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 100f,
            animationSpec = infiniteRepeatable(
                tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
            )
        )


        val brush = Brush.linearGradient(
            appColors(),
            start = Offset(offsetAnimation.value, offsetAnimation.value),
            end = Offset(x = offsetAnimation.value * 5, y = offsetAnimation.value * 3)
        )
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.cherry),
            contentDescription = "Cuccina",
            modifier = Modifier
                .graphicsLayer(alpha = 0.99f)
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawRect(brush, blendMode = BlendMode.SrcAtop)
                    }
                }
                .size(100.dp)
        )

        Text(
            text = "Cuccina",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )


    }
}

@Composable
fun getStateComponent(state: ViewModelBaseState, action: (ViewModelBaseState) -> Unit) {


    val message = when (state) {
        ViewModelBaseState.RequireAuth -> "Você precisa estar logado para acessar essa funcionalidade"
        ViewModelBaseState.DataDeletedState -> "Receita deletada com sucesso"
        ViewModelBaseState.LoadingState -> "Carregando..."
        ViewModelBaseState.LoadCompleteState -> "Carregamento completo"
        is ViewModelBaseState.DataRetrievedState -> "Receita carregada com sucesso"
        is ViewModelBaseState.DataListRetrievedState -> "Receitas carregadas com sucesso"
        is ViewModelBaseState.DataSavedState -> "Dados salvos com sucesso"
        is ViewModelBaseState.DataUpdateState -> "Dados atualizados com sucesso"
        is ViewModelBaseState.FileUploadedState -> "Arquivos enviados com sucesso"
        is ViewModelBaseState.ErrorState -> "Ocorreu um erro inesperado(${state.dataException.code.message}"
    }

    val buttonText = when (state) {
        ViewModelBaseState.RequireAuth -> "Fazer login"
        ViewModelBaseState.DataDeletedState -> "Ok"
        is ViewModelBaseState.ErrorState -> "Tentar novamente"
        else -> null
    }




    StateComponent(
        message = message,
        action = { action(state) },
        buttonText = buttonText
    )
}

@Preview(showBackground = true)
@Composable
fun CuccinaLoaderPreview() {
    CuccinaLoader()
}


fun annotatedPage(text: String, annotations: List<String>, color: Color) = buildAnnotatedString {
    append(text)
    Log.i("PageAnnotation", "annotatedPage: validating annotations -> $annotations on ($text)")
    annotations.forEach { annotation ->
        if (text.contains(annotation, true)) {
            val startIndex = text.indexOf(annotation)
            val endIndex = text.indexOf(annotation) + annotation.length
            if (startIndex != -1 && endIndex != text.length) {
                Log.i(javaClass.simpleName, "annotation: adding style to $annotation")
                addStyle(
                    SpanStyle(
                        color = color,
                        fontWeight = FontWeight.Bold
                    ),
                    start = text.indexOf(annotation),
                    end = (text.indexOf(annotation) + annotation.length)
                )
            }
        }
    }
}

@Composable
fun SimplePageView(page: Page.SimplePage) {

    Column(
        modifier = Modifier
            .background(page.backColor ?: MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize(), verticalArrangement = Arrangement.Center
    ) {
        val textColor = page.textColor ?: MaterialTheme.colorScheme.onBackground
        Text(
            text = page.title,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme
                .typography
                .headlineLarge
                .copy(textAlign = TextAlign.Center, color = textColor)
        )
        Text(
            text = annotatedPage(
                page.description,
                page.annotatedTexts,
                MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            style = MaterialTheme
                .typography
                .bodyMedium
                .copy(textAlign = TextAlign.Center, color = textColor)
        )
    }
}

@Composable
fun AnimatedTextPage(page: Page.AnimatedTextPage) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(page.backColor ?: MaterialTheme.colorScheme.background)
    ) {
        var texts = page.texts.toString().replace("[", "").replace("]", "").replace(",", "")
        val textColor = page.textColor ?: MaterialTheme.colorScheme.onBackground

        repeat(3 * page.texts.size) {
            texts += texts
        }

        Text(
            texts,
            letterSpacing = 3.sp,
            lineHeight = 50.sp,
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .graphicsLayer {
                    val scale = 2.5f
                    scaleX = scale
                    scaleY = scale
                }
                .fillMaxSize()
        )



        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = page.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme
                    .typography
                    .headlineLarge
                    .copy(
                        textAlign = TextAlign.Center,
                        color = textColor,
                        fontWeight = FontWeight.Bold,
                        shadow = Shadow(
                            color = MaterialColor.Black,
                            offset = Offset(1f, 1f),
                            blurRadius = 1.3f
                        )
                    )
            )
            Text(
                text = page.description,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme
                    .typography
                    .bodyMedium
                    .copy(
                        textAlign = TextAlign.Center,
                        shadow = Shadow(
                            color = MaterialColor.Black,
                            offset = Offset(1f, 1f),
                            blurRadius = 1.3f
                        ),
                        color = textColor
                    )
            )

        }


    }
}

@Composable
fun SuccessPageView(page: Page.SuccessPage, pageAction: () -> Unit) {
    Column(
        modifier = Modifier
            .background(page.backColor ?: MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .fillMaxSize(), verticalArrangement = Arrangement.Center
    ) {
        val textColor = page.textColor ?: MaterialTheme.colorScheme.onBackground

        val celebrateComposition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.happytoast)
        )

        val celebrateProgress by animateLottieCompositionAsState(
            celebrateComposition,
            isPlaying = true,
            iterations = LottieConstants.IterateForever
        )

        LottieAnimation(
            celebrateComposition,
            celebrateProgress,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        )


        Text(
            text = page.title,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme
                .typography
                .headlineLarge
                .copy(textAlign = TextAlign.Center, color = textColor)
        )
        Text(
            text = page.description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            style = MaterialTheme
                .typography
                .bodyMedium
                .copy(textAlign = TextAlign.Center, color = textColor)
        )

        Button(
            onClick = pageAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = page.actionText
            )
        }
    }
}

