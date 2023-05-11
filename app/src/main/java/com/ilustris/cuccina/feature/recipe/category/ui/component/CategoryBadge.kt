package com.ilustris.cuccina.feature.recipe.category.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius

@Composable
fun CategoryBadge(
    category: Category,
    selectedCategory: Category?,
    categorySelected: (Category) -> Unit
) {

    fun isSelected() = category == selectedCategory

    Button(
        onClick = {
            categorySelected(category)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(defaultRadius),
        modifier = Modifier
            .wrapContentSize(align = Alignment.CenterStart)
            .padding(4.dp)
    ) {
        val contentColor =
            if (isSelected()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
        Image(
            painterResource(id = category.icon),
            colorFilter = ColorFilter.tint(contentColor),
            contentDescription = category.title,
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp)
        )
        Text(
            text = category.title.toUpperCase(Locale.current),
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = if (isSelected()) FontWeight.Black else FontWeight.Medium),
            color = contentColor
        )
    }

}

@Composable
fun CategoryIcon(
    category: Category,
    selectedCategory: Category?,
    categorySelected: (Category) -> Unit
) {

    fun isSelected() = category == selectedCategory

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize(align = Alignment.Center)
            .padding(4.dp)
    ) {

        IconButton(
            onClick = {
                categorySelected(category)
            },
            modifier = Modifier
                .wrapContentSize(align = Alignment.CenterStart)
                .padding(16.dp)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface, CircleShape)
                .clip(CircleShape)
        ) {
            val contentColor =
                if (isSelected()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            Image(
                painterResource(id = category.icon),
                colorFilter = ColorFilter.tint(contentColor),
                contentDescription = category.title,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }

        Text(
            text = category.title.toUpperCase(Locale.current),
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = if (isSelected()) FontWeight.Black else FontWeight.Light,
                textAlign = TextAlign.Center
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

    }


}

@Preview(showBackground = true)
@Composable
fun BadgePreview() {
    CuccinaTheme {
        LazyColumn {
            item {
                LazyRow {
                    items(Category.values().size) {
                        CategoryBadge(Category.values()[it], Category.values().random()) {

                        }
                    }
                }
            }
            items((Category.values().size)) {
                CategoryIcon(Category.values()[it], Category.values().random()) {

                }
            }
        }

    }
}