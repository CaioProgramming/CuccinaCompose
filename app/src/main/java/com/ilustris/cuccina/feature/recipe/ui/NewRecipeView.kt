package com.ilustris.cuccina.feature.recipe.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.R
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius
import org.checkerframework.checker.units.qual.m

const val NEW_RECIPE_ROUTE = "NEW_RECIPE_ROUTE"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecipeView() {
    Column(modifier = Modifier.padding(16.dp)) {
        var recipeName by remember {
            mutableStateOf("")
        }
        Image(
            painterResource(id = R.drawable.cherry),
            contentDescription = "Enviar foto de receita",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(defaultRadius)
                )
                .padding(16.dp)
                .clip(RoundedCornerShape(defaultRadius))
        )

        TextField(
            value = recipeName,
            onValueChange = { recipeName = it },
            textStyle = MaterialTheme.typography.headlineMedium.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .background(Color.Transparent)
                .padding(vertical = 8.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Nome da receita",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )

        Text(
            text = "Ingredientes",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )
        Text(
            text = "Coloque todos os ingredientes que você utiliza na sua receita(até os secretos ;)).",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn() {}

        val buttonModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
        val buttonShape = RoundedCornerShape(defaultRadius)
        val buttonPaddingValues = PaddingValues(vertical = 4.dp)
        val buttonHorizontalArrangement = Arrangement.Start
        val buttonVerticalAlignment = Alignment.CenterVertically

        Button(
            onClick = { /*TODO*/ },
            shape = buttonShape,
            modifier = buttonModifier,
            contentPadding = buttonPaddingValues
        ) {
            Row(
                modifier = buttonModifier,
                horizontalArrangement = buttonHorizontalArrangement,
                verticalAlignment = buttonVerticalAlignment
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(text = "Adicionar ingrediente")
            }

        }

        Text(text = "Passo a Passo", style = MaterialTheme.typography.headlineSmall)

        Text(
            text = "Coloque cada etapa para fazer sua receita, de forma simples e objetiva.",
            style = MaterialTheme.typography.labelSmall
        )

        LazyColumn() {}

        Button(
            onClick = { /*TODO*/ },
            shape = buttonShape,
            modifier = buttonModifier,
            contentPadding = buttonPaddingValues
        ) {
            Row(
                modifier = buttonModifier,
                horizontalArrangement = buttonHorizontalArrangement,
                verticalAlignment = buttonVerticalAlignment
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_add_24),
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Text(text = "Adicionar etapa")
            }

        }


        Button(onClick = { /*TODO*/ }) {
            Row(modifier = Modifier.fillMaxWidth(),  horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Confirmar")
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.round_keyboard_arrow_right_24),
                    contentDescription = null
                )
            }
        }

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NewRecipeFormPreview() {
    CuccinaTheme() {
        NewRecipeView()
    }
}