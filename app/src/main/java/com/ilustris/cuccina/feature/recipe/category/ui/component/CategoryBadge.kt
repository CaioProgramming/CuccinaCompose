package com.ilustris.cuccina.feature.home.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.recipe.category.domain.model.Category
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import com.ilustris.cuccina.ui.theme.defaultRadius

@Composable
fun CategoryBadge(category: Category, categorySelected: (Category) -> Unit) {

    Button(
        onClick = { categorySelected(category) },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onBackground),
        shape = RoundedCornerShape(defaultRadius),
        modifier = Modifier.wrapContentSize(align = Alignment.CenterStart).padding(12.dp)
    ) {
        Image(
            painterResource(id = category.icon),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            contentDescription = category.description,
            modifier = Modifier
                .size(24.dp)
                .padding(4.dp)
        )
        Text(text = category.description.toUpperCase(Locale.current), style = MaterialTheme.typography.labelMedium)
    }

}

@Preview
@Composable
fun BadgePreview() {
    CuccinaTheme() {
        LazyRow() {
            items(Category.values().size) {
                CategoryBadge(Category.values()[it]) {

                }
            }
        }
    }
}