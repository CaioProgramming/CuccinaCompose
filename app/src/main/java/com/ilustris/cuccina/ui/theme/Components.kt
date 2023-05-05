package com.ilustris.cuccina.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.ui.component.StateComponent
import com.silent.ilustriscore.core.model.ViewModelBaseState


@Composable
fun appColors() =
    listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.tertiary
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
            end = Offset(x = offsetAnimation.value * 3, y = offsetAnimation.value)
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
