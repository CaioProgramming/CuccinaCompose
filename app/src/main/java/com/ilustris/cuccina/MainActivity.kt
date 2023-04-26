@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class
)

package com.ilustris.cuccina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.ilustris.cuccina.feature.home.ui.HOME_ROUTE
import com.ilustris.cuccina.feature.recipe.form.presentation.ui.NEW_RECIPE_ROUTE
import com.ilustris.cuccina.navigation.BottomNavigation
import com.ilustris.cuccina.navigation.NavigationGraph
import com.ilustris.cuccina.ui.theme.CuccinaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appName = LocalContext.current.getString(R.string.app_name)
            CuccinaTheme {
                val navController = rememberNavController()
                var title by remember {
                    mutableStateOf(appName)
                }
                Scaffold(topBar = {
                    TopAppBar(
                        title = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painterResource(id = R.drawable.cherry),
                                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                                    contentDescription = "category",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .padding(4.dp)
                                )
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                    )
                }, bottomBar = { BottomNavigation(navController = navController) }) {
                    NavigationGraph(navController = navController, paddingValues = it)
                }
                LaunchedEffect(navController) {
                    navController.currentBackStackEntryFlow.collect { backStackEntry ->
                        title = getRouteTitle(backStackEntry.destination.route)
                    }
                }
            }
        }
    }
}

fun getRouteTitle(route: String?) : String {
    if (route == null) return "Cuccina"
   return when(route) {
        HOME_ROUTE -> "Cuccina"
        NEW_RECIPE_ROUTE -> "Nova receita"
        else -> "Cuccina"
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    CuccinaTheme {
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painterResource(id = R.drawable.cherry),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                            contentDescription = "category",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(4.dp)
                        )
                        Text(
                            text = LocalContext.current.getString(R.string.app_name),
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Black)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
            bottomBar = { BottomNavigation(navController = navController) }) {
            NavigationGraph(navController = navController, paddingValues = it)
        }
    }
}