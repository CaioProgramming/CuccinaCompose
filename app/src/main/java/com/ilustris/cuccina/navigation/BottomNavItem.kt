package com.ilustris.cuccina.navigation

import ai.atick.material.MaterialColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ilustris.cuccina.R
import com.ilustris.cuccina.feature.home.ui.HOME_ROUTE
import com.ilustris.cuccina.feature.home.ui.HomeView
import com.ilustris.cuccina.feature.recipe.ui.NEW_RECIPE_ROUTE
import com.ilustris.cuccina.feature.recipe.ui.NewRecipeView
import com.ilustris.cuccina.ui.theme.defaultRadius

enum class BottomNavItem(val title: String, var icon: Int = R.drawable.cherry, val route: String) {
    HOME(title = "Home", route = HOME_ROUTE, icon = R.drawable.round_home_24),
    NEW_RECIPE(title = "Publicar", route = NEW_RECIPE_ROUTE, icon = R.drawable.cook),
    PROFILE(title = "Eu", route = "PROFILE_ROUTE")

}

@Composable
fun NavigationGraph(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = HOME_ROUTE,
        modifier = Modifier.padding(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        )
    ) {
        composable(BottomNavItem.HOME.route) {
            HomeView()
        }

        composable(BottomNavItem.NEW_RECIPE.route) {
            NewRecipeView()
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    androidx.compose.material.BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialColor.White,
        modifier = Modifier.clip(
            RoundedCornerShape(
                topEnd = defaultRadius,
                topStart = defaultRadius
            )
        )
    ) {
        val routes = BottomNavItem.values()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination
        routes.forEach { item ->
            val isSelected = currentRoute?.hierarchy?.any { it.route == item.route } == true

            BottomNavigationItem(selected = isSelected,
                label = { },
                icon = {
                    Image(
                        painterResource(item.icon),
                        contentDescription = item.title,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(
                            if (isSelected) MaterialColor.White else MaterialColor.White.copy(
                                alpha = 0.5f
                            )
                        )
                    )
                },
                selectedContentColor = MaterialColor.White,
                unselectedContentColor = MaterialColor.White.copy(alpha = 0.3f),
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}
