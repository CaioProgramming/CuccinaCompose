@file:OptIn(ExperimentalCoilApi::class)

package com.ilustris.cuccina.feature.recipe.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.ilustris.cuccina.R
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius

const val NEW_RECIPE_ROUTE = "NEW_RECIPE_ROUTE"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecipeView() {

    var selectedImage: Uri? by remember {
        mutableStateOf(null)
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        selectedImage = it
    }
    val scrollState = rememberScrollState()


    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(10.dp)
    ) {
        var recipeName by remember {
            mutableStateOf("")
        }
        var recipeDescription by remember {
            mutableStateOf("")
        }
        val painter =
            if (selectedImage != null) rememberImagePainter(selectedImage) else painterResource(id = R.drawable.cherry)
        val buttonModifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
        val buttonShape = RoundedCornerShape(defaultRadius)
        val buttonPaddingValues = PaddingValues(vertical = 4.dp)
        val buttonHorizontalArrangement = Arrangement.Start
        val buttonVerticalAlignment = Alignment.CenterVertically
        Image(
            painter,
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
                .clickable {
                    galleryLauncher.launch("image/*")
                }
        )

        TextField(
            value = recipeName,
            onValueChange = { recipeName = it },
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
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

        TextField(
            value = recipeDescription,
            onValueChange = { recipeDescription = it },
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .background(Color.Transparent)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                    RoundedCornerShape(
                        defaultRadius
                    )
                )
                .clip(RoundedCornerShape(defaultRadius))
                .padding(vertical = 16.dp)
                .fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Descrição da receita",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )



        Text(
            text = "Ingredientes",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxWidth()
        )
        Text(
            text = "Coloque todos os ingredientes que você utiliza na sua receita(até os secretos ;)).",
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        //ADD FOREACH TO ADD INGREDIENTS

        Button(
            onClick = { /*TODO*/ },
            shape = buttonShape,
            modifier = buttonModifier,
            contentPadding = buttonPaddingValues,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
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

        Text(text = "Passo a Passo", style = MaterialTheme.typography.bodyLarge)

        Text(
            text = "Coloque cada etapa para fazer sua receita, de forma simples e objetiva.",
            style = MaterialTheme.typography.labelSmall
        )

        //ADD FOREACH TO ADD STEPS
        Button(
            onClick = { /*TODO*/ },
            shape = buttonShape,
            modifier = buttonModifier,
            contentPadding = buttonPaddingValues,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onBackground)
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

        Button(
            onClick = { /*TODO*/ }, contentPadding = buttonPaddingValues, shape = buttonShape,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = buttonModifier.padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Confirmar")
            }
        }
    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NewRecipeFormPreview() {
    CuccinaTheme {
        NewRecipeView()
    }
}