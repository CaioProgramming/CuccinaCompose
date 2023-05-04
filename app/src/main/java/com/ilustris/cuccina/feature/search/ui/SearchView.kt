package com.ilustris.cuccina.feature.search.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.ilustris.cuccina.ui.theme.defaultRadius

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(performQuery: (String) -> Unit) {
    var query by remember {
        mutableStateOf("")
    }
    SearchBar(
        query = query,
        onQueryChange = {
            query = it
        },
        onSearch = {
            performQuery(it)
        },
        active = true,
        {},
        placeholder = {
            Text(text = "Pesquisar receitas")
        },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            Icon(Icons.Default.Check, contentDescription = null)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            defaultRadius
        )
    ) {

    }
}