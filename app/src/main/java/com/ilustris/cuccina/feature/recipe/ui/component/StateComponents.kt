package com.ilustris.cuccina.feature.recipe.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.ilustris.cuccina.R
import com.silent.ilustriscore.core.model.ViewModelBaseState
import com.silent.ilustriscore.core.utilities.delayedFunction

@Composable
fun StateComponent(
    animation: Int = R.raw.cakerun,
    message: String,
    buttonText: String? = null,
    dismiss: Boolean = false,
    action: (() -> Unit)? = null,
) {

    val visibility by remember {
        mutableStateOf(false).apply {
            delayedFunction(3000) {
                this.value = true
            }
        }
    }

    AnimatedVisibility(visible = visibility, enter = fadeIn(), exit = fadeOut()) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            val celebrateComposition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(animation)
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
                text = message,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center)
            )

            if (action != null && buttonText != null) {
                Button(
                    onClick = action, modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = buttonText)
                }
            }


        }

    }


}


@Composable
fun getStateComponent(state: ViewModelBaseState, action: (ViewModelBaseState) -> Unit) {


    val message = when (state) {
        ViewModelBaseState.RequireAuth -> "Faça login para começar a usar o app."
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
        ViewModelBaseState.DataDeletedState -> "Ok"
        ViewModelBaseState.RequireAuth -> "Fazer login"
        is ViewModelBaseState.ErrorState -> "Tentar novamente"
        is ViewModelBaseState.DataSavedState -> "Ok"
        else -> null
    }

    val animation = when (state) {
        ViewModelBaseState.RequireAuth -> R.raw.noodles
        is ViewModelBaseState.ErrorState -> R.raw.sad_planet
        is ViewModelBaseState.DataSavedState -> R.raw.happytoast
        else -> R.raw.cakerun
    }

    StateComponent(
        animation = animation,
        message = message,
        action = { action(state) },
        buttonText = buttonText
    )
}

